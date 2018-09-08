package uk.co.automatictester.lightning.core.facades;

import uk.co.automatictester.lightning.core.facades.base.AbstractLightningCoreFacade;
import uk.co.automatictester.lightning.core.readers.ConfigS3Reader;
import uk.co.automatictester.lightning.core.reporters.ci.JenkinsS3Reporter;
import uk.co.automatictester.lightning.core.reporters.junit.JunitS3Reporter;
import uk.co.automatictester.lightning.core.s3client.S3Client;
import uk.co.automatictester.lightning.core.s3client.factory.S3ClientFlyweightFactory;
import uk.co.automatictester.lightning.core.state.data.JmeterTransactions;
import uk.co.automatictester.lightning.core.state.data.PerfMonEntries;
import uk.co.automatictester.lightning.core.state.data.TestData;

public class LightningCoreS3Facade extends AbstractLightningCoreFacade {

    private static S3Client client;
    private String region;
    private String bucket;
    private String perfMonCsv;
    private String jmeterCsv;
    private String lightningXml;

    public void setRegionAndBucket(String region, String bucket) {
        this.region = region;
        this.bucket = bucket;
        client = S3ClientFlyweightFactory.getInstance(region).setBucket(bucket);
    }

    public void setPerfMonCsv(String key) {
        perfMonCsv = key;
    }

    public void setJmeterCsv(String key) {
        jmeterCsv = key;
    }

    public void setLightningXml(String key) {
        lightningXml = key;
    }

    public void loadConfigFromS3() {
        ConfigS3Reader configReader = new ConfigS3Reader();
        testSet = configReader.readTests(region, bucket, lightningXml);
    }

    public void loadTestDataFromS3() {
        jmeterTransactions = JmeterTransactions.fromS3Object(region, bucket, jmeterCsv);
        TestData testData = TestData.getInstance();
        testData.flush();
        testData.addClientSideTestData(jmeterTransactions);
        loadPerfMonDataIfProvided();
    }

    public String storeJenkinsBuildNameForVerifyInS3() {
        return JenkinsS3Reporter.fromTestSet(region, bucket, testSet).storeJenkinsBuildNameInS3();
    }

    public String storeJenkinsBuildNameForReportInS3() {
        return JenkinsS3Reporter.fromJmeterTransactions(region, bucket, jmeterTransactions).storeJenkinsBuildNameInS3();
    }

    public String saveJunitReportToS3() {
        JunitS3Reporter junitS3Reporter = new JunitS3Reporter(region, bucket);
        return junitS3Reporter.generateJunitReportToS3(testSet);
    }

    public String putS3Object(String key, String content) {
        return client.putObject(key, content);
    }

    private void loadPerfMonDataIfProvided() {
        if (perfMonCsv != null) {
            PerfMonEntries perfMonDataEntries = PerfMonEntries.fromS3Object(region, bucket, perfMonCsv);
            TestData.getInstance().addServerSideTestData(perfMonDataEntries);
        }
    }
}
