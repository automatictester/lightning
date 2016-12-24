package uk.co.deliverymind.lightning.readers;

import com.univocity.parsers.common.processor.ConcurrentRowProcessor;
import com.univocity.parsers.common.processor.RowListProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import uk.co.deliverymind.lightning.data.PerfMonDataEntries;
import uk.co.deliverymind.lightning.exceptions.CSVFileIOException;
import uk.co.deliverymind.lightning.exceptions.CSVFileNoTransactionsException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class PerfMonDataReader {

    private static final int TIMESTAMP = 0;
    private static final int VALUE = 1;
    private static final int HOST_AND_METRIC = 2;

    public PerfMonDataEntries getDataEntires(File csvFile) {
        PerfMonDataEntries perfMonDataEntries = new PerfMonDataEntries();
        try (FileReader fr = new FileReader(csvFile)) {
            perfMonDataEntries.addAll(getParser().parseAll(fr));
        } catch (IOException e) {
            throw new CSVFileIOException(e);
        }
        if (perfMonDataEntries.isEmpty()) {
            throw new CSVFileNoTransactionsException();
        }
        return perfMonDataEntries;
    }

    private CsvParser getParser() {
        CsvParserSettings parserSettings = new CsvParserSettings();
        parserSettings.setLineSeparatorDetectionEnabled(true);
        parserSettings.setHeaderExtractionEnabled(false);
        parserSettings.selectIndexes(TIMESTAMP, VALUE, HOST_AND_METRIC);
        RowListProcessor rowProcessor = new RowListProcessor();
        parserSettings.setProcessor(new ConcurrentRowProcessor(rowProcessor));
        return new CsvParser(parserSettings);
    }
}
