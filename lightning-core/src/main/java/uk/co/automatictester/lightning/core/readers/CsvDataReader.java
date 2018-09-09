package uk.co.automatictester.lightning.core.readers;

import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.automatictester.lightning.core.exceptions.CSVFileIOException;
import uk.co.automatictester.lightning.core.s3client.S3Client;
import uk.co.automatictester.lightning.core.s3client.factory.S3ClientFlyweightFactory;

import java.io.*;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public abstract class CsvDataReader {

    private static final Logger log = LoggerFactory.getLogger(CsvDataReader.class);

    public List<String[]> fromFile(File perfMonCsvFile) {
        Instant start = Instant.now();
        log.debug("Reading CSV file - start");

        List<String[]> entries = new ArrayList<>();
        try (FileReader fr = new FileReader(perfMonCsvFile)) {
            csvParserSettings().setInputBufferSize(20_000_000);
            CsvParser csvParser = new CsvParser(csvParserSettings());
            List<String[]> items = csvParser.parseAll(fr);
            entries.addAll(items);
        } catch (IOException e) {
            throw new CSVFileIOException(e);
        }
        throwExceptionIfEmpty(entries);

        Instant finish = Instant.now();
        Duration duration = Duration.between(start, finish);
        log.debug("Reading CSV file - finish, read {} rows, took {}ms", entries.size(), duration.toMillis());
        return entries;
    }

    public List<String[]> fromS3Object(String region, String bucket, String key) {
        Instant start = Instant.now();
        log.debug("Reading CSV file - start");

        List<String[]> entries = new ArrayList<>();
        S3Client s3Client = S3ClientFlyweightFactory.getInstance(region).setBucket(bucket);
        String csvObjectContent = s3Client.getObjectAsString(key);
        try (InputStreamReader isr = new InputStreamReader(new ByteArrayInputStream(csvObjectContent.getBytes()))) {
            CsvParser csvParser = new CsvParser(csvParserSettings());
            List<String[]> items = csvParser.parseAll(isr);
            entries.addAll(items);
        } catch (IOException e) {
            throw new CSVFileIOException(e);
        }
        throwExceptionIfEmpty(entries);

        Instant finish = Instant.now();
        Duration duration = Duration.between(start, finish);
        log.debug("Reading CSV file - finish, read {} rows, took {}ms", entries.size(), duration.toMillis());
        return entries;
    }

    protected abstract CsvParserSettings csvParserSettings();

    private void throwExceptionIfEmpty(List<String[]> entries) {
        if (entries.isEmpty()) {
            throw new IllegalStateException("No entries found in CSV file");
        }
    }
}
