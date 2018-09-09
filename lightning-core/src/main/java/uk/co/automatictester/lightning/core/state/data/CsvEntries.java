package uk.co.automatictester.lightning.core.state.data;

import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import uk.co.automatictester.lightning.core.exceptions.CSVFileIOException;
import uk.co.automatictester.lightning.core.s3client.S3Client;
import uk.co.automatictester.lightning.core.s3client.factory.S3ClientFlyweightFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class CsvEntries {

    protected List<String[]> entries = new ArrayList<>();

    public CsvEntries() {
    }

    public CsvEntries(List<String[]> entries) {
        this.entries.addAll(entries);
    }

    public List<String[]> asList() {
        return entries;
    }

    public Stream<String[]> asStream() {
        return entries.stream();
    }

    public int size() {
        return entries.size();
    }

    public void loadFromS3Object(String region, String bucket, String key, CsvParserSettings csvParserSettings) {
        S3Client s3Client = S3ClientFlyweightFactory.getInstance(region).setBucket(bucket);
        String csvObjectContent = s3Client.getObjectAsString(key);
        try (InputStreamReader isr = new InputStreamReader(new ByteArrayInputStream(csvObjectContent.getBytes()))) {
            CsvParser csvParser = new CsvParser(csvParserSettings);
            List<String[]> items = csvParser.parseAll(isr);
            entries.addAll(items);
        } catch (IOException e) {
            throw new CSVFileIOException(e);
        }
        throwExceptionIfEmpty();
    }

    public void loadFromFile(File perfMonCsvFile, CsvParserSettings csvParserSettings) {
        try (FileReader fr = new FileReader(perfMonCsvFile)) {
            csvParserSettings.setInputBufferSize(20_000_000);
            CsvParser csvParser = new CsvParser(csvParserSettings);
            List<String[]> items = csvParser.parseAll(fr);
            entries.addAll(items);
        } catch (IOException e) {
            throw new CSVFileIOException(e);
        }
        throwExceptionIfEmpty();
    }

    private void throwExceptionIfEmpty() {
        if (entries.isEmpty()) {
            throw new IllegalStateException("No entries found in CSV file");
        }
    }
}
