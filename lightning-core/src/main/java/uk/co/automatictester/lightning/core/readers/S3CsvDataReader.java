package uk.co.automatictester.lightning.core.readers;

import com.univocity.parsers.csv.CsvParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.automatictester.lightning.core.exceptions.CSVFileIOException;
import uk.co.automatictester.lightning.core.s3client.S3Client;
import uk.co.automatictester.lightning.core.s3client.factory.S3ClientFlyweightFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class S3CsvDataReader {

    private static final Logger log = LoggerFactory.getLogger(S3CsvDataReader.class);
    private final CsvDataReader csvDataReader;

    public S3CsvDataReader(CsvDataReader csvDataReader) {
        this.csvDataReader = csvDataReader;
    }

    public List<String[]> fromS3Object(String region, String bucket, String key) {
        Instant start = Instant.now();
        log.debug("Reading CSV file - start");

        List<String[]> entries = new ArrayList<>();
        S3Client s3Client = S3ClientFlyweightFactory.getInstance(region).setBucket(bucket);
        String csvObjectContent = s3Client.getObjectAsString(key);
        CsvParser csvParser = new CsvParser(csvDataReader.csvParserSettings());
        try (InputStreamReader isr = new InputStreamReader(new ByteArrayInputStream(csvObjectContent.getBytes()))) {
            entries.addAll(csvParser.parseAll(isr));
        } catch (IOException e) {
            throw new CSVFileIOException(e);
        }
        csvDataReader.throwExceptionIfEmpty(entries);

        Instant finish = Instant.now();
        Duration duration = Duration.between(start, finish);
        log.debug("Reading CSV file - finish, read {} rows, took {}ms", entries.size(), duration.toMillis());
        return entries;
    }
}
