package uk.co.automatictester.lightning.lambda.ci;

import uk.co.automatictester.lightning.TestSet;
import uk.co.automatictester.lightning.ci.CIReporter;
import uk.co.automatictester.lightning.data.JMeterTransactions;
import uk.co.automatictester.lightning.lambda.s3.S3Client;

public class JenkinsS3Reporter extends CIReporter {

    private static S3Client s3Client;

    public JenkinsS3Reporter(String region, String bucket, TestSet testSet) {
        super(testSet);
        s3Client = new S3Client(region, bucket);
    }

    public JenkinsS3Reporter(String region, String bucket, JMeterTransactions jmeterTransactions) {
        super(jmeterTransactions);
        s3Client = new S3Client(region, bucket);
    }

    public String storeJenkinsBuildNameInS3() {
        if (testSet != null) {
            return storeJenkinsReportToS3(getVerifySummary(testSet));
        } else if (jmeterTransactions != null) {
            return storeJenkinsReportToS3(getReportSummary(jmeterTransactions));
        }
        return null;
    }

    private String storeJenkinsReportToS3(String summary) {
        String escapedSummary = summary.replace(":", "\\:");
        String jenkinsReportTemplate = "#In Jenkins Build Name Setter Plugin, define build name as: ${BUILD_NUMBER} - ${PROPFILE,file=\"lightning-jenkins.properties\",property=\"result.string\"}\nresult.string=%s\n";
        String jenkinsReport = String.format(jenkinsReportTemplate, escapedSummary);
        return s3Client.putS3Object("output/lightning-jenkins.properties", jenkinsReport);
    }
}
