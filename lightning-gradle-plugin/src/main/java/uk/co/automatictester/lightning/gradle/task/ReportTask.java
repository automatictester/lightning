package uk.co.automatictester.lightning.gradle.task;

import org.gradle.api.GradleException;
import org.gradle.api.tasks.TaskAction;
import uk.co.automatictester.lightning.ci.JenkinsReporter;
import uk.co.automatictester.lightning.ci.TeamCityReporter;
import uk.co.automatictester.lightning.data.JMeterTransactions;
import uk.co.automatictester.lightning.reporters.JMeterReporter;

public class ReportTask extends LightningTask {

    @TaskAction
    public void report() {
        if (!extension.hasAllReportInput()) {
            throw new GradleException("Not all mandatory input specified for this task or specified files not readable");
        }
        runReport();
        notifyCIServer();
        setExitCode();
    }

    private void runReport() {
        jmeterTransactions = JMeterTransactions.fromFile(extension.getJmeterCsv());
        log(JMeterReporter.getJMeterReport(jmeterTransactions));
        if (jmeterTransactions.getFailCount() != 0) {
            exitCode = 1;
        }
    }

    private void notifyCIServer() {
        TeamCityReporter teamCityReporter = TeamCityReporter.fromJMeterTransactions(jmeterTransactions);
        log(teamCityReporter.getTeamCityBuildReportSummary());
        log(teamCityReporter.getTeamCityReportStatistics());
        JenkinsReporter.fromJMeterTransactions(jmeterTransactions).setJenkinsBuildName();
    }
}
