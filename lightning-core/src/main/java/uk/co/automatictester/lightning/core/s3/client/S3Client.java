package uk.co.automatictester.lightning.core.s3.client;

import com.amazonaws.services.s3.AmazonS3;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public abstract class S3Client {

    private static final Logger log = LoggerFactory.getLogger(S3Client.class);
    protected AmazonS3 client;
    private String s3Bucket;

    protected S3Client(AmazonS3 client) {
        this.client = client;
    }

    public S3Client setBucket(String bucket) {
        s3Bucket = bucket;
        return this;
    }

    public String getObjectAsString(String key) {
        log.info("Getting S3 object: {}/{}", s3Bucket, key);
        return client.getObjectAsString(s3Bucket, key);
    }

    public String putObject(String key, String content) {
        String s3key = key + "-" + getRandomString();
        log.info("Putting S3 object: {}/{}", s3Bucket, s3key);
        client.putObject(s3Bucket, s3key, content);
        return s3key;
    }

    public void putObjectFromFile(String file) throws IOException {
        log.info("Putting S3 object from file: {}/{}", s3Bucket, file);
        client.putObject(s3Bucket, file, readFileToString(file));
    }

    public boolean createBucketIfDoesNotExist(String bucket) {
        if (!client.doesBucketExistV2(bucket)) {
            client.createBucket(bucket);
            return true;
        }
        return false;
    }

    private static String getRandomString() {
        int stringLength = 8;
        return RandomStringUtils.randomAlphanumeric(stringLength).toUpperCase();
    }

    private String readFileToString(String file) throws IOException {
        Path path = Paths.get(file);
        return Files.lines(path).collect(Collectors.joining("\n"));
    }
}
