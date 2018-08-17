package uk.co.automatictester.lightning.core.facade;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class TestS3Client {

    private static final Logger log = LoggerFactory.getLogger(TestS3Client.class);
    private static AmazonS3 amazonS3;
    private String bucket;

    public TestS3Client(String region, String bucket) {
        AmazonS3ClientBuilder amazonS3ClientBuilder = AmazonS3ClientBuilder.standard().withRegion(region);
        amazonS3 = amazonS3ClientBuilder.build();
        this.bucket = bucket;
    }

    public String getS3ObjectContent(String key) {
        log.info("Getting test S3 object: {}/{}", bucket, key);
        return amazonS3.getObjectAsString(bucket, key);
    }

    public void putS3Object(String file) throws IOException {
        log.info("Putting test S3 object: {}/{}", bucket, file);
        amazonS3.putObject(bucket, file, getFileContent(file));
    }

    private String getFileContent(String resourceFilePath) throws IOException {
        File resourceFile = new File(this.getClass().getResource("/" + resourceFilePath).getFile());
        return FileUtils.readFileToString(resourceFile);
    }
}
