package uk.co.deliverymind.lightning.gradle.task;

import org.gradle.api.GradleException;
import org.gradle.api.tasks.TaskAction;
import uk.co.deliverymind.lightning.ci.JenkinsReporter;
import uk.co.deliverymind.lightning.ci.TeamCityReporter;

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
        TeamCityReporter teamCityReporter = new TeamCityReporter(jmeterTransactions);
        log(teamCityReporter.getTeamCityBuildStatusText());
        log(teamCityReporter.getTeamCityReportStatistics());
        new JenkinsReporter(jmeterTransactions).setJenkinsBuildName();
    }
}
