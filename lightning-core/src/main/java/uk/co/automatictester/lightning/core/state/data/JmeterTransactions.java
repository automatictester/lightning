package uk.co.automatictester.lightning.core.state.data;

import com.univocity.parsers.common.processor.ConcurrentRowProcessor;
import com.univocity.parsers.common.processor.RowListProcessor;
import com.univocity.parsers.csv.CsvParserSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.automatictester.lightning.core.exceptions.CSVFileNonexistentLabelException;
import uk.co.automatictester.lightning.core.s3client.factory.S3ClientFlyweightFactory;
import uk.co.automatictester.lightning.core.state.data.base.AbstractCsvEntries;

import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.lang.Math.min;
import static uk.co.automatictester.lightning.core.enums.JmeterColumns.*;

public class JmeterTransactions extends AbstractCsvEntries {

    private static final int MAX_NUMBER_OF_LONGEST_TRANSACTIONS = 5;
    private static final Logger log = LoggerFactory.getLogger(JmeterTransactions.class);

    private JmeterTransactions(File csvFile) {
        Instant start = Instant.now();
        log.debug("Reading CSV file - start");

        loadFromFile(csvFile);
        throwExceptionIfEmpty();

        Instant finish = Instant.now();
        Duration duration = Duration.between(start, finish);
        log.debug("Reading CSV file - finish, read {} rows, took {}ms", entries.size(), duration.toMillis());
    }

    private JmeterTransactions(String region, String bucket, String key) {
        s3Client = S3ClientFlyweightFactory.getInstance(region).setBucket(bucket);
        Instant start = Instant.now();
        log.debug("Reading CSV file - start");

        loadFromS3Object(key);
        throwExceptionIfEmpty();

        Instant finish = Instant.now();
        Duration duration = Duration.between(start, finish);
        log.debug("Reading CSV file - finish, read {} rows, took {}ms", entries.size(), duration.toMillis());
    }

    private JmeterTransactions(List<String[]> entries) {
        super(entries);
    }

    public static JmeterTransactions fromFile(File csvFile) {
        return new JmeterTransactions(csvFile);
    }

    public static JmeterTransactions fromS3Object(String region, String bucket, String key) {
        return new JmeterTransactions(region, bucket, key);
    }

    public static JmeterTransactions fromList(List<String[]> entries) {
        return new JmeterTransactions(entries);
    }

    public JmeterTransactions getTransactionsWith(String expectedTransactionName) {
        List<String[]> transactions = new ArrayList<>();
        entries.forEach(transaction -> {
            String transactionName = transaction[TRANSACTION_LABEL.getColumn()];
            boolean isInScope = transactionName.equals(expectedTransactionName);
            if (isInScope) {
                transactions.add(transaction);
            }
        });
        if (transactions.size() == 0) {
            throw new CSVFileNonexistentLabelException(expectedTransactionName);
        }
        return JmeterTransactions.fromList(transactions);
    }

    public JmeterTransactions getTransactionsMatching(String expectedTransactionName) {
        Pattern pattern = Pattern.compile(expectedTransactionName);
        List<String[]> transactions = new ArrayList<>();
        entries.forEach(transaction -> {
            String transactionName = transaction[TRANSACTION_LABEL.getColumn()];
            boolean isInScope = pattern.matcher(transactionName).matches();
            if (isInScope) {
                transactions.add(transaction);
            }
        });
        if (transactions.size() == 0) {
            throw new CSVFileNonexistentLabelException(expectedTransactionName);
        }
        return JmeterTransactions.fromList(transactions);
    }

    public List<Integer> getLongestTransactions() {
        List<Integer> transactionDurations = getTransactionDurationsDesc();
        return getLongestTransactionDurations(transactionDurations);
    }

    public int getFailCount() {
        return (int) entries.stream()
                .filter(t -> "false".equals(t[TRANSACTION_RESULT.getColumn()]))
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

    public String getJmeterReport() {
        return String.format("Transactions executed: %d, failed: %d", entries.size(), getFailCount());
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

    private long getEdgeTransactionTimestamp(BiPredicate<Long, Long> biPredicate) {
        long edgeTimestamp = 0;
        for (String[] transaction : entries) {
            long currentTransactionTimestamp = Long.parseLong(transaction[TRANSACTION_TIMESTAMP.getColumn()]);
            boolean isEdgeTimestamp = biPredicate.test(edgeTimestamp, currentTransactionTimestamp);
            if (isEdgeTimestamp) {
                edgeTimestamp = currentTransactionTimestamp;
            }
        }
        return edgeTimestamp;
    }

    private List<Integer> getTransactionDurationsDesc() {
        return entries.stream()
                .map(e -> Integer.parseInt(e[TRANSACTION_DURATION.getColumn()]))
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
