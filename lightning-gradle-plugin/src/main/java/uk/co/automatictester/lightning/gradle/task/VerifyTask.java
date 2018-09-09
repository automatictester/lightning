package uk.co.automatictester.lightning.gradle.task;

import org.gradle.api.GradleException;
import org.gradle.api.tasks.TaskAction;

import java.io.File;

public class VerifyTask extends LightningTask {

    @TaskAction
    public void verify() {
        if (!extension.hasAllVerifyInput()) {
            throw new GradleException("Not all mandatory input specified for this task or specified files not readable");
        }

        core.setJmeterCsv(extension.getJmeterCsv());
        core.setPerfMonCsv(extension.getPerfmonCsv());
        core.loadTestData();

        runTests();
        saveJunitReport();
        notifyCIServer();
        setExitCode();
    }

    private void runTests() {
        long testSetExecStart = System.currentTimeMillis();

        File testSetXml = extension.getTestSetXml();
        core.setLightningXml(testSetXml);
        core.loadConfig();

        String testExecutionReport = core.executeTests();
        log(testExecutionReport);

        String testSetExecutionSummaryReport = core.testSetExecutionSummaryReport();
        log(testSetExecutionSummaryReport);

        long testSetExecEnd = System.currentTimeMillis();
        long testExecTime = testSetExecEnd - testSetExecStart;
        String message = String.format("Execution time:    %dms", testExecTime);
        log(message);

        if (core.hasExecutionFailed()) {
            exitCode = 1;
        }
    }

    private void saveJunitReport() {
        core.saveJunitReport();
    }

    private void notifyCIServer() {
        String teamCityVerifyStatistics = core.teamCityVerifyStatistics();
        log(teamCityVerifyStatistics);
        core.setJenkinsBuildNameForVerify();
    }
}
