package uk.co.automatictester.lightning.core.s3;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import io.findify.s3mock.S3Mock;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;

public class S3ClientTest {

    private String region = "eu-west-2";
    private AmazonS3 amazonS3Client;
    private S3Mock s3Mock;
    private S3Client lightning3Client;
    private String defaultBucket = RandomStringUtils.randomAlphabetic(50).toLowerCase();
    private String key;
    private String content;

    @BeforeClass
    public void setupEnv() {
        int port = 8001;

        String serviceEndpoint = String.format("http://localhost:%d", port);
        AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(serviceEndpoint, region);
        amazonS3Client = AmazonS3ClientBuilder
                .standard()
                .withPathStyleAccessEnabled(true)
                .withEndpointConfiguration(endpointConfiguration)
                .build();

        s3Mock = new S3Mock.Builder().withPort(port).withInMemoryBackend().build();
        s3Mock.start();
    }

    @AfterClass
    public void teardown() {
        s3Mock.stop();
    }

    @BeforeMethod
    public void initialiseTestConfig() {
        content = RandomStringUtils.randomAlphanumeric(100);
        key = RandomStringUtils.randomAlphabetic(20);
        amazonS3Client.createBucket(defaultBucket);
    }

    @AfterMethod
    public void deleteBucket() {
        amazonS3Client.deleteBucket(defaultBucket);
    }

    @Test
    public void testGetObjectAsString() {
        amazonS3Client.putObject(defaultBucket, key, content);

        lightning3Client = S3Client.getInstance(region, true).setS3Bucket(defaultBucket);
        String retrievedContent = lightning3Client.getObjectAsString(key);

        assertThat(retrievedContent, is(equalTo(content)));
    }

    @Test
    public void testPutObject() {
        lightning3Client = S3Client.getInstance(region, true).setS3Bucket(defaultBucket);
        String generatedKey = lightning3Client.putObject(key, content);

        String retrievedContent = amazonS3Client.getObjectAsString(defaultBucket, generatedKey);

        assertThat(retrievedContent, is(equalTo(content)));
    }

    @Test
    public void testPutObjectFromFile() throws IOException {
        String file = "csv/jmeter/10_transactions.csv";

        lightning3Client = S3Client.getInstance(region, true).setS3Bucket(defaultBucket);
        lightning3Client.putObjectFromFile(file);

        String fileContent = readFileToString("/" + file);
        String objectContent = amazonS3Client.getObjectAsString(defaultBucket, file);

        assertThat(objectContent, is(equalTo(fileContent)));
    }

    @Test
    public void testCreateBucketIfDoesNotExist() {
        amazonS3Client.deleteBucket(defaultBucket);

        String bucket = RandomStringUtils.randomAlphabetic(50).toLowerCase();
        lightning3Client = S3Client.getInstance(region, true).setS3Bucket(defaultBucket);
        boolean bucketCreated = lightning3Client.createBucketIfDoesNotExist(bucket);
        assertThat(bucketCreated, is(true));

        amazonS3Client.createBucket(defaultBucket);
    }

    @Test
    public void testDoNotCreateBucketIfExists() {
        assertThat(amazonS3Client.doesBucketExistV2(defaultBucket), is(true));

        lightning3Client = S3Client.getInstance(region, true).setS3Bucket(defaultBucket);
        boolean bucketCreated = lightning3Client.createBucketIfDoesNotExist(defaultBucket);

        assertThat(bucketCreated, is(false));
        assertThat(amazonS3Client.doesBucketExistV2(defaultBucket), is(true));
    }

    @Test(expectedExceptions = RuntimeException.class, expectedExceptionsMessageRegExp = ".*Always call getInstance method with same region.*")
    public void testGetInstanceWithDifferentParam() {
        S3Client.getInstance(region);
        S3Client.getInstance("wrong");
    }

    private String readFileToString(String filePath) throws IOException {
        File file = new File(this.getClass().getResource(filePath).getFile());
        return FileUtils.readFileToString(file);
    }
}