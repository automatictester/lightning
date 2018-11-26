package uk.co.automatictester.lightning.core.readers;

import io.findify.s3mock.S3Mock;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import uk.co.automatictester.lightning.core.exceptions.CSVFileIOException;
import uk.co.automatictester.lightning.core.s3client.S3Client;
import uk.co.automatictester.lightning.core.s3client.factory.S3ClientFlyweightFactory;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static uk.co.automatictester.lightning.shared.LegacyTestData.*;

public class S3CsvDataReaderTest {

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
    public void testFromS3Object() throws IOException {
        client.putObjectFromFile(CSV_2_TRANSACTIONS.toString());
        CsvDataReader jmeterReader = new JmeterDataReader();
        S3CsvDataReader s3Reader = new S3CsvDataReader(jmeterReader);
        List<String[]> entries = s3Reader.fromS3Object(REGION, BUCKET, CSV_2_TRANSACTIONS.toString());
        assertThat(entries, hasItem(LOGIN_3514_SUCCESS));
        assertThat(entries, hasItem(SEARCH_11221_SUCCESS));
        assertThat(entries.size(), is(2));
    }
}
