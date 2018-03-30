package uk.co.automatictester.lightning.ci;

import org.testng.annotations.Test;
import uk.co.deliverymind.lightning.TestSet;
import uk.co.deliverymind.lightning.data.JMeterTransactions;
import uk.co.deliverymind.lightning.tests.ClientSideTest;
import uk.co.deliverymind.lightning.tests.PassedTransactionsTest;
import uk.co.deliverymind.lightning.tests.ServerSideTest;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TeamCityReporterTest
{

    @Test
    public void testPrintTeamCityReportStatistics() {
        JMeterTransactions jmeterTransactions = mock(JMeterTransactions.class);
        when(jmeterTransactions.getTransactionCount()).thenReturn(1204);
        when(jmeterTransactions.getFailCount()).thenReturn(25);

        String output = new TeamCityReporter(jmeterTransactions).getTeamCityReportStatistics();
        assertThat(output, containsString("##teamcity[buildStatisticValue key='Failed transactions' value='25']"));
        assertThat(output, containsString("##teamcity[buildStatisticValue key='Total transactions' value='1204']"));
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

        String output = new TeamCityReporter(testSet).getTeamCityVerifyStatistics();
        assertThat(output, containsString("##teamcity[buildStatisticValue key='Failed transactions' value='1']"));
        assertThat(output, containsString("##teamcity[buildStatisticValue key='Memory utilization' value='45']"));
    }

    @Test
    public void testSetTeamCityBuildStatusTextTest_report_passed() {
        String expectedOutput = String.format("##teamcity[buildStatus text='Transactions executed: 10, failed: 0']");

        JMeterTransactions jmeterTransactions = mock(JMeterTransactions.class);
        when(jmeterTransactions.getTransactionCount()).thenReturn(10);
        when(jmeterTransactions.getFailCount()).thenReturn(0);

        String output = new TeamCityReporter(jmeterTransactions).getTeamCityBuildStatusText();
        assertThat(output, containsString(expectedOutput));
    }

    @Test
    public void testSetTeamCityBuildStatusTextTest_report_failed() {
        String expectedOutput = String.format("##teamcity[buildProblem description='Transactions executed: 10, failed: 1']");

        JMeterTransactions jmeterTransactions = mock(JMeterTransactions.class);
        when(jmeterTransactions.getTransactionCount()).thenReturn(10);
        when(jmeterTransactions.getFailCount()).thenReturn(1);

        String output = new TeamCityReporter(jmeterTransactions).getTeamCityBuildStatusText();
        assertThat(output, containsString(expectedOutput));
    }
}