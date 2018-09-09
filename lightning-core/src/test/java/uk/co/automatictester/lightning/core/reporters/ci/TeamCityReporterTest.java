package uk.co.automatictester.lightning.core.reporters.ci;

import org.testng.annotations.Test;
import uk.co.automatictester.lightning.core.state.data.JmeterTransactions;
import uk.co.automatictester.lightning.core.state.tests.TestSet;
import uk.co.automatictester.lightning.core.tests.LightningTest;
import uk.co.automatictester.lightning.core.tests.PassedTransactionsAbsoluteTest;
import uk.co.automatictester.lightning.core.tests.AbstractServerSideTest;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TeamCityReporterTest {

    @Test
    public void testPrintTeamCityReportStatistics() {
        JmeterTransactions jmeterTransactions = mock(JmeterTransactions.class);
        when(jmeterTransactions.size()).thenReturn(1204);
        when(jmeterTransactions.failCount()).thenReturn(25);

        String output = TeamCityReporter.fromJmeterTransactions(jmeterTransactions).teamCityReportStatistics();
        assertThat(output, containsString("##teamcity[buildStatisticValue key='Failed transactions' value='25']"));
        assertThat(output, containsString("##teamcity[buildStatisticValue key='Total transactions' value='1204']"));
    }

    @Test
    public void testPrintTeamCityVerifyStatistics() {
        final PassedTransactionsAbsoluteTest clientTest = mock(PassedTransactionsAbsoluteTest.class);
        when(clientTest.name()).thenReturn("Failed transactions");
        when(clientTest.actualResult()).thenReturn(1);

        final AbstractServerSideTest serverTest = mock(AbstractServerSideTest.class);
        when(serverTest.name()).thenReturn("Memory utilization");
        when(serverTest.actualResult()).thenReturn(45);

        TestSet testSet = mock(TestSet.class);
        when(testSet.get()).thenReturn(new ArrayList<LightningTest>() {{
            add(clientTest);
            add(serverTest);
        }});

        String output = TeamCityReporter.fromTestSet(testSet).teamCityVerifyStatistics();
        assertThat(output, containsString("##teamcity[buildStatisticValue key='Failed transactions' value='1']"));
        assertThat(output, containsString("##teamcity[buildStatisticValue key='Memory utilization' value='45']"));
    }

    @Test
    public void testSetTeamCityBuildStatusTextTest_report_passed() {
        String expectedOutput = String.format("##teamcity[buildStatus text='Transactions executed: 10, failed: 0']");

        JmeterTransactions jmeterTransactions = mock(JmeterTransactions.class);
        when(jmeterTransactions.summaryReport()).thenReturn("Transactions executed: 10, failed: 0");
        when(jmeterTransactions.failCount()).thenReturn(0);

        String output = TeamCityReporter.fromJmeterTransactions(jmeterTransactions).teamCityBuildReportSummary();
        assertThat(output, containsString(expectedOutput));
    }

    @Test
    public void testSetTeamCityBuildStatusTextTest_report_failed() {
        String expectedOutput = String.format("##teamcity[buildProblem description='Transactions executed: 10, failed: 1']");

        JmeterTransactions jmeterTransactions = mock(JmeterTransactions.class);
        when(jmeterTransactions.summaryReport()).thenReturn("Transactions executed: 10, failed: 1");
        when(jmeterTransactions.failCount()).thenReturn(1);

        String output = TeamCityReporter.fromJmeterTransactions(jmeterTransactions).teamCityBuildReportSummary();
        assertThat(output, containsString(expectedOutput));
    }
}