package uk.co.automatictester.lightning.gradle.plugin;

import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

import static org.gradle.internal.impldep.org.hamcrest.MatcherAssert.assertThat;
import static org.gradle.internal.impldep.org.hamcrest.core.Is.is;
import static org.gradle.testkit.runner.TaskOutcome.SUCCESS;

public class ReportTaskTest extends FileAndOutputComparisonTest {

    @Test
    public void runReport() {
        BuildResult result = GradleRunner.create()
                .withProjectDir(new File("src/integrationTest/resources/build/complete"))
                .withArguments(":report")
                .withPluginClasspath()
                .build();

        assertThat(result.task(":report").getOutcome(), is(SUCCESS));
        assertThat(taskOutputContainsFileContent("/results/expected/report.txt", result), is(true));
    }

    @Test
    public void runReportWithMissingInput() {
        BuildResult result = GradleRunner.create()
                .withProjectDir(new File("src/integrationTest/resources/build/no/csv"))
                .withArguments(":report")
                .withPluginClasspath()
                .buildAndFail();

        assertThat(taskOutputContainsText("Not all mandatory input specified for this task or specified files not readable", result), is(true));
    }

    @Test
    public void runReportWithFailureExpected() throws IOException {
        BuildResult result = GradleRunner.create()
                .withProjectDir(new File("src/integrationTest/resources/build/failure_report"))
                .withArguments(":report")
                .withPluginClasspath()
                .buildAndFail();

        assertThat(taskOutputContainsFileContent("/results/expected/report_failure.txt", result), is(true));
        assertThat(fileContainsText("lightning-jenkins.properties", "result.string=Transactions executed\\: 2, failed\\: 1"), is(true));
    }
}