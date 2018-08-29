package uk.co.automatictester.lightning.core.data;

import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import uk.co.automatictester.lightning.core.exceptions.CSVFileIOException;
import uk.co.automatictester.lightning.core.exceptions.CSVFileNoTransactionsException;
import uk.co.automatictester.lightning.core.s3.client.S3Client;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public abstract class CsvEntries {

    protected List<String[]> entries = new ArrayList<>();
    static S3Client s3Client;

    CsvEntries() {
    }

    CsvEntries(List<String[]> entries) {
        this.entries.addAll(entries);
    }

    public void add(String[] entry) {
        entries.add(entry);
    }

    public List<String[]> getEntries() {
        return entries;
    }

    public int size() {
        return entries.size();
    }

    void loadFromS3Object(String csvObject) {
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

    void throwExceptionIfEmpty() {
        if (entries.isEmpty()) {
            throw new CSVFileNoTransactionsException();
        }
    }

    void loadFromFile(File perfMonCsvFile) {
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

    protected abstract CsvParserSettings getCsvParserSettings();
}
