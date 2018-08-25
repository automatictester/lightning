package uk.co.automatictester.lightning.core.facade;

import uk.co.automatictester.lightning.core.data.JMeterTransactions;
import uk.co.automatictester.lightning.core.data.PerfMonEntries;
import uk.co.automatictester.lightning.core.s3.JUnitS3Reporter;
import uk.co.automatictester.lightning.core.s3.JenkinsS3Reporter;
import uk.co.automatictester.lightning.core.s3.LightningLambdaConfig;
import uk.co.automatictester.lightning.core.s3.S3Client;
import uk.co.automatictester.lightning.core.structures.TestData;

public class LightningCoreS3Facade extends LightningCoreFacade {

    private static S3Client client;

    private String region;
    private String bucket;
    private String perfMonCsv;
    private String jmeterCsv;
    private String lightningXml;

    public void setRegionAndBucket(String region, String bucket) {
        this.region = region;
        this.bucket = bucket;
        client = S3Client.getInstance(region).setS3Bucket(bucket);
    }

    public void setPerfMonCsv(String object) {
        perfMonCsv = object;
    }

    public void setJmeterCsv(String object) {
        jmeterCsv = object;
    }

    public void setLightningXml(String object) {
        lightningXml = object;
    }

    public void loadConfigFromS3() {
        LightningLambdaConfig lightningLambdaConfig = new LightningLambdaConfig(region, bucket);
        lightningLambdaConfig.readTests(lightningXml);
    }

    public void loadTestDataFromS3() {
        jmeterTransactions = JMeterTransactions.fromS3Object(region, bucket, jmeterCsv);
        TestData.addClientSideTestData(jmeterTransactions);
        loadPerfMonDataIfProvided();
    }

    public String storeJenkinsBuildNameForVerifyInS3() {
        return JenkinsS3Reporter.fromTestSet(region, bucket, testSet).storeJenkinsBuildNameInS3();
    }

    public String storeJenkinsBuildNameForReportInS3() {
        return JenkinsS3Reporter.fromJMeterTransactions(region, bucket, jmeterTransactions).storeJenkinsBuildNameInS3();
    }

    public String saveJunitReportToS3() {
        JUnitS3Reporter junitS3Reporter = new JUnitS3Reporter(region, bucket);
        return junitS3Reporter.generateJUnitReportToS3(testSet);
    }

    public String putS3Object(String key, String content) {
        return client.putObject(key, content);
    }

    private void loadPerfMonDataIfProvided() {
        if (perfMonCsv != null) {
            PerfMonEntries perfMonDataEntries = PerfMonEntries.fromS3Object(region, bucket, perfMonCsv);
            TestData.addServerSideTestData(perfMonDataEntries);
        }
    }
}
