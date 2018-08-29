package uk.co.automatictester.lightning.core.ci;

import org.testng.annotations.Test;
import uk.co.automatictester.lightning.core.data.JMeterTransactions;
import uk.co.automatictester.lightning.core.state.TestSet;
import uk.co.automatictester.lightning.core.tests.PassedTransactionsAbsoluteTest;
import uk.co.automatictester.lightning.core.tests.base.LightningTest;
import uk.co.automatictester.lightning.core.tests.base.ServerSideTest;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TeamCityReporterTest {

    @Test
    public void testPrintTeamCityReportStatistics() {
        JMeterTransactions jmeterTransactions = mock(JMeterTransactions.class);
        when(jmeterTransactions.size()).thenReturn(1204);
        when(jmeterTransactions.getFailCount()).thenReturn(25);

        String output = TeamCityReporter.fromJmeterTransactions(jmeterTransactions).getTeamCityReportStatistics();
        assertThat(output, containsString("##teamcity[buildStatisticValue key='Failed transactions' value='25']"));
        assertThat(output, containsString("##teamcity[buildStatisticValue key='Total transactions' value='1204']"));
    }

    @Test
    public void testPrintTeamCityVerifyStatistics() {
        final PassedTransactionsAbsoluteTest clientTest = mock(PassedTransactionsAbsoluteTest.class);
        when(clientTest.getName()).thenReturn("Failed transactions");
        when(clientTest.getActualResult()).thenReturn(1);

        final ServerSideTest serverTest = mock(ServerSideTest.class);
        when(serverTest.getName()).thenReturn("Memory utilization");
        when(serverTest.getActualResult()).thenReturn(45);

        TestSet testSet = mock(TestSet.class);
        when(testSet.getTests()).thenReturn(new ArrayList<LightningTest>() {{
            add(clientTest);
            add(serverTest);
        }});

        String output = TeamCityReporter.fromTestSet(testSet).getTeamCityVerifyStatistics();
        assertThat(output, containsString("##teamcity[buildStatisticValue key='Failed transactions' value='1']"));
        assertThat(output, containsString("##teamcity[buildStatisticValue key='Memory utilization' value='45']"));
    }

    @Test
    public void testSetTeamCityBuildStatusTextTest_report_passed() {
        String expectedOutput = String.format("##teamcity[buildStatus text='Transactions executed: 10, failed: 0']");

        JMeterTransactions jmeterTransactions = mock(JMeterTransactions.class);
        when(jmeterTransactions.size()).thenReturn(10);
        when(jmeterTransactions.getFailCount()).thenReturn(0);

        String output = TeamCityReporter.fromJmeterTransactions(jmeterTransactions).getTeamCityBuildReportSummary();
        assertThat(output, containsString(expectedOutput));
    }

    @Test
    public void testSetTeamCityBuildStatusTextTest_report_failed() {
        String expectedOutput = String.format("##teamcity[buildProblem description='Transactions executed: 10, failed: 1']");

        JMeterTransactions jmeterTransactions = mock(JMeterTransactions.class);
        when(jmeterTransactions.size()).thenReturn(10);
        when(jmeterTransactions.getFailCount()).thenReturn(1);

        String output = TeamCityReporter.fromJmeterTransactions(jmeterTransactions).getTeamCityBuildReportSummary();
        assertThat(output, containsString(expectedOutput));
    }
}