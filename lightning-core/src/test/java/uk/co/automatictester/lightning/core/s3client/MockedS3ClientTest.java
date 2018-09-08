package uk.co.automatictester.lightning.core.s3client;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import io.findify.s3mock.S3Mock;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringWhiteSpace;
import static org.hamcrest.core.Is.is;

public class MockedS3ClientTest {

    private static final String S3_MOCK_URL = "http://localhost:8001";
    private static final String REGION = "eu-west-2";
    private AmazonS3 amazonS3Client;
    private S3Mock s3Mock;
    private S3Client lightning3Client = MockedS3Client.createInstance(REGION);
    private String key;
    private String content;
    private String bucket;

    @BeforeClass
    public void setupEnv() {
        int port = 8001;

        AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(S3_MOCK_URL, REGION);
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
        key = getRandomKey();
        content = getRandomContent();
        bucket = getRandomBucketName();
        amazonS3Client.createBucket(bucket);
        lightning3Client.setBucket(bucket);
    }

    @AfterMethod
    public void deleteBucket() {
        amazonS3Client.deleteBucket(bucket);
    }

    @Test
    public void testGetObjectAsString() {
        amazonS3Client.putObject(bucket, key, content);
        String retrievedContent = lightning3Client.getObjectAsString(key);
        assertThat(retrievedContent, is(equalTo(content)));
    }

    @Test
    public void testPutObject() {
        String generatedKey = lightning3Client.putObject(key, content);
        String retrievedContent = amazonS3Client.getObjectAsString(bucket, generatedKey);
        assertThat(retrievedContent, is(equalTo(content)));
    }

    @Test
    public void testPutObjectFromFile() throws IOException {
        String file = "src/test/resources/csv/jmeter/10_transactions.csv";
        lightning3Client.putObjectFromFile(file);
        String objectContent = amazonS3Client.getObjectAsString(bucket, file);
        String fileContent = readResourceFileToString(file);
        assertThat(objectContent, is(equalToIgnoringWhiteSpace(fileContent)));
    }

    @Test
    public void testCreateBucketIfDoesNotExist() {
        String bucket = getRandomBucketName();
        boolean bucketCreated = lightning3Client.createBucketIfDoesNotExist(bucket);
        assertThat(bucketCreated, is(true));
    }

    @Test
    public void testDoNotCreateBucketIfExists() {
        String bucket = getRandomBucketName();
        lightning3Client.createBucketIfDoesNotExist(bucket);
        boolean bucketCreatedIfAlreadyExists = lightning3Client.createBucketIfDoesNotExist(bucket);
        assertThat(bucketCreatedIfAlreadyExists, is(false));
        assertThat(amazonS3Client.doesBucketExistV2(bucket), is(true));
    }

    @Test
    public void testToString() {
        String toString = String.format("URL: %s, bucket: %s", S3_MOCK_URL, bucket);
        assertThat(lightning3Client.toString(), is(equalTo(toString)));
    }

    private String getRandomKey() {
        return RandomStringUtils.randomAlphabetic(20);
    }

    private String getRandomContent() {
        return RandomStringUtils.randomAlphanumeric(100);
    }

    private String getRandomBucketName() {
        return RandomStringUtils.randomAlphabetic(50).toLowerCase();
    }

    private String readResourceFileToString(String file) throws IOException {
        Path path = Paths.get(file);
        return Files.lines(path).collect(Collectors.joining("\n"));
    }
}