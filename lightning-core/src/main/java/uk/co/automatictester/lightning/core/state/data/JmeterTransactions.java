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
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static java.lang.Math.min;
import static java.util.Comparator.reverseOrder;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;
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
        Predicate<String[]> isTransactionNameEqualToExpected = t -> t[TRANSACTION_LABEL.getColumn()].equals(expectedTransactionName);
        return getTransactions(isTransactionNameEqualToExpected, expectedTransactionName);
    }

    public JmeterTransactions getTransactionsMatching(String expectedTransactionName) {
        Pattern pattern = Pattern.compile(expectedTransactionName);
        Predicate<String[]> isTransactionNameMatchingExpected = t -> pattern.matcher(t[TRANSACTION_LABEL.getColumn()]).matches();
        return getTransactions(isTransactionNameMatchingExpected, expectedTransactionName);
    }

    private JmeterTransactions getTransactions(Predicate<String[]> predicate, String expectedTransactionName) {
        List<String[]> transactions = entries.stream()
                .filter(predicate)
                .collect(collectingAndThen(toList(), filteredList -> returnListOrThrowExceptionIfEmpty(filteredList, expectedTransactionName)));
        return JmeterTransactions.fromList(transactions);
    }

    public List<Integer> getLongestTransactions() {
        int numberOfLongestTransactions = min(entries.size(), MAX_NUMBER_OF_LONGEST_TRANSACTIONS);
        return entries.stream()
                .map(e -> Integer.parseInt(e[TRANSACTION_DURATION.getColumn()]))
                .sorted(reverseOrder())
                .limit(numberOfLongestTransactions)
                .collect(toList());
    }

    public int getFailCount() {
        return (int) entries.stream()
                .filter(t -> "false".equals(t[TRANSACTION_RESULT.getColumn()]))
                .count();
    }

    public long getFirstTransactionTimestamp() {
        return entries.stream()
                .mapToLong(e -> Long.parseLong(e[TRANSACTION_TIMESTAMP.getColumn()]))
                .sorted()
                .limit(1)
                .findFirst()
                .getAsLong();
    }

    public long getLastTransactionTimestamp() {
        return entries.stream()
                .map(e -> Long.parseLong(e[TRANSACTION_TIMESTAMP.getColumn()]))
                .sorted(reverseOrder())
                .limit(1)
                .mapToLong(e -> e)
                .findFirst()
                .getAsLong();
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

    private List<String[]> returnListOrThrowExceptionIfEmpty(List<String[]> list, String expectedTransactionName) {
        if (list.size() == 0) {
            throw new CSVFileNonexistentLabelException(expectedTransactionName);
        }
        return list;
    }
}
