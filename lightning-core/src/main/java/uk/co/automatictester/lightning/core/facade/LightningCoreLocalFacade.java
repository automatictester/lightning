package uk.co.automatictester.lightning.core.facade;

import uk.co.automatictester.lightning.core.ci.JUnitReporter;
import uk.co.automatictester.lightning.core.ci.JenkinsReporter;
import uk.co.automatictester.lightning.core.config.LightningConfig;
import uk.co.automatictester.lightning.core.data.JMeterTransactions;
import uk.co.automatictester.lightning.core.data.PerfMonEntries;
import uk.co.automatictester.lightning.core.structures.TestData;

import java.io.File;

public class LightningCoreLocalFacade extends LightningCoreFacade {

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
        LightningConfig lightningConfig = new LightningConfig();
        lightningConfig.readTests(lightningXml);
    }

    public void loadTestData() {
        jmeterTransactions = JMeterTransactions.fromFile(jmeterCsv);
        TestData.addClientSideTestData(jmeterTransactions);
        loadPerfMonDataIfProvided();
    }

    public void setJenkinsBuildNameForVerify() {
        JenkinsReporter.fromTestSet(testSet).setJenkinsBuildName();
    }

    public void setJenkinsBuildNameForReport() {
        JenkinsReporter.fromJMeterTransactions(jmeterTransactions).setJenkinsBuildName();
    }

    public void saveJunitReport() {
        JUnitReporter junitreporter = new JUnitReporter();
        junitreporter.generateJUnitReport(testSet);
    }

    private void loadPerfMonDataIfProvided() {
        if (perfMonCsv != null) {
            PerfMonEntries perfMonDataEntries = PerfMonEntries.fromFile(perfMonCsv);
            TestData.addServerSideTestData(perfMonDataEntries);
        }
    }
}
