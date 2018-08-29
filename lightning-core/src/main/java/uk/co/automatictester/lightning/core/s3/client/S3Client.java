package uk.co.automatictester.lightning.core.s3.client;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class S3Client {

    private Logger log = LoggerFactory.getLogger(this.getClass());
    private AmazonS3 client;
    private String s3Bucket;

    private S3Client(AmazonS3 client) {
        this.client = client;
    }

    static S3Client getInstance(String region) {
        AmazonS3ClientBuilder amazonS3ClientBuilder = AmazonS3ClientBuilder.standard().withRegion(region);
        AmazonS3 client = amazonS3ClientBuilder.build();
        return new S3Client(client);
    }

    static S3Client getMockedInstance(String region) {
        AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration("http://localhost:8001", region);
        AmazonS3 client = AmazonS3ClientBuilder
                .standard()
                .withPathStyleAccessEnabled(true)
                .withEndpointConfiguration(endpointConfiguration)
                .build();
        return new S3Client(client);
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
