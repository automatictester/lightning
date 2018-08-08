package uk.co.automatictester.lightning.gradle.task;

import org.gradle.api.GradleException;
import org.gradle.api.tasks.TaskAction;
import uk.co.automatictester.lightning.TestSet;
import uk.co.automatictester.lightning.ci.JUnitReporter;
import uk.co.automatictester.lightning.ci.JenkinsReporter;
import uk.co.automatictester.lightning.ci.TeamCityReporter;
import uk.co.automatictester.lightning.config.LightningConfig;
import uk.co.automatictester.lightning.data.JMeterTransactions;
import uk.co.automatictester.lightning.data.PerfMonEntries;
import uk.co.automatictester.lightning.reporters.TestSetReporter;
import uk.co.automatictester.lightning.tests.ClientSideTest;
import uk.co.automatictester.lightning.tests.ServerSideTest;

import java.util.List;

public class VerifyTask extends LightningTask {

    @TaskAction
    public void verify() {
        if (!extension.hasAllVerifyInput()) {
            throw new GradleException("Not all mandatory input specified for this task or specified files not readable");
        }
        runTests();
        saveJunitReport();
        notifyCIServer();
        setExitCode();
    }

    private void runTests() {
        long testSetExecStart = System.currentTimeMillis();

        LightningConfig lightningConfig = new LightningConfig();
        lightningConfig.readTests(extension.getTestSetXml());
        populateTestSet(lightningConfig);

        jmeterTransactions = JMeterTransactions.fromFile(extension.getJmeterCsv());

        executeServerSideTestsIfPerfMonDataProvided();
        testSet.executeClientSideTests(jmeterTransactions);
        log(testSet.getTestExecutionReport());

        log(TestSetReporter.getTestSetExecutionSummaryReport(testSet));

        long testSetExecEnd = System.currentTimeMillis();
        long testExecTime = testSetExecEnd - testSetExecStart;
        log(String.format("Execution time:    %dms", testExecTime));

        if (testSet.getFailCount() + testSet.getErrorCount() != 0) {
            exitCode = 1;
        }
    }

    private void saveJunitReport() {
        JUnitReporter junitreporter = new JUnitReporter();
        junitreporter.generateJUnitReport(testSet);
    }

    private void notifyCIServer() {
        log(TeamCityReporter.fromTestSet(testSet).getTeamCityVerifyStatistics());
        JenkinsReporter.fromTestSet(testSet).setJenkinsBuildName();
    }

    private void populateTestSet(LightningConfig lightningConfig) {
        List<ClientSideTest> clientSideTests = lightningConfig.getClientSideTests();
        List<ServerSideTest> serverSideTests = lightningConfig.getServerSideTests();
        if (serverSideTests.size() == 0) {
            testSet = TestSet.fromClientSideTest(clientSideTests);
        } else {
            testSet = TestSet.fromClientAndServerSideTest(clientSideTests, serverSideTests);
        }
    }

    private void executeServerSideTestsIfPerfMonDataProvided() {
        if (extension.getPerfmonCsv() != null) {
            PerfMonEntries perfMonDataEntries = PerfMonEntries.fromFile(extension.getPerfmonCsv());
            testSet.executeServerSideTests(perfMonDataEntries);
        }
    }
}
