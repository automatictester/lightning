package uk.co.automatictester.lightning.core.readers;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import uk.co.automatictester.lightning.core.s3.AmazonS3Test;
import uk.co.automatictester.lightning.core.s3client.S3Client;
import uk.co.automatictester.lightning.core.s3client.factory.S3ClientFlyweightFactory;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static uk.co.automatictester.lightning.shared.LegacyTestData.*;

public class S3CsvDataReaderTest extends AmazonS3Test {

    private static final String REGION = "eu-west-2";
    private static final String BUCKET = "automatictester.co.uk-lightning-aws-lambda";
    private S3Client client;

    @BeforeClass
    public void setupEnv() {
        client = S3ClientFlyweightFactory.getInstance(REGION).setBucket(BUCKET);
        client.createBucketIfDoesNotExist(BUCKET);
    }

    @Test
    public void testFromS3Object() throws IOException {
        client.putObjectFromFile(CSV_2_TRANSACTIONS.toString());
        CsvDataReader jmeterReader = new JmeterDataReader();
        S3CsvDataReader s3Reader = new S3CsvDataReader(jmeterReader);
        List<JmeterBean> entries = (List<JmeterBean>) s3Reader.fromS3Object(REGION, BUCKET, CSV_2_TRANSACTIONS.toString());
        assertThat(entries, hasItem(LOGIN_3514_SUCCESS));
        assertThat(entries, hasItem(SEARCH_11221_SUCCESS));
        assertThat(entries.size(), is(2));
    }
}
