package uk.co.automatictester.lightning.gradle.plugin;

import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

import static org.gradle.internal.impldep.org.hamcrest.MatcherAssert.assertThat;
import static org.gradle.internal.impldep.org.hamcrest.core.Is.is;
import static org.gradle.testkit.runner.TaskOutcome.FAILED;
import static org.gradle.testkit.runner.TaskOutcome.SUCCESS;

public class VerifyTaskTest extends FileAndOutputComparisonTest {

    @Test
    public void runVerify() {
        BuildResult result = GradleRunner.create()
                .withProjectDir(new File("src/integrationTest/resources/build/complete"))
                .withArguments(":verify")
                .withPluginClasspath()
                .build();

        assertThat(result.task(":verify").getOutcome(), is(SUCCESS));
        assertThat(taskOutputContainsFileContent("/results/expected/3_0_0.txt", result), is(true));
    }

    @Test
    public void runVerifyWithRegexp() {
        BuildResult result = GradleRunner.create()
                .withProjectDir(new File("src/integrationTest/resources/build/regexp"))
                .withArguments(":verify")
                .withPluginClasspath()
                .build();

        assertThat(result.task(":verify").getOutcome(), is(SUCCESS));
        assertThat(taskOutputContainsFileContent("/results/expected/regexp.txt", result), is(true));
    }

    @Test
    public void runVerifyWithServerSideTests() {
        BuildResult result = GradleRunner.create()
                .withProjectDir(new File("src/integrationTest/resources/build/perfmon"))
                .withArguments(":verify")
                .withPluginClasspath()
                .build();

        assertThat(result.task(":verify").getOutcome(), is(SUCCESS));
        assertThat(taskOutputContainsFileContent("/results/expected/1_client_2_server.txt", result), is(true));
    }

    @Test
    public void runVerifyAndCheckJUnitReport() throws IOException {
        BuildResult result = GradleRunner.create()
                .withProjectDir(new File("src/integrationTest/resources/build/junit"))
                .withArguments(":verify")
                .withPluginClasspath()
                .buildAndFail();

        assertThat(result.task(":verify").getOutcome(), is(FAILED));
        assertThat(fileContentIsEqual("/results/expected/junit-expected.xml", "junit.xml"), is(true));
    }

    @Test
    public void runVerifyWithFailureAndError() throws IOException {
        BuildResult result = GradleRunner.create()
                .withProjectDir(new File("src/integrationTest/resources/build/1_1_1"))
                .withArguments(":verify")
                .withPluginClasspath()
                .buildAndFail();

        assertThat(result.task(":verify").getOutcome(), is(FAILED));
        assertThat(taskOutputContainsFileContent("/results/expected/1_1_1.txt", result), is(true));
        assertThat(fileContainsText("lightning-jenkins.properties", "result.string=Tests executed\\: 3, failed\\: 2"), is(true));
    }

    @Test(dataProvider = "getProjectDir")
    public void runVerifyWithMissingInput(String projectDir) {
        BuildResult result = GradleRunner.create()
                .withProjectDir(new File(projectDir))
                .withArguments(":verify")
                .withPluginClasspath()
                .buildAndFail();

        assertThat(taskOutputContainsText("Not all mandatory input specified for this task or specified files not readable", result), is(true));
    }

    @DataProvider
    private Object[][] getProjectDir() {
        return new Object[][]{
                new Object[]{"src/integrationTest/resources/build/no/csv"},
                new Object[]{"src/integrationTest/resources/build/no/xml"}
        };
    }
}