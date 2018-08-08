package uk.co.automatictester.lightning.data;

import com.univocity.parsers.common.processor.ConcurrentRowProcessor;
import com.univocity.parsers.common.processor.RowListProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.automatictester.lightning.exceptions.CSVFileIOException;
import uk.co.automatictester.lightning.exceptions.CSVFileNoTransactionsException;
import uk.co.automatictester.lightning.exceptions.CSVFileNonexistentLabelException;
import uk.co.automatictester.lightning.s3.S3Client;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JMeterTransactions extends ArrayList<String[]> {

    private static final int TRANSACTION_LABEL_INDEX = 0;
    private static final int TRANSACTION_DURATION_INDEX = 1;
    private static final int TRANSACTION_RESULT_INDEX = 2;
    private static final int TRANSACTION_TIMESTAMP = 3;
    private static final int MAX_NUMBER_OF_LONGEST_TRANSACTIONS = 5;

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private static S3Client s3Client;

    protected JMeterTransactions() {
    }

    private JMeterTransactions(File csvFile) {
        long start = System.currentTimeMillis();
        log.debug("Reading CSV file - start");

        loadFromFile(csvFile);
        throwExceptionIfEmpty();

        long finish = System.currentTimeMillis();
        long millisecondsBetween = finish - start;
        log.debug("Reading CSV file - finish, read {} rows, took {}ms", this.size(), millisecondsBetween);
    }

    private JMeterTransactions(String region, String bucket, String csvObject) {
        s3Client = new S3Client(region, bucket);
        long start = System.currentTimeMillis();
        log.debug("Reading CSV file - start");

        loadFromS3Object(csvObject);
        throwExceptionIfEmpty();

        long finish = System.currentTimeMillis();
        long millisecondsBetween = finish - start;
        log.debug("Reading CSV file - finish, read {} rows, took {}ms", this.size(), millisecondsBetween);
    }

    private JMeterTransactions(List<String[]> jmeterTransactions) {
        this.addAll(jmeterTransactions);
    }

    public static JMeterTransactions fromList(List<String[]> jmeterTransactions) {
        return new JMeterTransactions(jmeterTransactions);
    }

    public static JMeterTransactions fromFile(File csvFile) {
        return new JMeterTransactions(csvFile);
    }

    public static JMeterTransactions fromS3Object(String region, String bucket, String csvObject) {
        return new JMeterTransactions(region, bucket, csvObject);
    }

    public JMeterTransactions getTransactionsWith(String label) {
        JMeterTransactions transactions = new JMeterTransactions();
        for (String[] transaction : this) {
            if (transaction[TRANSACTION_LABEL_INDEX].equals(label)) {
                transactions.add(transaction);
            }
        }
        if (transactions.size() == 0) {
            throw new CSVFileNonexistentLabelException(label);
        }
        return transactions;
    }

    public JMeterTransactions getTransactionsMatching(String labelPattern) {
        JMeterTransactions transactions = new JMeterTransactions();
        for (String[] transaction : this) {
            if (transaction[TRANSACTION_LABEL_INDEX].matches(labelPattern)) {
                transactions.add(transaction);
            }
        }
        if (transactions.size() == 0) {
            throw new CSVFileNonexistentLabelException(labelPattern);
        }
        return transactions;
    }

    public List<Integer> getLongestTransactions() {
        List<Integer> transactionDurations = getTransactionDurationsDesc();
        return getLongestTransactionDurations(transactionDurations);
    }

    public int getFailCount() {
        int failCount = 0;
        for (String[] transaction : this) {
            if ("false".equals(transaction[TRANSACTION_RESULT_INDEX])) {
                failCount++;
            }
        }
        return failCount;
    }

    public double getThroughput() {
        double transactionTimespanInMilliseconds = getLastTransactionTimestamp() - getFirstTransactionTimestamp();
        return size() / (transactionTimespanInMilliseconds / 1000);
    }

    protected CsvParser getParser() {
        CsvParserSettings parserSettings = new CsvParserSettings();
        parserSettings.setLineSeparatorDetectionEnabled(true);
        parserSettings.setHeaderExtractionEnabled(true);
        parserSettings.selectFields("label", "elapsed", "success", "timeStamp");
        RowListProcessor rowProcessor = new RowListProcessor();
        parserSettings.setProcessor(new ConcurrentRowProcessor(rowProcessor));
        return new CsvParser(parserSettings);
    }

    private List<Integer> getTransactionDurationsDesc() {
        List<Integer> transactionDurations = new ArrayList<>();
        for (String[] transaction : this) {
            int elapsed = Integer.parseInt(transaction[TRANSACTION_DURATION_INDEX]);
            transactionDurations.add(elapsed);
        }
        Collections.sort(transactionDurations);
        Collections.reverse(transactionDurations);
        return transactionDurations;
    }

    private List<Integer> getLongestTransactionDurations(List<Integer> transactionDurations) {
        // TODO: (Java 8) potential for refactor
        int transactionDurationsCount = (transactionDurations.size() >= MAX_NUMBER_OF_LONGEST_TRANSACTIONS) ? MAX_NUMBER_OF_LONGEST_TRANSACTIONS : transactionDurations.size();
        return transactionDurations.subList(0, transactionDurationsCount);
    }

    private long getFirstTransactionTimestamp() {
        long minTimestamp = 0;
        for (String[] transaction : this) {
            long currentTransactionTimestamp = Long.parseLong(transaction[TRANSACTION_TIMESTAMP]);
            if (minTimestamp == 0 || currentTransactionTimestamp < minTimestamp) {
                minTimestamp = currentTransactionTimestamp;
            }
        }
        return minTimestamp;
    }

    private long getLastTransactionTimestamp() {
        long maxTimestamp = 0;
        for (String[] transaction : this) {
            long currentTransactionTimestamp = Long.parseLong(transaction[TRANSACTION_TIMESTAMP]);
            if (maxTimestamp == 0 || currentTransactionTimestamp > maxTimestamp) {
                maxTimestamp = currentTransactionTimestamp;
            }
        }
        return maxTimestamp;
    }

    private void loadFromFile(File csvFile) {
        try (FileReader fr = new FileReader(csvFile)) {
            this.addAll(getParser().parseAll(fr));
        } catch (IOException e) {
            throw new CSVFileIOException(e);
        }
    }

    private void loadFromS3Object(String csvObject) {
        String csvObjectContent = s3Client.getS3ObjectContent(csvObject);
        try (InputStreamReader isr = new InputStreamReader(new ByteArrayInputStream(csvObjectContent.getBytes()))) {
            this.addAll(getParser().parseAll(isr));
        } catch (IOException e) {
            throw new CSVFileIOException(e);
        }
    }

    private void throwExceptionIfEmpty() {
        if (this.isEmpty()) {
            throw new CSVFileNoTransactionsException();
        }
    }
}
