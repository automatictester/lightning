package uk.co.automatictester.lightning.ci;

import org.testng.annotations.Test;
import uk.co.automatictester.lightning.TestSet;
import uk.co.automatictester.lightning.data.JMeterTransactions;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JenkinsReporterTest {

    @Test
    public void testSetJenkinsBuildName_verify() throws FileNotFoundException {
        TestSet testSet = mock(TestSet.class);
        when(testSet.getTestCount()).thenReturn(3);
        when(testSet.getFailCount()).thenReturn(0);
        when(testSet.getIgnoreCount()).thenReturn(0);

        new JenkinsReporter().setJenkinsBuildName(testSet);

        File lightningFile = new File("lightning-jenkins.properties");
        String text = new Scanner(lightningFile).useDelimiter("\\A").next();
        lightningFile.delete();

        assertThat(text, containsString("In Jenkins Build Name Setter Plugin, define build name as: ${PROPFILE,file=\"lightning-jenkins.properties\",property=\"result.string\"}"));
        assertThat(text, containsString("result.string=Tests executed\\: 3, failed\\: 0, ignored\\: 0"));
    }

    @Test
    public void testSetJenkinsBuildName_report() throws FileNotFoundException {
        JMeterTransactions jmeterTransactions = mock(JMeterTransactions.class);
        when(jmeterTransactions.getTransactionCount()).thenReturn(3);
        when(jmeterTransactions.getFailCount()).thenReturn(1);

        new JenkinsReporter().setJenkinsBuildName(jmeterTransactions);

        File lightningFile = new File("lightning-jenkins.properties");
        String text = new Scanner(lightningFile).useDelimiter("\\A").next();
        lightningFile.delete();

        assertThat(text, containsString("In Jenkins Build Name Setter Plugin, define build name as: ${PROPFILE,file=\"lightning-jenkins.properties\",property=\"result.string\"}"));
        assertThat(text, containsString("result.string=Transactions executed\\: 3, failed\\: 1"));
    }
}