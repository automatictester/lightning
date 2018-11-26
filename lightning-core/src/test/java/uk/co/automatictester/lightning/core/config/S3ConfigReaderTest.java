package uk.co.automatictester.lightning.core.config;

import io.findify.s3mock.S3Mock;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import uk.co.automatictester.lightning.core.exceptions.XMLFileException;
import uk.co.automatictester.lightning.core.s3client.S3Client;
import uk.co.automatictester.lightning.core.s3client.factory.S3ClientFlyweightFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

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
        s3Mock.stop();
    }

    @Test(expectedExceptions = XMLFileException.class)
    public void verifyGetTestsMethodThrowsXMLFileLoadingException() throws IOException {
        // suppress error output - coming NOT from own code
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(outContent));
        client.putObjectFromFile("src/test/resources/xml/not_well_formed.xml");

        new S3ConfigReader(REGION, BUCKET).readTests("src/test/resources/xml/not_well_formed.xml");
        System.setErr(null);
    }
}