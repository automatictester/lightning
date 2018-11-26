package uk.co.automatictester.lightning.core.config;

import io.findify.s3mock.S3Mock;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import uk.co.automatictester.lightning.core.exceptions.XMLFileException;
import uk.co.automatictester.lightning.core.s3client.S3Client;
import uk.co.automatictester.lightning.core.s3client.factory.S3ClientFlyweightFactory;
import uk.co.automatictester.lightning.core.state.tests.TestSet;
import uk.co.automatictester.lightning.core.tests.LightningTest;
import uk.co.automatictester.lightning.core.tests.RespTimeAvgTest;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

public class S3ConfigReaderTest {

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
    public void verifyGetTestsMethodAvgRespTime() throws IOException {
        client.putObjectFromFile("src/test/resources/xml/avgRespTimeTest.xml");
        TestSet testSet = new S3ConfigReader(REGION, BUCKET).readTests("src/test/resources/xml/avgRespTimeTest.xml");
        List<LightningTest> tests = testSet.get();
        RespTimeAvgTest test = new RespTimeAvgTest.Builder("Test #1", 4000).withDescription("Verify average login times").withTransactionName("Login").build();

        assertThat(tests, hasSize(1));
        assertThat(tests.contains(test), is(true));
    }

    @Test(expectedExceptions = XMLFileException.class)
    public void verifyGetTestsMethodThrowsXMLFileLoadingException() throws IOException {
        client.putObjectFromFile("src/test/resources/xml/not_well_formed.xml");
        new S3ConfigReader(REGION, BUCKET).readTests("src/test/resources/xml/not_well_formed.xml");
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void verifyGetTestsMethodThrowsXMLFileNoTestsException() throws IOException {
        client.putObjectFromFile("src/test/resources/xml/0_0_0.xml");
        new S3ConfigReader(REGION, BUCKET).readTests("src/test/resources/xml/0_0_0.xml");
    }
}