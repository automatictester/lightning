package uk.co.automatictester.lightning.core.facades;

import uk.co.automatictester.lightning.core.facades.base.AbstractLightningCoreFacade;
import uk.co.automatictester.lightning.core.readers.ConfigReader;
import uk.co.automatictester.lightning.core.reporters.ci.JenkinsReporter;
import uk.co.automatictester.lightning.core.reporters.junit.JunitReporter;
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
        ConfigReader.readTests(lightningXml);
    }

    public void loadTestData() {
        jmeterTransactions = JmeterTransactions.fromFile(jmeterCsv);
        TestData testData = TestData.getInstance();
        testData.flush();
        testData.addClientSideTestData(jmeterTransactions);
        loadPerfMonDataIfProvided();
    }

    public void setJenkinsBuildNameForVerify() {
        JenkinsReporter.fromTestSet(testSetResult).setJenkinsBuildName();
    }

    public void setJenkinsBuildNameForReport() {
        JenkinsReporter.fromJmeterTransactions(jmeterTransactions).setJenkinsBuildName();
    }

    public void saveJunitReport() {
        JunitReporter junitreporter = new JunitReporter();
        junitreporter.generateJUnitReport(testSetResult);
    }

    private void loadPerfMonDataIfProvided() {
        if (perfMonCsv != null) {
            PerfMonEntries perfMonDataEntries = PerfMonEntries.fromFile(perfMonCsv);
            TestData.getInstance().addServerSideTestData(perfMonDataEntries);
        }
    }
}
