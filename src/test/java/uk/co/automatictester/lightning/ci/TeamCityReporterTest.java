package uk.co.automatictester.lightning.ci;

import org.testng.annotations.Test;
import uk.co.automatictester.lightning.ConsoleOutputTest;
import uk.co.automatictester.lightning.TestSet;
import uk.co.automatictester.lightning.data.JMeterTransactions;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TeamCityReporterTest extends ConsoleOutputTest {

    @Test
    public void testSetTeamCityBuildStatusTextTest_verify_passed() {
        String expectedOutput = String.format("##teamcity[buildStatus text='Tests executed: 6, failed: 0, ignored: 0']%n");

        TestSet testSet = mock(TestSet.class);
        when(testSet.getTestCount()).thenReturn(6);
        when(testSet.getFailCount()).thenReturn(0);
        when(testSet.getIgnoreCount()).thenReturn(0);

        configureStream();
        new TeamCityReporter().setTeamCityBuildStatusText(testSet);
        assertThat(out.toString(), containsString(expectedOutput));
        revertStream();
    }

    @Test
    public void testSetTeamCityBuildStatusTextTest_verify_failed() {
        String expectedOutput = String.format("##teamcity[buildProblem description='Tests executed: 6, failed: 1, ignored: 0']%n");

        TestSet testSet = mock(TestSet.class);
        when(testSet.getTestCount()).thenReturn(6);
        when(testSet.getFailCount()).thenReturn(1);
        when(testSet.getIgnoreCount()).thenReturn(0);

        configureStream();
        new TeamCityReporter().setTeamCityBuildStatusText(testSet);
        assertThat(out.toString(), containsString(expectedOutput));
        revertStream();
    }

    @Test
    public void testSetTeamCityBuildStatusTextTest_verify_ignored() {
        String expectedOutput = String.format("##teamcity[buildProblem description='Tests executed: 6, failed: 0, ignored: 1']%n");

        TestSet testSet = mock(TestSet.class);
        when(testSet.getTestCount()).thenReturn(6);
        when(testSet.getFailCount()).thenReturn(0);
        when(testSet.getIgnoreCount()).thenReturn(1);

        configureStream();
        new TeamCityReporter().setTeamCityBuildStatusText(testSet);
        assertThat(out.toString(), containsString(expectedOutput));
        revertStream();
    }

    @Test
    public void testSetTeamCityBuildStatusTextTest_report_passed() {
        String expectedOutput = String.format("##teamcity[buildStatus text='Transactions executed: 10, failed: 0']%n");

        JMeterTransactions jmeterTransactions = mock(JMeterTransactions.class);
        when(jmeterTransactions.getTransactionCount()).thenReturn(10);
        when(jmeterTransactions.getFailCount()).thenReturn(0);

        configureStream();
        new TeamCityReporter().setTeamCityBuildStatusText(jmeterTransactions);
        assertThat(out.toString(), containsString(expectedOutput));
        revertStream();
    }

    @Test
    public void testSetTeamCityBuildStatusTextTest_report_failed() {
        String expectedOutput = String.format("##teamcity[buildProblem description='Transactions executed: 10, failed: 1']%n");

        JMeterTransactions jmeterTransactions = mock(JMeterTransactions.class);
        when(jmeterTransactions.getTransactionCount()).thenReturn(10);
        when(jmeterTransactions.getFailCount()).thenReturn(1);

        configureStream();
        new TeamCityReporter().setTeamCityBuildStatusText(jmeterTransactions);
        assertThat(out.toString(), containsString(expectedOutput));
        revertStream();
    }
}