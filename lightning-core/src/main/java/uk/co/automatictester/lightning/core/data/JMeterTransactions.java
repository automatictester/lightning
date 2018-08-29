package uk.co.automatictester.lightning.core.data;

import com.univocity.parsers.common.processor.ConcurrentRowProcessor;
import com.univocity.parsers.common.processor.RowListProcessor;
import com.univocity.parsers.csv.CsvParserSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.automatictester.lightning.core.exceptions.CSVFileNonexistentLabelException;
import uk.co.automatictester.lightning.core.s3.client.S3ClientFlyweightFactory;

import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

import static java.lang.Math.min;
import static uk.co.automatictester.lightning.core.enums.JMeterColumns.*;

public class JMeterTransactions extends CsvEntries {

    private static final int MAX_NUMBER_OF_LONGEST_TRANSACTIONS = 5;
    private Logger log = LoggerFactory.getLogger(this.getClass());

    protected JMeterTransactions() {
    }

    private JMeterTransactions(File csvFile) {
        Instant start = Instant.now();
        log.debug("Reading CSV file - start");

        loadFromFile(csvFile);
        throwExceptionIfEmpty();

        Instant finish = Instant.now();
        Duration duration = Duration.between(start, finish);
        log.debug("Reading CSV file - finish, read {} rows, took {}ms", entries.size(), duration.toMillis());
    }

    private JMeterTransactions(String region, String bucket, String key) {
        s3Client = S3ClientFlyweightFactory.getInstance(region).setBucket(bucket);
        Instant start = Instant.now();
        log.debug("Reading CSV file - start");

        loadFromS3Object(key);
        throwExceptionIfEmpty();

        Instant finish = Instant.now();
        Duration duration = Duration.between(start, finish);
        log.debug("Reading CSV file - finish, read {} rows, took {}ms", entries.size(), duration.toMillis());
    }

    private JMeterTransactions(List<String[]> entries) {
        super(entries);
    }

    public static JMeterTransactions fromFile(File csvFile) {
        return new JMeterTransactions(csvFile);
    }

    public static JMeterTransactions fromS3Object(String region, String bucket, String key) {
        return new JMeterTransactions(region, bucket, key);
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

    @Override
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

    private JMeterTransactions getTransactions(String expectedTransactionName, BiPredicate<String, String> biPredicate) {
        JMeterTransactions transactions = new JMeterTransactions();
        entries.forEach(transaction -> {
            String transactionName = transaction[TRANSACTION_LABEL_INDEX.getValue()];
            boolean isInScope = biPredicate.test(transactionName, expectedTransactionName);
            if (isInScope) {
                transactions.add(transaction);
            }
        });
        if (transactions.size() == 0) {
            throw new CSVFileNonexistentLabelException(expectedTransactionName);
        }
        return transactions;
    }

    private long getEdgeTransactionTimestamp(BiPredicate<Long, Long> biPredicate) {
        long edgeTimestamp = 0;
        for (String[] transaction : entries) {
            long currentTransactionTimestamp = Long.parseLong(transaction[TRANSACTION_TIMESTAMP.getValue()]);
            boolean isEdgeTimestamp = biPredicate.test(edgeTimestamp, currentTransactionTimestamp);
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
}
