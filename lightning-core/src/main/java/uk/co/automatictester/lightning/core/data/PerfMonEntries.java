package uk.co.automatictester.lightning.core.data;

import com.univocity.parsers.common.processor.ConcurrentRowProcessor;
import com.univocity.parsers.common.processor.RowListProcessor;
import com.univocity.parsers.csv.CsvParserSettings;
import uk.co.automatictester.lightning.core.exceptions.CSVFileNonexistentHostAndMetricException;
import uk.co.automatictester.lightning.core.s3.S3Client;

import java.io.File;
import java.util.List;

import static uk.co.automatictester.lightning.core.enums.PerfMonColumns.*;

public class PerfMonEntries extends CsvEntries {

    private PerfMonEntries() {
    }

    private PerfMonEntries(File perfMonCsvFile) {
        loadFromFile(perfMonCsvFile);
        throwExceptionIfEmpty();
    }

    private PerfMonEntries(List<String[]> perfMonEntries) {
        super(perfMonEntries);
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
        for (String[] dataEntry : entries) {
            if (dataEntry[HOST_AND_METRIC_INDEX.getValue()].equals(hostAndMetric)) {
                filteredDataEntries.add(dataEntry);
            }
        }
        if (filteredDataEntries.size() == 0) {
            throw new CSVFileNonexistentHostAndMetricException(hostAndMetric);
        }
        return filteredDataEntries;
    }

    protected CsvParserSettings getCsvParserSettings() {
        CsvParserSettings parserSettings = new CsvParserSettings();
        parserSettings.setLineSeparatorDetectionEnabled(true);
        parserSettings.setHeaderExtractionEnabled(false);
        parserSettings.selectIndexes(TIMESTAMP_INDEX.getValue(), VALUE_INDEX.getValue(), HOST_AND_METRIC_INDEX.getValue());
        RowListProcessor rowProcessor = new RowListProcessor();
        ConcurrentRowProcessor concurrentRowProcessor = new ConcurrentRowProcessor(rowProcessor);
        parserSettings.setProcessor(concurrentRowProcessor);
        return parserSettings;
    }
}
