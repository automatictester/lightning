package uk.co.automatictester.lightning.core.s3;

import io.findify.s3mock.S3Mock;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public abstract class AmazonS3Test {

    protected S3Mock s3Mock;

    @BeforeClass(alwaysRun = true)
    public void maybeSetupS3mock() {
        if (System.getProperty("mockS3") != null) {
            s3Mock = new S3Mock.Builder().withPort(getMockedPort()).withInMemoryBackend().build();
            s3Mock.start();
        }
    }

    @AfterClass(alwaysRun = true)
    public void maybeCleanupS3Mock() {
        if (System.getProperty("mockS3") != null) {
            s3Mock.stop();
        }
    }

    private int getMockedPort() {
        return Integer.parseInt(System.getProperty("mockS3Port", "8001"));
    }
}
