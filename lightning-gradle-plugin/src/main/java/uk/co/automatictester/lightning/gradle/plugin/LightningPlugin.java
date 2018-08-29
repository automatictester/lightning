package uk.co.automatictester.lightning.gradle.plugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import uk.co.automatictester.lightning.gradle.extension.LightningExtension;
import uk.co.automatictester.lightning.gradle.task.ReportTask;
import uk.co.automatictester.lightning.gradle.task.VerifyTask;

public class LightningPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        project.getExtensions()
                .create("lightningSettings", LightningExtension.class);
        project.getTasks()
                .create("verify", VerifyTask.class)
                .setDescription("Runs Lightning report on JMeter CSV file");
        project.getTasks()
                .create("report", ReportTask.class)
                .setDescription("Runs Lightning verify on JMeter CSV file, using XML file with test definitions");
    }
}
