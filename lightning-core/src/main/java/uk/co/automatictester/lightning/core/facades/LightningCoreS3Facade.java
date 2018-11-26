package uk.co.automatictester.lightning.core.facades;

import uk.co.automatictester.lightning.core.config.ConfigReader;
import uk.co.automatictester.lightning.core.config.S3ConfigReader;
import uk.co.automatictester.lightning.core.readers.CsvDataReader;
import uk.co.automatictester.lightning.core.readers.JmeterDataReader;
import uk.co.automatictester.lightning.core.readers.PerfMonDataReader;
import uk.co.automatictester.lightning.core.readers.S3CsvDataReader;
import uk.co.automatictester.lightning.core.reporters.TransactionReporter;
import uk.co.automatictester.lightning.core.reporters.jenkins.S3JenkinsReporter;
import uk.co.automatictester.lightning.core.reporters.junit.S3JunitReporter;
import uk.co.automatictester.lightning.core.s3client.S3Client;
import uk.co.automatictester.lightning.core.s3client.factory.S3ClientFlyweightFactory;
import uk.co.automatictester.lightning.core.state.data.JmeterTransactions;
import uk.co.automatictester.lightning.core.state.data.TestData;

import java.util.List;

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
        ConfigReader configReader = new S3ConfigReader(region, bucket);
        testSet = configReader.readTests(lightningXml);
    }

    public void loadTestDataFromS3() {
        CsvDataReader jmeterReader = new JmeterDataReader();
        S3CsvDataReader s3Reader = new S3CsvDataReader(jmeterReader);
        List<String[]> entries = s3Reader.fromS3Object(region, bucket, jmeterCsv);
        TestData testData = TestData.getInstance();
        testData.flush();
        testData.addClientSideTestData(entries);
        loadPerfMonDataIfProvided();
    }

    public String storeJenkinsBuildNameForVerifyInS3() {
        String report = testSet.jenkinsSummaryReport();
        return new S3JenkinsReporter(region, bucket).storeJenkinsBuildNameToS3(report);
    }

    public String storeJenkinsBuildNameForReportInS3() {
        TestData testData = TestData.getInstance();
        JmeterTransactions transactions = testData.clientSideTestData();
        TransactionReporter reporter = new TransactionReporter(transactions);
        String report = reporter.summaryReport();
        return new S3JenkinsReporter(region, bucket).storeJenkinsBuildNameToS3(report);
    }

    public String saveJunitReportToS3() {
        return S3JunitReporter.generateReport(region, bucket, testSet);
    }

    public String putS3Object(String key, String content) {
        return client.putObject(key, content);
    }

    private void loadPerfMonDataIfProvided() {
        if (perfMonCsv != null) {
            CsvDataReader perfMonReader = new PerfMonDataReader();
            S3CsvDataReader s3Reader = new S3CsvDataReader(perfMonReader);
            List<String[]> entries = s3Reader.fromS3Object(region, bucket, perfMonCsv);
            TestData.getInstance().addServerSideTestData(entries);
        }
    }
}
