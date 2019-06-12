package uk.co.automatictester.lightning.core.s3client;

import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;

public class RealS3ClientTest {

    private static final String REGION = "eu-west-2";
    private static final String BUCKET = "mybucketname";

    @Test
    public void testToString() {
        S3Client realClient = RealS3Client.createInstance(REGION);
        realClient.setBucket(BUCKET);
        String toString = String.format("Region: %s, bucket: %s", REGION, BUCKET);
        assertThat(realClient.toString(), is(equalTo(toString)));
    }
}
