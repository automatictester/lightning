package uk.co.automatictester.lightning.core.s3;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class S3Client {

    private static final Logger log = LoggerFactory.getLogger(S3Client.class);
    private static AmazonS3 client;
    private String bucket;

    public S3Client(String region, String bucket) {
        if (System.getProperty("mockS3") == null) {
            AmazonS3ClientBuilder amazonS3ClientBuilder = AmazonS3ClientBuilder.standard().withRegion(region);
            client = amazonS3ClientBuilder.build();
        } else {
            AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration("http://localhost:8001", region);
            client = AmazonS3ClientBuilder
                    .standard()
                    .withPathStyleAccessEnabled(true)
                    .withEndpointConfiguration(endpointConfiguration)
                    .build();
        }
        this.bucket = bucket;
    }

    public String getS3ObjectContent(String key) {
        log.info("Getting S3 object: {}/{}", bucket, key);
        return client.getObjectAsString(bucket, key);
    }

    public String putS3Object(String key, String content) {
        String s3key = key + "-" + getRandomString();
        log.info("Putting S3 object: {}/{}", bucket, s3key);
        client.putObject(bucket, s3key, content);
        return s3key;
    }

    private static String getRandomString() {
        int stringLength = 8;
        return RandomStringUtils.randomAlphanumeric(stringLength).toUpperCase();
    }
}
