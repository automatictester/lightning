package uk.co.automatictester.lightning.gradle.task;

import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import uk.co.automatictester.lightning.TestSet;
import uk.co.automatictester.lightning.data.JMeterTransactions;
import uk.co.automatictester.lightning.gradle.extension.LightningExtension;

import java.util.Arrays;

abstract class LightningTask extends DefaultTask {

    protected int exitCode = 0;
    protected TestSet testSet = new TestSet();
    protected JMeterTransactions jmeterTransactions;
    protected LightningExtension extension;

    LightningTask() {
        extension = getProject().getExtensions().findByType(LightningExtension.class);
        if (extension == null) {
            extension = new LightningExtension();
        }
    }

    protected void setExitCode() {
        if (exitCode != 0) {
            throw new GradleException("Task failed");
        }
    }

    protected void log(String text) {
        for (String line : Arrays.asList(text.split(System.lineSeparator()))) {
            System.out.println(line);
        }
    }
}
