package uk.co.automatictester.lightning.core.ci;

import org.testng.annotations.Test;
import uk.co.automatictester.lightning.core.data.JMeterTransactions;
import uk.co.automatictester.lightning.core.state.TestSet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JenkinsReporterTest {

    @Test
    public void testSetJenkinsBuildName_verify() throws IOException {
        TestSet testSet = mock(TestSet.class);
        when(testSet.getTestCount()).thenReturn(3);
        when(testSet.getFailCount()).thenReturn(1);
        when(testSet.getErrorCount()).thenReturn(1);

        JenkinsReporter.fromTestSet(testSet).setJenkinsBuildName();

        String fileContent = readFileToStringAndDelete();
        assertThat(fileContent, containsString("In Jenkins Build Name Setter Plugin, define build name as: ${BUILD_NUMBER} - ${PROPFILE,file=\"lightning-jenkins.properties\",property=\"result.string\"}"));
        assertThat(fileContent, containsString("result.string=Tests executed\\: 3, failed\\: 2"));
    }

    @Test
    public void testSetJenkinsBuildName_report() throws IOException {
        JMeterTransactions jmeterTransactions = mock(JMeterTransactions.class);
        when(jmeterTransactions.size()).thenReturn(3);
        when(jmeterTransactions.getFailCount()).thenReturn(1);

        JenkinsReporter.fromJMeterTransactions(jmeterTransactions).setJenkinsBuildName();

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