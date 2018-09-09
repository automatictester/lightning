package uk.co.automatictester.lightning.core.reporters.jenkins;

import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

public class LocalFileSystemJenkinsReporterTest {

    @Test
    public void testStoreJenkinsBuildName() throws IOException {
        String report = "Transactions executed: 3, failed: 1";
        LocalFileSystemJenkinsReporter.storeJenkinsBuildName(report);

        String fileContent = readFileToStringAndDelete();
        assertThat(fileContent, containsString("In Jenkins Build Name Setter Plugin, define build name as: ${BUILD_NUMBER} - ${PROPFILE,file=\"lightning-jenkins.properties\",property=\"result.string\"}"));
        assertThat(fileContent, containsString("result.string=Transactions executed\\: 3, failed\\: 1"));
    }

    private String readFileToStringAndDelete() throws IOException {
        Path path = Paths.get("lightning-jenkins.properties");
        String fileContent = Files.lines(path).collect(Collectors.joining("\n"));
        Files.delete(path);
        return fileContent;
    }
}
