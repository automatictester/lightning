package uk.co.automatictester.lightning.core.facades;

import uk.co.automatictester.lightning.core.config.ConfigReader;
import uk.co.automatictester.lightning.core.config.LocalFileSystemConfigReader;
import uk.co.automatictester.lightning.core.readers.CsvDataReader;
import uk.co.automatictester.lightning.core.readers.JmeterDataReader;
import uk.co.automatictester.lightning.core.readers.LocalFilesystemCsvDataReader;
import uk.co.automatictester.lightning.core.readers.PerfMonDataReader;
import uk.co.automatictester.lightning.core.reporters.TransactionReporter;
import uk.co.automatictester.lightning.core.reporters.jenkins.LocalFileSystemJenkinsReporter;
import uk.co.automatictester.lightning.core.reporters.junit.LocalFileSystemJunitReporter;
import uk.co.automatictester.lightning.core.state.data.JmeterTransactions;
import uk.co.automatictester.lightning.core.state.data.TestData;

import java.io.File;
import java.util.List;

public class LightningCoreLocalFacade extends AbstractLightningCoreFacade {

    private File perfMonCsv;
    private File jmeterCsv;
    private File lightningXml;

    public void setPerfMonCsv(File file) {
        perfMonCsv = file;
    }

    public void setJmeterCsv(File file) {
        jmeterCsv = file;
    }

    public void setLightningXml(File file) {
        lightningXml = file;
    }

    public void loadConfig() {
        ConfigReader configReader = new LocalFileSystemConfigReader();
        String xmlFile = lightningXml.toPath().toString();
        testSet = configReader.readTests(xmlFile);
    }

    public void loadTestData() {
        CsvDataReader jmeterReader = new JmeterDataReader();
        LocalFilesystemCsvDataReader localReader = new LocalFilesystemCsvDataReader(jmeterReader);
        List<String[]> entries = localReader.fromFile(jmeterCsv);
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
        LocalFileSystemJunitReporter.generateReport(testSet);
    }

    private void loadPerfMonDataIfProvided() {
        if (perfMonCsv != null) {
            CsvDataReader perfMonReader = new PerfMonDataReader();
            LocalFilesystemCsvDataReader localReader = new LocalFilesystemCsvDataReader(perfMonReader);
            List<String[]> entries = localReader.fromFile(perfMonCsv);
            TestData.getInstance().addServerSideTestData(entries);
        }
    }
}
