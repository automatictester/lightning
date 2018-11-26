package uk.co.automatictester.lightning.core.reporters.junit;

import io.findify.s3mock.S3Mock;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import uk.co.automatictester.lightning.core.s3client.S3Client;
import uk.co.automatictester.lightning.core.s3client.factory.S3ClientFlyweightFactory;
import uk.co.automatictester.lightning.core.state.tests.TestSet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

public class S3JunitReporterTest {

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
    public void testGenerateReport() {
        String key = S3JunitReporter.generateReport(REGION, BUCKET, new TestSet());
        String reportContent = client.getObjectAsString(key);
        String expectedReportContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><testsuite errors=\"0\" failures=\"0\" name=\"Lightning\" tests=\"0\" time=\"0\"/>";
        assertThat(reportContent, containsString(expectedReportContent));
    }
}
