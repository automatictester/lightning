package uk.co.automatictester.lightning.gradle.plugin;

import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;

import static org.gradle.internal.impldep.org.hamcrest.MatcherAssert.assertThat;
import static org.gradle.internal.impldep.org.hamcrest.core.Is.is;
import static org.gradle.testkit.runner.TaskOutcome.SUCCESS;

public class GradleCompatibilityTest {

    @Test(dataProvider = "getGradleVersions")
    public void checkGradleCompatibility(String gradleVersion) {
        BuildResult result = GradleRunner.create()
                .withGradleVersion(gradleVersion)
                .withProjectDir(new File("src/integrationTest/resources/build/complete"))
                .withArguments(":report")
                .withPluginClasspath()
                .build();

        assertThat(result.task(":report").getOutcome(), is(SUCCESS));
    }

    @DataProvider
    private Object[][] getGradleVersions() {
        return new Object[][] {
                new Object[] { "2.14.1" },
                new Object[] { "3.5" },
                new Object[] { "4.0" }
        };
    }
}
