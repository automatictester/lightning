package uk.co.automatictester.lightning.core.reporters.junit;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import uk.co.automatictester.lightning.core.s3.AmazonS3Test;
import uk.co.automatictester.lightning.core.s3client.S3Client;
import uk.co.automatictester.lightning.core.s3client.factory.S3ClientFlyweightFactory;
import uk.co.automatictester.lightning.core.state.tests.TestSet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

public class S3JunitReporterTest extends AmazonS3Test {

    private static final String REGION = "eu-west-2";
    private static final String BUCKET = "automatictester.co.uk-lightning-aws-lambda";
    private S3Client client;

    @BeforeClass
    public void setupEnv() {
        client = S3ClientFlyweightFactory.getInstance(REGION).setBucket(BUCKET);
        client.createBucketIfDoesNotExist(BUCKET);
    }

    @Test
    public void testGenerateReport() {
        String key = S3JunitReporter.generateReport(REGION, BUCKET, new TestSet());
        String reportContent = client.getObjectAsString(key);
        String expectedReportContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><testsuite errors=\"0\" failures=\"0\" name=\"Lightning\" tests=\"0\" time=\"0\"/>";
        assertThat(reportContent, containsString(expectedReportContent));
    }
}
