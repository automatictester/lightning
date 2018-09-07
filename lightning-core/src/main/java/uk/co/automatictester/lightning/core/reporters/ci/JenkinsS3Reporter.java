package uk.co.automatictester.lightning.core.reporters.ci;

import uk.co.automatictester.lightning.core.reporters.ci.base.AbstractCiReporter;
import uk.co.automatictester.lightning.core.state.data.JmeterTransactions;
import uk.co.automatictester.lightning.core.s3client.base.S3Client;
import uk.co.automatictester.lightning.core.s3client.factory.S3ClientFlyweightFactory;
import uk.co.automatictester.lightning.core.state.tests.results.LightningTestSetResult;

public class JenkinsS3Reporter extends AbstractCiReporter {

    private static S3Client s3Client;

    private JenkinsS3Reporter(String region, String bucket, LightningTestSetResult testSet) {
        super(testSet);
        s3Client = S3ClientFlyweightFactory.getInstance(region).setBucket(bucket);
    }

    private JenkinsS3Reporter(String region, String bucket, JmeterTransactions jmeterTransactions) {
        super(jmeterTransactions);
        s3Client = S3ClientFlyweightFactory.getInstance(region).setBucket(bucket);
    }

    public static JenkinsS3Reporter fromTestSet(String region, String bucket, LightningTestSetResult testSet) {
        return new JenkinsS3Reporter(region, bucket, testSet);
    }

    public static JenkinsS3Reporter fromJmeterTransactions(String region, String bucket, JmeterTransactions jmeterTransactions) {
        return new JenkinsS3Reporter(region, bucket, jmeterTransactions);
    }

    public String storeJenkinsBuildNameInS3() {
        String jenkinsReportContent;
        if (testSet != null) {
            jenkinsReportContent = verifySummary();
        } else {
            jenkinsReportContent = reportSummary();
        }
        return storeJenkinsReportToS3(jenkinsReportContent);
    }

    private String verifySummary() {
        int executed = testSet.testCount();
        int failed = testSet.failCount() + testSet.errorCount();
        return String.format("Tests executed: %s, failed: %s", executed, failed);
    }

    private String storeJenkinsReportToS3(String summary) {
        String escapedSummary = summary.replace(":", "\\:");
        String jenkinsReportTemplate = "#In Jenkins Build Name Setter Plugin, define build name as: ${BUILD_NUMBER} - ${PROPFILE,file=\"lightning-jenkins.properties\",property=\"result.string\"}\nresult.string=%s\n";
        String jenkinsReport = String.format(jenkinsReportTemplate, escapedSummary);
        return s3Client.putObject("output/lightning-jenkins.properties", jenkinsReport);
    }
}
