package uk.co.automatictester.lightning.core.reporters.jenkins;

import uk.co.automatictester.lightning.core.exceptions.JenkinsReportGenerationException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class LocalFileSystemJenkinsReporter {

    private LocalFileSystemJenkinsReporter() {
    }

    public static void storeJenkinsBuildName(String report) {
        try (FileOutputStream fos = new FileOutputStream("lightning-jenkins.properties")) {
            Properties props = new Properties();
            props.setProperty("result.string", report);
            OutputStreamWriter out = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
            props.store(out, "In Jenkins Build Name Setter Plugin, define build name as: ${BUILD_NUMBER} - ${PROPFILE,file=\"lightning-jenkins.properties\",property=\"result.string\"}");
        } catch (IOException e) {
            throw new JenkinsReportGenerationException(e);
        }
    }
}
