package uk.co.automatictester.lightning.ci;

import org.testng.annotations.Test;
import uk.co.automatictester.lightning.ConsoleOutputTest;
import uk.co.automatictester.lightning.TestSet;
import uk.co.automatictester.lightning.data.JMeterTransactions;
import uk.co.automatictester.lightning.tests.ClientSideTest;
import uk.co.automatictester.lightning.tests.PassedTransactionsTest;
import uk.co.automatictester.lightning.tests.ServerSideTest;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TeamCityReporterTest extends ConsoleOutputTest {

    @Test
    public void testPrintTeamCityReportStatistics() {
        JMeterTransactions jmeterTransactions = mock(JMeterTransactions.class);
        when(jmeterTransactions.getTransactionCount()).thenReturn(1204);
        when(jmeterTransactions.getFailCount()).thenReturn(25);

        configureStream();
        new TeamCityReporter(jmeterTransactions).printTeamCityReportStatistics();
        assertThat(out.toString(), containsString("##teamcity[buildStatisticValue key='Failed transactions' value='25']"));
        assertThat(out.toString(), containsString("##teamcity[buildStatisticValue key='Total transactions' value='1204']"));
        revertStream();
    }

    @Test
    public void testPrintTeamCityVerifyStatistics() {
        final PassedTransactionsTest clientTest = mock(PassedTransactionsTest.class);
        when(clientTest.getName()).thenReturn("Failed transactions");
        when(clientTest.getActualResult()).thenReturn(1);

        final ServerSideTest serverTest = mock(ServerSideTest.class);
        when(serverTest.getName()).thenReturn("Memory utilization");
        when(serverTest.getActualResult()).thenReturn(45);

        TestSet testSet = mock(TestSet.class);
        when(testSet.getClientSideTests()).thenReturn(new ArrayList<ClientSideTest>() {{ add(clientTest); }});
        when(testSet.getServerSideTests()).thenReturn(new ArrayList<ServerSideTest>() {{ add(serverTest); }});

        configureStream();
        new TeamCityReporter(testSet).printTeamCityVerifyStatistics();
        assertThat(out.toString(), containsString("##teamcity[buildStatisticValue key='Failed transactions' value='1']"));
        assertThat(out.toString(), containsString("##teamcity[buildStatisticValue key='Memory utilization' value='45']"));
        revertStream();
    }

    @Test
    public void testSetTeamCityBuildStatusTextTest_verify_passed() {
        String expectedOutput = String.format("##teamcity[buildStatus text='Tests executed: 6, failed: 0, ignored: 0']%n");

        TestSet testSet = mock(TestSet.class);
        when(testSet.getTestCount()).thenReturn(6);
        when(testSet.getFailCount()).thenReturn(0);
        when(testSet.getIgnoreCount()).thenReturn(0);

        configureStream();
        new TeamCityReporter(testSet).setTeamCityVerifyBuildStatusText();
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
        new TeamCityReporter(testSet).setTeamCityVerifyBuildStatusText();
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
        new TeamCityReporter(testSet).setTeamCityVerifyBuildStatusText();
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
        new TeamCityReporter(jmeterTransactions).setTeamCityReportBuildStatusText();
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
        new TeamCityReporter(jmeterTransactions).setTeamCityReportBuildStatusText();
        assertThat(out.toString(), containsString(expectedOutput));
        revertStream();
    }
}