package uk.co.automatictester.lightning.core.state.data;

import com.univocity.parsers.common.processor.ConcurrentRowProcessor;
import com.univocity.parsers.common.processor.RowListProcessor;
import com.univocity.parsers.csv.CsvParserSettings;
import uk.co.automatictester.lightning.core.exceptions.CSVFileNonexistentHostAndMetricException;

import java.io.File;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;
import static uk.co.automatictester.lightning.core.enums.PerfMonColumns.*;

public class PerfMonEntries {

    private CsvEntries entries = new CsvEntries();

    private PerfMonEntries(File perfMonCsvFile) {
        entries.loadFromFile(perfMonCsvFile, csvParserSettings());
    }

    private PerfMonEntries(List<String[]> perfMonEntries) {
        entries = new CsvEntries(perfMonEntries);
    }

    private PerfMonEntries(String region, String bucket, String key) {
        entries.loadFromS3Object(region, bucket, key, csvParserSettings());
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

    public int size() {
        return entries.size();
    }

    public Stream<String[]> asStream() {
        return entries.asStream();
    }

    public List<String[]> asList() {
        return entries.asList();
    }

    public PerfMonEntries entriesWith(String hostAndMetric) {
        List<String[]> list = entries.asStream()
                .filter(e -> e[HOST_AND_METRIC.getColumn()].equals(hostAndMetric))
                .collect(collectingAndThen(toList(), filteredList -> returnListOrThrowExceptionIfEmpty(filteredList, hostAndMetric)));
        return PerfMonEntries.fromList(list);
    }

    private CsvParserSettings csvParserSettings() {
        CsvParserSettings parserSettings = new CsvParserSettings();
        parserSettings.setLineSeparatorDetectionEnabled(true);
        parserSettings.setHeaderExtractionEnabled(false);
        parserSettings.selectIndexes(TIMESTAMP.getColumn(), VALUE.getColumn(), HOST_AND_METRIC.getColumn());
        RowListProcessor rowProcessor = new RowListProcessor();
        ConcurrentRowProcessor concurrentRowProcessor = new ConcurrentRowProcessor(rowProcessor);
        parserSettings.setProcessor(concurrentRowProcessor);
        return parserSettings;
    }

    private List<String[]> returnListOrThrowExceptionIfEmpty(List<String[]> list, String hostAndMetric) {
        if (list.size() == 0) {
            throw new CSVFileNonexistentHostAndMetricException(hostAndMetric);
        }
        return list;
    }
}
