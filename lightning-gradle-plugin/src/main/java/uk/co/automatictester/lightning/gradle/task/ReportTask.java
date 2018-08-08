package uk.co.automatictester.lightning.gradle.task;

import org.gradle.api.GradleException;
import org.gradle.api.tasks.TaskAction;
import uk.co.automatictester.lightning.ci.JenkinsReporter;
import uk.co.automatictester.lightning.ci.TeamCityReporter;

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

    private void notifyCIServer() {
        TeamCityReporter teamCityReporter = TeamCityReporter.fromJMeterTransactions(jmeterTransactions);
        log(teamCityReporter.getTeamCityBuildReportSummary());
        log(teamCityReporter.getTeamCityReportStatistics());
        JenkinsReporter.fromJMeterTransactions(jmeterTransactions).setJenkinsBuildName();
    }
}
