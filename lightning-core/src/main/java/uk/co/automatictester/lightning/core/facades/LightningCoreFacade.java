package uk.co.automatictester.lightning.core.facades;

import uk.co.automatictester.lightning.core.config.ConfigReader;
import uk.co.automatictester.lightning.core.config.LocalFileSystemConfigReader;
import uk.co.automatictester.lightning.core.readers.*;
import uk.co.automatictester.lightning.core.reporters.TransactionReporter;
import uk.co.automatictester.lightning.core.reporters.jenkins.LocalFileSystemJenkinsReporter;
import uk.co.automatictester.lightning.core.reporters.junit.LocalFileSystemJunitReporter;
import uk.co.automatictester.lightning.core.state.data.JmeterTransactions;
import uk.co.automatictester.lightning.core.state.data.TestData;
import uk.co.automatictester.lightning.core.state.tests.TestSet;

import java.io.File;
import java.util.List;

public class LightningCoreFacade {

    private TestSet testSet;
    private File perfMonCsv;
    private File jmeterCsv;
    private File lightningXml;
    private String junitReportSuffix;

    public void setPerfMonCsv(File file) {
        perfMonCsv = file;
    }

    public void setJmeterCsv(File file) {
        jmeterCsv = file;
    }

    public void setLightningXml(File file) {
        lightningXml = file;
    }

    public void setJunitReportSuffix(String suffix) {
        junitReportSuffix = suffix;
    }

    public void loadConfig() {
        ConfigReader configReader = new LocalFileSystemConfigReader();
        String xmlFile = lightningXml.toPath().toString();
        testSet = configReader.readTests(xmlFile);
    }

    public void loadTestData() {
        CsvDataReader jmeterReader = new JmeterDataReader();
        LocalFilesystemCsvDataReader localReader = new LocalFilesystemCsvDataReader(jmeterReader);
        List<JmeterBean> entries = (List<JmeterBean>) localReader.fromFile(jmeterCsv);
        TestData testData = TestData.getInstance();
        testData.flush();
        testData.addClientSideTestData(entries);
        loadPerfMonDataIfProvided();
    }

    public void setJenkinsBuildNameForVerify() {
        String report = testSet.jenkinsSummaryReport();
        LocalFileSystemJenkinsReporter.storeJenkinsBuildName(report);
    }

    public void setJenkinsBuildNameForReport() {
        TestData testData = TestData.getInstance();
        JmeterTransactions transactions = testData.clientSideTestData();
        TransactionReporter reporter = new TransactionReporter(transactions);
        String report = reporter.summaryReport();
        LocalFileSystemJenkinsReporter.storeJenkinsBuildName(report);
    }

    public void saveJunitReport() {
        LocalFileSystemJunitReporter.setSuffix(junitReportSuffix);
        LocalFileSystemJunitReporter.generateReport(testSet);
    }

    public String executeTests() {
        testSet.executeTests();
        return testSet.testExecutionReport();
    }

    public String runReport() {
        TestData testData = TestData.getInstance();
        JmeterTransactions transactions = testData.clientSideTestData();
        TransactionReporter reporter = new TransactionReporter(transactions);
        return reporter.summaryReport();
    }

    public String testSetExecutionSummaryReport() {
        return testSet.testSetExecutionSummaryReport();
    }

    public boolean hasExecutionFailed() {
        return testSet.hasFailed();
    }

    public boolean hasFailedTransactions() {
        TestData testData = TestData.getInstance();
        JmeterTransactions jmeterTransactions = testData.clientSideTestData();
        return jmeterTransactions.failCount() != 0;
    }

    public String teamCityVerifyStatistics() {
        return testSet.teamCityVerifyStatistics();
    }

    public String teamCityBuildReportSummary() {
        TestData testData = TestData.getInstance();
        JmeterTransactions transactions = testData.clientSideTestData();
        TransactionReporter reporter = new TransactionReporter(transactions);
        return reporter.teamCityBuildReportSummary();
    }

    public String teamCityReportStatistics() {
        TestData testData = TestData.getInstance();
        JmeterTransactions transactions = testData.clientSideTestData();
        TransactionReporter reporter = new TransactionReporter(transactions);
        return reporter.teamCityReportStatistics();
    }

    private void loadPerfMonDataIfProvided() {
        if (perfMonCsv != null) {
            CsvDataReader perfMonReader = new PerfMonDataReader();
            LocalFilesystemCsvDataReader localReader = new LocalFilesystemCsvDataReader(perfMonReader);
            List<PerfMonBean> entries = (List<PerfMonBean>) localReader.fromFile(perfMonCsv);
            TestData.getInstance().addServerSideTestData(entries);
        }
    }
}
