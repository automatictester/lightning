package uk.co.automatictester.lightning.core.state.data.base;

import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import uk.co.automatictester.lightning.core.exceptions.CSVFileIOException;
import uk.co.automatictester.lightning.core.exceptions.CSVFileNoTransactionsException;
import uk.co.automatictester.lightning.core.s3client.base.S3Client;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public abstract class AbstractCsvEntries {

    protected List<String[]> entries = new ArrayList<>();
    protected static S3Client s3Client;

    protected AbstractCsvEntries() {
    }

    protected AbstractCsvEntries(List<String[]> entries) {
        this.entries.addAll(entries);
    }

    public List<String[]> getEntries() {
        return entries;
    }

    public Stream<String[]> stream() {
        return entries.stream();
    }

    public int size() {
        return entries.size();
    }

    protected void loadFromS3Object(String csvObject) {
        String csvObjectContent = s3Client.getObjectAsString(csvObject);
        try (InputStreamReader isr = new InputStreamReader(new ByteArrayInputStream(csvObjectContent.getBytes()))) {
            CsvParserSettings csvParserSettings = getCsvParserSettings();
            CsvParser csvParser = new CsvParser(csvParserSettings);
            List<String[]> items = csvParser.parseAll(isr);
            entries.addAll(items);
        } catch (IOException e) {
            throw new CSVFileIOException(e);
        }
    }

    protected void loadFromFile(File perfMonCsvFile) {
        try (FileReader fr = new FileReader(perfMonCsvFile)) {
            CsvParserSettings csvParserSettings = getCsvParserSettings();
            csvParserSettings.setInputBufferSize(20_000_000);
            CsvParser csvParser = new CsvParser(csvParserSettings);
            List<String[]> items = csvParser.parseAll(fr);
            entries.addAll(items);
        } catch (IOException e) {
            throw new CSVFileIOException(e);
        }
    }

    protected void throwExceptionIfEmpty() {
        if (entries.isEmpty()) {
            throw new CSVFileNoTransactionsException();
        }
    }

    protected abstract CsvParserSettings getCsvParserSettings();
}
