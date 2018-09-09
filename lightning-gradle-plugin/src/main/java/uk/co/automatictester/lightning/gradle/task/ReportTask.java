package uk.co.automatictester.lightning.gradle.task;

import org.gradle.api.GradleException;
import org.gradle.api.tasks.TaskAction;

import java.io.File;

public class ReportTask extends LightningTask {

    @TaskAction
    public void report() {
        if (!extension.hasAllReportInput()) {
            throw new GradleException("Not all mandatory input specified for this task or specified files not readable");
        }

        core.setJmeterCsv(extension.getJmeterCsv());
        core.loadTestData();

        runReport();
        notifyCIServer();
        setExitCode();
    }

    private void runReport() {
        File jmeterCsv = extension.getJmeterCsv();
        core.setJmeterCsv(jmeterCsv);
        String report = core.runReport();
        log(report);
        if (core.hasFailedTransactions()) {
            exitCode = 1;
        }
    }

    private void notifyCIServer() {
        String teamCityBuildReportSummary = core.teamCityBuildReportSummary();
        log(teamCityBuildReportSummary);
        String teamCityReportStatistics = core.teamCityReportStatistics();
        log(teamCityReportStatistics);
        core.setJenkinsBuildNameForReport();
    }
}
