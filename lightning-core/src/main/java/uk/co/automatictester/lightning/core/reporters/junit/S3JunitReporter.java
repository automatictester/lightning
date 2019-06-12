package uk.co.automatictester.lightning.core.reporters.junit;

import uk.co.automatictester.lightning.core.s3client.S3Client;
import uk.co.automatictester.lightning.core.s3client.factory.S3ClientFlyweightFactory;
import uk.co.automatictester.lightning.core.state.tests.TestSet;

public class S3JunitReporter {

    private static final String KEY = "output/junit.xml";
    private static S3Client s3Client;

    private S3JunitReporter() {
    }

    public static String generateReport(String region, String bucket, TestSet testSet) {
        s3Client = S3ClientFlyweightFactory.getInstance(region).setBucket(bucket);
        JunitReportGenerator reportGenerator = new JunitReportGenerator(testSet);
        String report = reportGenerator.generate();
        return storeReportToS3(report);
    }

    private static String storeReportToS3(String report) {
        return s3Client.putObject(KEY, report);
    }
}
