package uk.co.automatictester.lightning.core.s3;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class S3Client {

    private static final Logger log = LoggerFactory.getLogger(S3Client.class);
    private static AmazonS3 client;
    private static S3Client instance;
    private static String s3Bucket;
    private static String awsRegion;

    private S3Client() {
    }

    public static synchronized S3Client getInstance(String region, boolean... enforceMockedClient) {
        if (instance == null) {
            awsRegion = region;
            instance = new S3Client();
            if (isEnforcingMockClient(enforceMockedClient)) {
                setMockedClient(region);
            } else if (System.getProperty("mockS3") == null) {
                setRealClient(region);
            } else {
                setMockedClient(region);
            }
        } else {
            if (!region.equals(awsRegion)) {
                String message = String.format("Always call getInstance method with same region. Previous region: %s, current region: %s", region, awsRegion);
                throw new RuntimeException(message);
            }
        }
        return instance;
    }

    public S3Client setS3Bucket(String bucket) {
        s3Bucket = bucket;
        return instance;
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
        client.putObject(s3Bucket, file, getFileContent(file));
    }

    public boolean createBucketIfDoesNotExist(String bucket) {
        if (!client.doesBucketExistV2(bucket)) {
            client.createBucket(bucket);
            return true;
        }
        return false;
    }

    private static boolean isEnforcingMockClient(boolean... enforceMockedClient) {
        return enforceMockedClient != null && enforceMockedClient.length == 1 && enforceMockedClient[0];
    }

    private static void setRealClient(String region) {
        AmazonS3ClientBuilder amazonS3ClientBuilder = AmazonS3ClientBuilder.standard().withRegion(region);
        client = amazonS3ClientBuilder.build();
    }

    private static void setMockedClient(String region) {
        AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration("http://localhost:8001", region);
        client = AmazonS3ClientBuilder
                .standard()
                .withPathStyleAccessEnabled(true)
                .withEndpointConfiguration(endpointConfiguration)
                .build();
    }

    private static String getRandomString() {
        int stringLength = 8;
        return RandomStringUtils.randomAlphanumeric(stringLength).toUpperCase();
    }

    private String getFileContent(String resourceFilePath) throws IOException {
        File resourceFile = new File(this.getClass().getResource("/" + resourceFilePath).getFile());
        return FileUtils.readFileToString(resourceFile);
    }
}
