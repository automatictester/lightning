package uk.co.automatictester.lightning.core.reporters.jenkins;

import io.findify.s3mock.S3Mock;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import uk.co.automatictester.lightning.core.s3client.S3Client;
import uk.co.automatictester.lightning.core.s3client.factory.S3ClientFlyweightFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

public class S3JenkinsReporterTest {

    private static final String REGION = "eu-west-2";
    private static final String BUCKET = "automatictester.co.uk-lightning-aws-lambda";
    private S3Mock s3Mock;
    private S3Client client;

    @BeforeClass
    public void setupEnv() {
        if (System.getProperty("mockS3") != null) {
            s3Mock = new S3Mock.Builder().withPort(8001).withInMemoryBackend().build();
            s3Mock.start();
        }
        client = S3ClientFlyweightFactory.getInstance(REGION).setBucket(BUCKET);
        client.createBucketIfDoesNotExist(BUCKET);
    }

    @AfterClass
    public void teardown() {
        if (System.getProperty("mockS3") != null) {
            s3Mock.stop();
        }
    }

    @Test
    public void testStoreJenkinsBuildName() {
        String report = "Transactions executed: 3, failed: 1";
        S3JenkinsReporter jenkinsReporter = new S3JenkinsReporter(REGION, BUCKET);
        String key = jenkinsReporter.storeJenkinsBuildNameToS3(report);

        String fileContent = client.getObjectAsString(key);
        assertThat(fileContent, containsString("In Jenkins Build Name Setter Plugin, define build name as: ${BUILD_NUMBER} - ${PROPFILE,file=\"lightning-jenkins.properties\",property=\"result.string\"}"));
        assertThat(fileContent, containsString("result.string=Transactions executed\\: 3, failed\\: 1"));
    }
}
