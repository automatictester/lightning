package uk.co.automatictester.lightning.core.state.data;

import com.univocity.parsers.common.processor.ConcurrentRowProcessor;
import com.univocity.parsers.common.processor.RowListProcessor;
import com.univocity.parsers.csv.CsvParserSettings;
import uk.co.automatictester.lightning.core.exceptions.CSVFileNonexistentHostAndMetricException;
import uk.co.automatictester.lightning.core.s3client.factory.S3ClientFlyweightFactory;
import uk.co.automatictester.lightning.core.state.data.base.AbstractCsvEntries;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import static uk.co.automatictester.lightning.core.enums.PerfMonColumns.*;

public class PerfMonEntries extends AbstractCsvEntries {

    private PerfMonEntries(File perfMonCsvFile) {
        loadFromFile(perfMonCsvFile);
        throwExceptionIfEmpty();
    }

    private PerfMonEntries(List<String[]> perfMonEntries) {
        super(perfMonEntries);
    }

    private PerfMonEntries(String region, String bucket, String key) {
        s3Client = S3ClientFlyweightFactory.getInstance(region).setBucket(bucket);
        loadFromS3Object(key);
        throwExceptionIfEmpty();
    }

    public static PerfMonEntries fromFile(File perfMonCvsFile) {
        return new PerfMonEntries(perfMonCvsFile);
    }

    public static PerfMonEntries fromList(List<String[]> perfMonEntries) {
        return new PerfMonEntries(perfMonEntries);
    }

    public static PerfMonEntries fromS3Object(String region, String bucket, String key) {
        return new PerfMonEntries(region, bucket, key);
    }

    public PerfMonEntries getEntriesWith(String hostAndMetric) {
        List<String[]> list = entries.stream()
                .filter(e -> e[HOST_AND_METRIC.getColumn()].equals(hostAndMetric))
                .collect(Collectors.toList());
        PerfMonEntries filteredDataEntries = PerfMonEntries.fromList(list);
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
        parserSettings.selectIndexes(TIMESTAMP.getColumn(), VALUE.getColumn(), HOST_AND_METRIC.getColumn());
        RowListProcessor rowProcessor = new RowListProcessor();
        ConcurrentRowProcessor concurrentRowProcessor = new ConcurrentRowProcessor(rowProcessor);
        parserSettings.setProcessor(concurrentRowProcessor);
        return parserSettings;
    }
}
