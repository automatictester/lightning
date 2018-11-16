package uk.co.automatictester.lightning.core.reporters;

import org.testng.annotations.Test;
import uk.co.automatictester.lightning.core.state.data.JmeterTransactions;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

public class TransactionReporterTest {

    @Test
    public void testSummaryReport() {
        List<String[]> testData = new ArrayList<>();
        testData.add(new String[]{"Login", "421", "true", "1434291240000"});
        testData.add(new String[]{"Login", "500", "false", "1434291245000"});
        testData.add(new String[]{"Login", "345", "true", "1434291246000"});
        testData.add(new String[]{"Login", "650", "true", "1434291245000"});
        testData.add(new String[]{"Login", "721", "false", "1434291246000"});

        JmeterTransactions transactions = JmeterTransactions.fromList(testData);
        TransactionReporter reporter = new TransactionReporter(transactions);

        String output = reporter.summaryReport();
        assertThat(output, containsString("Transactions executed: 5, failed: 2"));
    }

    @Test
    public void testTeamCityBuildReportSummaryPassed() {
        List<String[]> testData = new ArrayList<>();
        testData.add(new String[]{"Login", "345", "true", "1434291246000"});
        testData.add(new String[]{"Login", "650", "true", "1434291245000"});
        testData.add(new String[]{"Login", "721", "true", "1434291246000"});

        JmeterTransactions transactions = JmeterTransactions.fromList(testData);
        TransactionReporter reporter = new TransactionReporter(transactions);

        assertThat(reporter.teamCityBuildReportSummary(), containsString("##teamcity[buildStatus text='Transactions executed: 3, failed: 0']"));
    }

    @Test
    public void testTeamCityBuildReportSummaryFailed() {
        List<String[]> testData = new ArrayList<>();
        testData.add(new String[]{"Login", "345", "false", "1434291246000"});
        testData.add(new String[]{"Login", "650", "true", "1434291245000"});
        testData.add(new String[]{"Login", "721", "false", "1434291246000"});

        JmeterTransactions transactions = JmeterTransactions.fromList(testData);
        TransactionReporter reporter = new TransactionReporter(transactions);

        assertThat(reporter.teamCityBuildReportSummary(), containsString("##teamcity[buildProblem description='Transactions executed: 3, failed: 2']"));
    }

}