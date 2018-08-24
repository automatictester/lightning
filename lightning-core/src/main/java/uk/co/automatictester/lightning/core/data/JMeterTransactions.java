package uk.co.automatictester.lightning.core.data;

import com.univocity.parsers.common.processor.ConcurrentRowProcessor;
import com.univocity.parsers.common.processor.RowListProcessor;
import com.univocity.parsers.csv.CsvParserSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.automatictester.lightning.core.exceptions.CSVFileNonexistentLabelException;
import uk.co.automatictester.lightning.core.s3.S3Client;

import java.io.File;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

import static java.lang.Math.min;
import static uk.co.automatictester.lightning.core.enums.JMeterColumns.*;

public class JMeterTransactions extends CsvEntries {

    private static final int MAX_NUMBER_OF_LONGEST_TRANSACTIONS = 5;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    protected JMeterTransactions() {
    }

    private JMeterTransactions(File csvFile) {
        long start = System.currentTimeMillis();
        log.debug("Reading CSV file - start");

        loadFromFile(csvFile);
        throwExceptionIfEmpty();

        long finish = System.currentTimeMillis();
        long millisecondsBetween = finish - start;
        log.debug("Reading CSV file - finish, read {} rows, took {}ms", entries.size(), millisecondsBetween);
    }

    private JMeterTransactions(String region, String bucket, String csvObject) {
        s3Client = S3Client.getInstance(region, bucket);
        long start = System.currentTimeMillis();
        log.debug("Reading CSV file - start");

        loadFromS3Object(csvObject);
        throwExceptionIfEmpty();

        long finish = System.currentTimeMillis();
        long millisecondsBetween = finish - start;
        log.debug("Reading CSV file - finish, read {} rows, took {}ms", entries.size(), millisecondsBetween);
    }

    private JMeterTransactions(List<String[]> entries) {
        super(entries);
    }

    public static JMeterTransactions fromFile(File csvFile) {
        return new JMeterTransactions(csvFile);
    }

    public static JMeterTransactions fromS3Object(String region, String bucket, String csvObject) {
        return new JMeterTransactions(region, bucket, csvObject);
    }

    public static JMeterTransactions fromList(List<String[]> entries) {
        return new JMeterTransactions(entries);
    }

    public JMeterTransactions getTransactionsWith(String expectedTransactionName) {
        BiPredicate<String, String> equals = String::equals;
        return getTransactions(expectedTransactionName, equals);
    }

    public JMeterTransactions getTransactionsMatching(String expectedTransactionName) {
        BiPredicate<String, String> matches = String::matches;
        return getTransactions(expectedTransactionName, matches);
    }

    public List<Integer> getLongestTransactions() {
        List<Integer> transactionDurations = getTransactionDurationsDesc();
        return getLongestTransactionDurations(transactionDurations);
    }

    public int getFailCount() {
        return (int) entries.stream()
                .filter(t -> "false".equals(t[TRANSACTION_RESULT_INDEX.getValue()]))
                .count();
    }

    public long getFirstTransactionTimestamp() {
        BiPredicate<Long, Long> isBefore = (edgeTimestamp, currentTimestamp) -> edgeTimestamp == 0 || currentTimestamp < edgeTimestamp;
        return getEdgeTransactionTimestamp(isBefore);
    }

    public long getLastTransactionTimestamp() {
        BiPredicate<Long, Long> isAfter = (edgeTimestamp, currentTimestamp) -> edgeTimestamp == 0 || currentTimestamp > edgeTimestamp;
        return getEdgeTransactionTimestamp(isAfter);
    }

    private JMeterTransactions getTransactions(String expectedTransactionName, BiPredicate<String, String> trait) {
        JMeterTransactions transactions = new JMeterTransactions();
        entries.forEach(transaction -> {
            String transactionName = transaction[TRANSACTION_LABEL_INDEX.getValue()];
            boolean isInScope = trait.test(transactionName, expectedTransactionName);
            if (isInScope) {
                transactions.add(transaction);
            }
        });
        if (transactions.size() == 0) {
            throw new CSVFileNonexistentLabelException(expectedTransactionName);
        }
        return transactions;
    }

    private long getEdgeTransactionTimestamp(BiPredicate<Long, Long> trait) {
        long edgeTimestamp = 0;
        for (String[] transaction : entries) {
            long currentTransactionTimestamp = Long.parseLong(transaction[TRANSACTION_TIMESTAMP.getValue()]);
            boolean isEdgeTimestamp = trait.test(edgeTimestamp, currentTransactionTimestamp);
            if (isEdgeTimestamp) {
                edgeTimestamp = currentTransactionTimestamp;
            }
        }
        return edgeTimestamp;
    }

    private List<Integer> getTransactionDurationsDesc() {
        return entries.stream()
                .map(e -> Integer.parseInt(e[TRANSACTION_DURATION_INDEX.getValue()]))
                .sorted((i1, i2) -> i2 - i1)
                .collect(Collectors.toList());
    }

    private List<Integer> getLongestTransactionDurations(List<Integer> transactionDurations) {
        int transactionDurationsCount = min(transactionDurations.size(), MAX_NUMBER_OF_LONGEST_TRANSACTIONS);
        return transactionDurations.stream()
                .limit(transactionDurationsCount)
                .collect(Collectors.toList());
    }

    protected CsvParserSettings getCsvParserSettings() {
        CsvParserSettings parserSettings = new CsvParserSettings();
        parserSettings.setLineSeparatorDetectionEnabled(true);
        parserSettings.setHeaderExtractionEnabled(true);
        parserSettings.selectFields("label", "elapsed", "success", "timeStamp");
        RowListProcessor rowProcessor = new RowListProcessor();
        ConcurrentRowProcessor concurrentRowProcessor = new ConcurrentRowProcessor(rowProcessor);
        parserSettings.setProcessor(concurrentRowProcessor);
        return parserSettings;
    }
}
