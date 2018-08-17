package uk.co.automatictester.lightning.lambda.ci;

import uk.co.automatictester.lightning.core.state.TestSet;
import uk.co.automatictester.lightning.core.ci.CIReporter;
import uk.co.automatictester.lightning.core.data.JMeterTransactions;
import uk.co.automatictester.lightning.core.s3.S3Client;

public class JenkinsS3Reporter extends CIReporter {

    private static S3Client s3Client;

    private JenkinsS3Reporter(String region, String bucket, TestSet testSet) {
        super(testSet);
        s3Client = new S3Client(region, bucket);
    }

    private JenkinsS3Reporter(String region, String bucket, JMeterTransactions jmeterTransactions) {
        super(jmeterTransactions);
        s3Client = new S3Client(region, bucket);
    }

    public static JenkinsS3Reporter fromTestSet(String region, String bucket, TestSet testSet) {
        return new JenkinsS3Reporter(region, bucket, testSet);
    }

    public static JenkinsS3Reporter fromJMeterTransactions(String region, String bucket, JMeterTransactions jmeterTransactions) {
        return new JenkinsS3Reporter(region, bucket, jmeterTransactions);
    }

    public String storeJenkinsBuildNameInS3() {
        if (testSet != null) {
            String jenkinsReportContent = getVerifySummary(testSet);
            return storeJenkinsReportToS3(jenkinsReportContent);
        } else {
            return storeJenkinsReportToS3(getReportSummary());
        }
    }

    private static String getVerifySummary(TestSet testSet) {
        int executed = testSet.getTestCount();
        int failed = testSet.getFailCount() + testSet.getErrorCount();
        return String.format("Tests executed: %s, failed: %s", executed, failed);
    }

    private String storeJenkinsReportToS3(String summary) {
        String escapedSummary = summary.replace(":", "\\:");
        String jenkinsReportTemplate = "#In Jenkins Build Name Setter Plugin, define build name as: ${BUILD_NUMBER} - ${PROPFILE,file=\"lightning-jenkins.properties\",property=\"result.string\"}\nresult.string=%s\n";
        String jenkinsReport = String.format(jenkinsReportTemplate, escapedSummary);
        return s3Client.putS3Object("output/lightning-jenkins.properties", jenkinsReport);
    }
}
