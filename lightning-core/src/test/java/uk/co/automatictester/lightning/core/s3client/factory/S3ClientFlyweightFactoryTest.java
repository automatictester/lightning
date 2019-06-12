package uk.co.automatictester.lightning.core.s3client.factory;

import org.testng.annotations.Test;
import uk.co.automatictester.lightning.core.s3client.S3Client;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class S3ClientFlyweightFactoryTest {

    @Test
    public void testGetSameMaybeRealS3ClientInstances() {
        S3Client c1 = S3ClientFlyweightFactory.getInstance("eu-west-2");
        S3Client c2 = S3ClientFlyweightFactory.getInstance("eu-west-2");
        assertThat(c1 == c2, is(true));
    }

    @Test
    public void testGetDifferentRegionMockedS3ClientInstances() {
        S3Client c1 = S3ClientFlyweightFactory.getInstance("eu-west-1");
        S3Client c2 = S3ClientFlyweightFactory.getInstance("eu-west-2");
        assertThat(c1 == c2, is(false));
    }
}
