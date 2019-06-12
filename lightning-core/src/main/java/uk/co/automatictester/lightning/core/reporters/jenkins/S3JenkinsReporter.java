package uk.co.automatictester.lightning.core.reporters.jenkins;

import uk.co.automatictester.lightning.core.s3client.S3Client;
import uk.co.automatictester.lightning.core.s3client.factory.S3ClientFlyweightFactory;

public class S3JenkinsReporter {

    private static S3Client s3Client;

    public S3JenkinsReporter(String region, String bucket) {
        s3Client = S3ClientFlyweightFactory.getInstance(region).setBucket(bucket);
    }

    public String storeJenkinsBuildNameToS3(String report) {
        String escapedReport = report.replace(":", "\\:");
        String jenkinsReportTemplate = "#In Jenkins Build Name Setter Plugin, define build name as: ${BUILD_NUMBER} - ${PROPFILE,file=\"lightning-jenkins.properties\",property=\"result.string\"}\nresult.string=%s\n";
        String finalReport = String.format(jenkinsReportTemplate, escapedReport);
        return s3Client.putObject("output/lightning-jenkins.properties", finalReport);
    }
}
