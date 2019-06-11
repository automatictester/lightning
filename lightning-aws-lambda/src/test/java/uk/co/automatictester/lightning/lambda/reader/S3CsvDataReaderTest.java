package uk.co.automatictester.lightning.lambda.reader;

import org.hamcrest.Matchers;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import uk.co.automatictester.lightning.core.readers.CsvDataReader;
import uk.co.automatictester.lightning.core.readers.JmeterBean;
import uk.co.automatictester.lightning.core.readers.JmeterDataReader;
import uk.co.automatictester.lightning.lambda.s3.AmazonS3Test;
import uk.co.automatictester.lightning.lambda.s3client.S3Client;
import uk.co.automatictester.lightning.lambda.s3client.factory.S3ClientFlyweightFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class S3CsvDataReaderTest extends AmazonS3Test {

    private static final String REGION = "eu-west-2";
    private static final String BUCKET = "automatictester.co.uk-lightning-aws-lambda";

    private static final String CSV_2_TRANSACTIONS = new File("src/test/resources/csv/jmeter/2_transactions.csv").toString();
    private static final JmeterBean LOGIN_3514_SUCCESS = new JmeterBean("Login", "3514", "true", "1434291247743");
    private static final JmeterBean SEARCH_11221_SUCCESS = new JmeterBean("Search", "11221", "true", "1434291252072");

    private S3Client client;

    @BeforeClass
    public void setupEnv() {
        client = S3ClientFlyweightFactory.getInstance(REGION).setBucket(BUCKET);
        client.createBucketIfDoesNotExist(BUCKET);
    }

    @Test
    public void testFromS3Object() throws IOException {
        client.putObjectFromFile(CSV_2_TRANSACTIONS);
        CsvDataReader jmeterReader = new JmeterDataReader();
        S3CsvDataReader s3Reader = new S3CsvDataReader(jmeterReader);
        List<JmeterBean> entries = (List<JmeterBean>) s3Reader.fromS3Object(REGION, BUCKET, CSV_2_TRANSACTIONS);
        assertThat(entries, Matchers.hasItem(LOGIN_3514_SUCCESS));
        assertThat(entries, Matchers.hasItem(SEARCH_11221_SUCCESS));
        assertThat(entries.size(), is(2));
    }
}
