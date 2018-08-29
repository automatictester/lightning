package uk.co.automatictester.lightning.core.data;

import com.univocity.parsers.common.processor.ConcurrentRowProcessor;
import com.univocity.parsers.common.processor.RowListProcessor;
import com.univocity.parsers.csv.CsvParserSettings;
import uk.co.automatictester.lightning.core.exceptions.CSVFileNonexistentHostAndMetricException;
import uk.co.automatictester.lightning.core.s3.client.S3ClientFlyweightFactory;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

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

    private PerfMonEntries(String region, String bucket, String key) {
        s3Client = S3ClientFlyweightFactory.getInstance(region).setS3Bucket(bucket);
        loadFromS3Object(key);
        throwExceptionIfEmpty();
    }

    public static PerfMonEntries from(File perfMonCvsFile) {
        return new PerfMonEntries(perfMonCvsFile);
    }

    public static PerfMonEntries from(List<String[]> perfMonEntries) {
        return new PerfMonEntries(perfMonEntries);
    }

    public static PerfMonEntries from(String region, String bucket, String key) {
        return new PerfMonEntries(region, bucket, key);
    }

    public PerfMonEntries getEntriesWith(String hostAndMetric) {
        List<String[]> list = entries.stream()
                .filter(e -> e[HOST_AND_METRIC_INDEX.getValue()].equals(hostAndMetric))
                .collect(Collectors.toList());
        PerfMonEntries filteredDataEntries = PerfMonEntries.from(list);
        if (filteredDataEntries.size() == 0) {
            throw new CSVFileNonexistentHostAndMetricException(hostAndMetric);
        }
        return filteredDataEntries;
    }

    @Override
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
