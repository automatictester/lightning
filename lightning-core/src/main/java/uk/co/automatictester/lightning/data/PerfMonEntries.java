package uk.co.automatictester.lightning.data;

import com.univocity.parsers.common.processor.ConcurrentRowProcessor;
import com.univocity.parsers.common.processor.RowListProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import uk.co.automatictester.lightning.exceptions.CSVFileIOException;
import uk.co.automatictester.lightning.exceptions.CSVFileNoTransactionsException;
import uk.co.automatictester.lightning.exceptions.CSVFileNonexistentHostAndMetricException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PerfMonEntries extends ArrayList<String[]> {

    private static final int TIMESTAMP_INDEX = 0;
    private static final int VALUE_INDEX = 1;
    private static final int HOST_AND_METRIC_INDEX = 2;

    private PerfMonEntries() {
    }

    private PerfMonEntries(File perfMonCsvFile) {
        loadFromFile(perfMonCsvFile);
        throwExceptionIfEmpty();
    }

    private PerfMonEntries(List<String[]> perfMonEntries) {
        this.addAll(perfMonEntries);
    }

    public static PerfMonEntries fromFile(File perfMonCvsFile) {
        return new PerfMonEntries(perfMonCvsFile);
    }

    public static PerfMonEntries fromList(List<String[]> perfMonEntries) {
        return new PerfMonEntries(perfMonEntries);
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
}
