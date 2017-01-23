package uk.co.deliverymind.lightning.gradle.task;

import org.gradle.api.GradleException;
import org.gradle.api.tasks.TaskAction;
import uk.co.deliverymind.lightning.ci.JenkinsReporter;
import uk.co.deliverymind.lightning.ci.TeamCityReporter;

public class VerifyTask extends LightningTask {

    @TaskAction
    public void verify() {
        if (!getExtension().hasAllVerifyInput()) {
            throw new GradleException("Not all mandatory input specified for this task or specified files not readable");
        }
        runTests();
        saveJunitReport();
        notifyCIServer();
        setExitCode();
    }

    private void notifyCIServer() {
        log(new TeamCityReporter(getTestSet()).getTeamCityVerifyStatistics());
        new JenkinsReporter(getTestSet()).setJenkinsBuildName();
    }
}
