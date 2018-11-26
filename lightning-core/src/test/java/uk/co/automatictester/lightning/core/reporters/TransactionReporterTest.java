package uk.co.automatictester.lightning.core.reporters;

import org.testng.annotations.Test;
import uk.co.automatictester.lightning.core.state.data.JmeterTransactions;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TransactionReporterTest {

    @Test
    public void testSummaryReport() {
        JmeterTransactions transactions = mock(JmeterTransactions.class);
        when(transactions.size()).thenReturn(5);
        when(transactions.failCount()).thenReturn(2);

        TransactionReporter reporter = new TransactionReporter(transactions);

        String output = reporter.summaryReport();
        assertThat(output, containsString("Transactions executed: 5, failed: 2"));
    }

    @Test
    public void testTeamCityBuildReportSummaryPassed() {
        JmeterTransactions transactions = mock(JmeterTransactions.class);
        when(transactions.size()).thenReturn(3);
        when(transactions.failCount()).thenReturn(0);

        TransactionReporter reporter = new TransactionReporter(transactions);

        assertThat(reporter.teamCityBuildReportSummary(), containsString("##teamcity[buildStatus text='Transactions executed: 3, failed: 0']"));
    }

    @Test
    public void testTeamCityBuildReportSummaryFailed() {
        JmeterTransactions transactions = mock(JmeterTransactions.class);
        when(transactions.size()).thenReturn(3);
        when(transactions.failCount()).thenReturn(2);

        TransactionReporter reporter = new TransactionReporter(transactions);

        assertThat(reporter.teamCityBuildReportSummary(), containsString("##teamcity[buildProblem description='Transactions executed: 3, failed: 2']"));
    }

    @Test
    public void testTeamCityReportStatistics() {
        JmeterTransactions transactions = mock(JmeterTransactions.class);
        when(transactions.size()).thenReturn(1000);
        when(transactions.failCount()).thenReturn(23);

        TransactionReporter reporter = new TransactionReporter(transactions);

        assertThat(reporter.teamCityReportStatistics(), containsString("##teamcity[buildStatisticValue key='Failed transactions' value='23']\n"));
        assertThat(reporter.teamCityReportStatistics(), containsString("##teamcity[buildStatisticValue key='Total transactions' value='1000']"));
    }
}
