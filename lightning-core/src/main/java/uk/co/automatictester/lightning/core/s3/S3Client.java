package uk.co.automatictester.lightning.core.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class S3Client {

    private static final Logger log = LoggerFactory.getLogger(S3Client.class);
    private static AmazonS3 amazonS3;
    private String bucket;

    public S3Client(String region, String bucket) {
        AmazonS3ClientBuilder amazonS3ClientBuilder = AmazonS3ClientBuilder.standard().withRegion(region);
        amazonS3 = amazonS3ClientBuilder.build();
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
        int stringLength = 8;
        return RandomStringUtils.randomAlphanumeric(stringLength).toUpperCase();
    }
}
