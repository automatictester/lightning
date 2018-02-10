package uk.co.deliverymind.lightning.lambda.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class S3Client {

    private static final Logger log = LogManager.getLogger(S3Client.class);
    private static AmazonS3 amazonS3;
    private String bucket;

    public S3Client(String region, String bucket) {
        amazonS3 = AmazonS3ClientBuilder.standard().withRegion(region).build();
        this.bucket = bucket;
    }

    public String getS3ObjectContent(String key) {
        log.info("Getting S3 object: {}/{}", bucket, key);
        return amazonS3.getObjectAsString(bucket, key);
    }

    public String putS3Object(String key, String content) {
        String s3key = key + "-" + getRandomString();
        log.info("Putting S3 object: {}/{}", bucket, s3key);
        amazonS3.putObject(bucket, s3key, content);
        return s3key;
    }

    private static String getRandomString() {
        return RandomStringUtils.randomAlphanumeric(8).toUpperCase();
    }
}
