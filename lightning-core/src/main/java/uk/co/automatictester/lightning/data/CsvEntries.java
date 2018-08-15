package uk.co.automatictester.lightning.data;

import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import uk.co.automatictester.lightning.exceptions.CSVFileIOException;
import uk.co.automatictester.lightning.exceptions.CSVFileNoTransactionsException;
import uk.co.automatictester.lightning.s3.S3Client;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public abstract class CsvEntries {

    protected static S3Client s3Client;

    protected List<String[]> entries = new ArrayList<>();

    protected CsvEntries() {
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

    protected CsvEntries(List<String[]> entries) {
        this.entries.addAll(entries);
    }

    protected abstract CsvParserSettings getCsvParserSettings();

    protected void loadFromFile(File perfMonCsvFile) {
        try (FileReader fr = new FileReader(perfMonCsvFile)) {
            CsvParserSettings csvParserSettings = getCsvParserSettings();
            CsvParser csvParser = new CsvParser(csvParserSettings);
            List<String[]> items = csvParser.parseAll(fr);
            entries.addAll(items);
        } catch (IOException e) {
            throw new CSVFileIOException(e);
        }
    }

    protected void loadFromS3Object(String csvObject) {
        String csvObjectContent = s3Client.getS3ObjectContent(csvObject);
        try (InputStreamReader isr = new InputStreamReader(new ByteArrayInputStream(csvObjectContent.getBytes()))) {
            CsvParserSettings csvParserSettings = getCsvParserSettings();
            CsvParser csvParser = new CsvParser(csvParserSettings);
            List<String[]> items = csvParser.parseAll(isr);
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
}
