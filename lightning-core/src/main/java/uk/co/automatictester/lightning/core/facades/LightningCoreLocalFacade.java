package uk.co.automatictester.lightning.core.facades;

import uk.co.automatictester.lightning.core.config.ConfigReader;
import uk.co.automatictester.lightning.core.config.LocalFileSystemConfigReader;
import uk.co.automatictester.lightning.core.readers.CsvDataReader;
import uk.co.automatictester.lightning.core.readers.JmeterDataReader;
import uk.co.automatictester.lightning.core.readers.PerfMonDataReader;
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
        CsvDataReader reader = new JmeterDataReader();
        List<String[]> entries = reader.fromFile(jmeterCsv);
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
        JmeterTransactions jmeterTransactions = testData.clientSideTestData();
        String report = jmeterTransactions.summaryReport();
        LocalFileSystemJenkinsReporter.storeJenkinsBuildName(report);
    }

    public void saveJunitReport() {
        LocalFileSystemJunitReporter.generateReport(testSet);
    }

    private void loadPerfMonDataIfProvided() {
        if (perfMonCsv != null) {
            CsvDataReader reader = new PerfMonDataReader();
            List<String[]> entries = reader.fromFile(perfMonCsv);
            TestData.getInstance().addServerSideTestData(entries);
        }
    }
}
