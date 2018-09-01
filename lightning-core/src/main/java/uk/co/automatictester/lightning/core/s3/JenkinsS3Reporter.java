package uk.co.automatictester.lightning.core.s3;

import uk.co.automatictester.lightning.core.ci.CiReporter;
import uk.co.automatictester.lightning.core.data.JmeterTransactions;
import uk.co.automatictester.lightning.core.s3.client.S3Client;
import uk.co.automatictester.lightning.core.s3.client.S3ClientFlyweightFactory;
import uk.co.automatictester.lightning.core.state.TestSet;

public class JenkinsS3Reporter extends CiReporter {

    private static S3Client s3Client;

    private JenkinsS3Reporter(String region, String bucket, TestSet testSet) {
        super(testSet);
        s3Client = S3ClientFlyweightFactory.getInstance(region).setBucket(bucket);
    }

    private JenkinsS3Reporter(String region, String bucket, JmeterTransactions jmeterTransactions) {
        super(jmeterTransactions);
        s3Client = S3ClientFlyweightFactory.getInstance(region).setBucket(bucket);
    }

    public static JenkinsS3Reporter fromTestSet(String region, String bucket, TestSet testSet) {
        return new JenkinsS3Reporter(region, bucket, testSet);
    }

    public static JenkinsS3Reporter fromJmeterTransactions(String region, String bucket, JmeterTransactions jmeterTransactions) {
        return new JenkinsS3Reporter(region, bucket, jmeterTransactions);
    }

    public String storeJenkinsBuildNameInS3() {
        String jenkinsReportContent;
        if (testSet != null) {
            jenkinsReportContent = getVerifySummary();
        } else {
            jenkinsReportContent = getReportSummary();
        }
        return storeJenkinsReportToS3(jenkinsReportContent);
    }

    private String getVerifySummary() {
        int executed = testSet.getTestCount();
        int failed = testSet.getFailCount() + testSet.getErrorCount();
        return String.format("Tests executed: %s, failed: %s", executed, failed);
    }

    private String storeJenkinsReportToS3(String summary) {
        String escapedSummary = summary.replace(":", "\\:");
        String jenkinsReportTemplate = "#In Jenkins Build Name Setter Plugin, define build name as: ${BUILD_NUMBER} - ${PROPFILE,file=\"lightning-jenkins.properties\",property=\"result.string\"}\nresult.string=%s\n";
        String jenkinsReport = String.format(jenkinsReportTemplate, escapedSummary);
        return s3Client.putObject("output/lightning-jenkins.properties", jenkinsReport);
    }
}
