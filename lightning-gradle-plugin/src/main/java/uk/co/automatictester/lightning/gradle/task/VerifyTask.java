package uk.co.automatictester.lightning.gradle.task;

import org.gradle.api.GradleException;
import org.gradle.api.tasks.TaskAction;
import uk.co.automatictester.lightning.ci.JenkinsReporter;
import uk.co.automatictester.lightning.ci.TeamCityReporter;

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

    private void notifyCIServer() {
        log(TeamCityReporter.fromTestSet(testSet).getTeamCityVerifyStatistics());
        JenkinsReporter.fromTestSet(testSet).setJenkinsBuildName();
    }
}
