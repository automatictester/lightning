package uk.co.automatictester.lightning.data;

import com.univocity.parsers.common.processor.ConcurrentRowProcessor;
import com.univocity.parsers.common.processor.RowListProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import uk.co.automatictester.lightning.exceptions.CSVFileIOException;
import uk.co.automatictester.lightning.exceptions.CSVFileNoTransactionsException;
import uk.co.automatictester.lightning.exceptions.CSVFileNonexistentHostAndMetricException;
import uk.co.automatictester.lightning.s3.S3Client;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

// TODO: extract common methods for this and JMeterTransactions class to a parent abstract class
public class PerfMonEntries extends ArrayList<String[]> {

    private static final int TIMESTAMP_INDEX = 0;
    private static final int VALUE_INDEX = 1;
    private static final int HOST_AND_METRIC_INDEX = 2;

    private static S3Client s3Client;

    private PerfMonEntries() {
    }

    private PerfMonEntries(File perfMonCsvFile) {
        loadFromFile(perfMonCsvFile);
        throwExceptionIfEmpty();
    }

    private PerfMonEntries(List<String[]> perfMonEntries) {
        this.addAll(perfMonEntries);
    }

    private PerfMonEntries(String region, String bucket, String csvObject) {
        s3Client = new S3Client(region, bucket);
        loadFromS3Object(csvObject);
        throwExceptionIfEmpty();
    }

    public static PerfMonEntries fromFile(File perfMonCvsFile) {
        return new PerfMonEntries(perfMonCvsFile);
    }

    public static PerfMonEntries fromList(List<String[]> perfMonEntries) {
        return new PerfMonEntries(perfMonEntries);
    }

    public static PerfMonEntries fromS3Object(String region, String bucket, String csvObject) {
        return new PerfMonEntries(region, bucket, csvObject);
    }

    public PerfMonEntries getEntriesWith(String hostAndMetric) {
        PerfMonEntries filteredDataEntries = new PerfMonEntries();
        for (String[] dataEntry : this) {
            if (dataEntry[HOST_AND_METRIC_INDEX].equals(hostAndMetric)) {
                filteredDataEntries.add(dataEntry);
            }
        }
        if (filteredDataEntries.size() == 0) {
            throw new CSVFileNonexistentHostAndMetricException(hostAndMetric);
        }
        return filteredDataEntries;
    }

    protected CsvParser getParser() {
        CsvParserSettings parserSettings = new CsvParserSettings();
        parserSettings.setLineSeparatorDetectionEnabled(true);
        parserSettings.setHeaderExtractionEnabled(false);
        parserSettings.selectIndexes(TIMESTAMP_INDEX, VALUE_INDEX, HOST_AND_METRIC_INDEX);
        RowListProcessor rowProcessor = new RowListProcessor();
        parserSettings.setProcessor(new ConcurrentRowProcessor(rowProcessor));
        return new CsvParser(parserSettings);
    }

    private void loadFromFile(File perfMonCsvFile) {
        try (FileReader fr = new FileReader(perfMonCsvFile)) {
            this.addAll(getParser().parseAll(fr));
        } catch (IOException e) {
            throw new CSVFileIOException(e);
        }
    }

    private void throwExceptionIfEmpty() {
        if (this.isEmpty()) {
            throw new CSVFileNoTransactionsException();
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
}
