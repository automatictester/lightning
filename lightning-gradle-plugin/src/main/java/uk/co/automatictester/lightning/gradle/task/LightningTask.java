package uk.co.automatictester.lightning.gradle.task;

import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import uk.co.automatictester.lightning.core.facades.LightningCoreLocalFacade;
import uk.co.automatictester.lightning.gradle.extension.LightningExtension;

import java.util.Arrays;

abstract class LightningTask extends DefaultTask {

    protected LightningCoreLocalFacade core = new LightningCoreLocalFacade();
    protected LightningExtension extension;
    protected int exitCode = 0;

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
