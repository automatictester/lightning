package uk.co.automatictester.lightning.core.facade;

import uk.co.automatictester.lightning.core.ci.JUnitReporter;
import uk.co.automatictester.lightning.core.ci.JenkinsReporter;
import uk.co.automatictester.lightning.core.ci.TeamCityReporter;
import uk.co.automatictester.lightning.core.config.LightningConfig;
import uk.co.automatictester.lightning.core.data.JMeterTransactions;
import uk.co.automatictester.lightning.core.data.PerfMonEntries;
import uk.co.automatictester.lightning.core.reporters.JMeterReporter;
import uk.co.automatictester.lightning.core.reporters.TestSetReporter;
import uk.co.automatictester.lightning.core.state.TestSet;
import uk.co.automatictester.lightning.core.structures.TestData;

import java.io.File;

public class LightningCoreFacade { // TODO: extract some of it to parent and rename this to LightningCoreFileSystemFacade

    protected TestSet testSet = new TestSet();
    protected TeamCityReporter teamCityReporter;
    protected JMeterTransactions jmeterTransactions;

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

    public String executeTests() {
        testSet.executeTests();
        return testSet.getTestExecutionReport();
    }

    public String runReport() {
        return JMeterReporter.getJMeterReport(jmeterTransactions);
    }

    public String getTestSetExecutionSummaryReport() {
        return TestSetReporter.getTestSetExecutionSummaryReport(testSet);
    }

    public boolean hasExecutionFailed() {
        return testSet.getFailCount() + testSet.getErrorCount() != 0;
    }

    public boolean hasFailedTransactions() {
        return jmeterTransactions.getFailCount() != 0;
    }

    public String getTeamCityVerifyStatistics() {
        return TeamCityReporter.fromTestSet(testSet).getTeamCityVerifyStatistics();
    }

    public void setJenkinsBuildNameForVerify() {
        JenkinsReporter.fromTestSet(testSet).setJenkinsBuildName();
    }

    public void setJenkinsBuildNameForReport() {
        JenkinsReporter.fromJMeterTransactions(jmeterTransactions).setJenkinsBuildName();
    }

    public String getTeamCityBuildReportSummary() {
        setTeamCityReporter();
        return teamCityReporter.getTeamCityBuildReportSummary();
    }

    public String getTeamCityReportStatistics() {
        setTeamCityReporter();
        return teamCityReporter.getTeamCityReportStatistics();
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

    private void setTeamCityReporter() {
        if (teamCityReporter == null) {
            teamCityReporter = TeamCityReporter.fromJMeterTransactions(jmeterTransactions);
        }
    }
}
