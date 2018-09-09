package uk.co.automatictester.lightning.core.facades;

import uk.co.automatictester.lightning.core.config.ConfigReader;
import uk.co.automatictester.lightning.core.config.LocalFileSystemConfigReader;
import uk.co.automatictester.lightning.core.reporters.jenkins.LocalFileSystemJenkinsReporter;
import uk.co.automatictester.lightning.core.reporters.junit.LocalFileSystemJunitReporter;
import uk.co.automatictester.lightning.core.state.data.JmeterTransactions;
import uk.co.automatictester.lightning.core.state.data.PerfMonEntries;
import uk.co.automatictester.lightning.core.state.data.TestData;

import java.io.File;

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
        JmeterTransactions jmeterTransactions = JmeterTransactions.fromFile(jmeterCsv);
        TestData testData = TestData.getInstance();
        testData.flush();
        testData.addClientSideTestData(jmeterTransactions.asList()); // TODO
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
            PerfMonEntries perfMonDataEntries = PerfMonEntries.fromFile(perfMonCsv); // TODO
            TestData.getInstance().addServerSideTestData(perfMonDataEntries.asList());
        }
    }
}
