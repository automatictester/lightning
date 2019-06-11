package uk.co.automatictester.lightning.lambda.reader;

import com.univocity.parsers.csv.CsvParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.automatictester.lightning.core.readers.CsvBean;
import uk.co.automatictester.lightning.core.readers.CsvDataReader;
import uk.co.automatictester.lightning.lambda.s3client.S3Client;
import uk.co.automatictester.lightning.lambda.s3client.factory.S3ClientFlyweightFactory;

import java.io.*;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class S3CsvDataReader {

    private static final Logger log = LoggerFactory.getLogger(S3CsvDataReader.class);
    private final CsvDataReader csvDataReader;

    public S3CsvDataReader(CsvDataReader csvDataReader) {
        this.csvDataReader = csvDataReader;
    }

    public List<? extends CsvBean> fromS3Object(String region, String bucket, String key) {
        Instant start = Instant.now();
        log.debug("Reading CSV file - start");

        CsvParser parser = new CsvParser(csvDataReader.csvParserSettings());
        S3Client s3Client = S3ClientFlyweightFactory.getInstance(region).setBucket(bucket);
        String csvObjectContent = s3Client.getObjectAsString(key);
        parser.parse(getReader(csvObjectContent.getBytes()));
        List<? extends CsvBean> beans = csvDataReader.beanListProcessor().getBeans();

        Instant finish = Instant.now();
        Duration duration = Duration.between(start, finish);
        log.debug("Reading CSV file - finish, read {} rows, took {}ms", beans.size(), duration.toMillis());
        csvDataReader.throwExceptionIfEmpty(beans);
        return beans;
    }

    private static Reader getReader(byte[] csvFile) {
        return new InputStreamReader(new ByteArrayInputStream(csvFile));
    }
}
