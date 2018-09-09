package uk.co.automatictester.lightning.core.reporters.ci;

import uk.co.automatictester.lightning.core.s3client.S3Client;
import uk.co.automatictester.lightning.core.s3client.factory.S3ClientFlyweightFactory;
import uk.co.automatictester.lightning.core.state.data.JmeterTransactions;
import uk.co.automatictester.lightning.core.state.tests.TestSet;

public class JenkinsS3Reporter {

    private static S3Client s3Client;
    private TestSet testSet;
    private JmeterTransactions jmeterTransactions;

    private JenkinsS3Reporter(String region, String bucket, TestSet testSet) {
        this.testSet = testSet;
        s3Client = S3ClientFlyweightFactory.getInstance(region).setBucket(bucket);
    }

    private JenkinsS3Reporter(String region, String bucket, JmeterTransactions jmeterTransactions) {
        this.jmeterTransactions = jmeterTransactions;
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
            jenkinsReportContent = testSet.jenkinsSummaryReport();
        } else {
            jenkinsReportContent = jmeterTransactions.summaryReport();
        }
        return storeJenkinsReportToS3(jenkinsReportContent);
    }

    private String storeJenkinsReportToS3(String summary) {
        String escapedSummary = summary.replace(":", "\\:");
        String jenkinsReportTemplate = "#In Jenkins Build Name Setter Plugin, define build name as: ${BUILD_NUMBER} - ${PROPFILE,file=\"lightning-jenkins.properties\",property=\"result.string\"}\nresult.string=%s\n";
        String jenkinsReport = String.format(jenkinsReportTemplate, escapedSummary);
        return s3Client.putObject("output/lightning-jenkins.properties", jenkinsReport);
    }
}
