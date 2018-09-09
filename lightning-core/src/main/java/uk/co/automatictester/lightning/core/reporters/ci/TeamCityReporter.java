package uk.co.automatictester.lightning.core.reporters.ci;

import uk.co.automatictester.lightning.core.state.data.JmeterTransactions;
import uk.co.automatictester.lightning.core.state.tests.TestSet;

public class TeamCityReporter {

    private static final String TEAMCITY_BUILD_STATUS = "##teamcity[buildStatus text='%s']";
    private static final String TEAMCITY_BUILD_PROBLEM = "##teamcity[buildProblem description='%s']%n";
    private static final String TEAMCITY_STATISTICS = "##teamcity[buildStatisticValue key='%s' value='%s']%n";
    private TestSet testSet;
    private JmeterTransactions jmeterTransactions;

    private TeamCityReporter(TestSet testSet) {
        this.testSet = testSet;
    }

    private TeamCityReporter(JmeterTransactions jmeterTransactions) {
        this.jmeterTransactions = jmeterTransactions;
    }

    public static TeamCityReporter fromTestSet(TestSet testSet) {
        return new TeamCityReporter(testSet);
    }

    public static TeamCityReporter fromJmeterTransactions(JmeterTransactions jmeterTransactions) {
        return new TeamCityReporter(jmeterTransactions);
    }

    public String teamCityBuildReportSummary() {
        String outputTemplate = jmeterTransactions.failCount() > 0 ? TEAMCITY_BUILD_PROBLEM : TEAMCITY_BUILD_STATUS;
        String reportSummary = jmeterTransactions.summaryReport();
        return String.format(outputTemplate, reportSummary);
    }

    public String teamCityVerifyStatistics() {
        StringBuilder output = new StringBuilder();
        testSet.get().forEach(test -> {
            String teamCityConsoleOutputEntry = String.format(TEAMCITY_STATISTICS, test.name(), test.actualResult());
            output.append(teamCityConsoleOutputEntry);
        });
        return output.toString();
    }

    public String teamCityReportStatistics() {
        String output = "";
        String failedTransactionsStats = String.format(TEAMCITY_STATISTICS, "Failed transactions", jmeterTransactions.failCount());
        output += failedTransactionsStats;
        String totalTransactionsStats = String.format(TEAMCITY_STATISTICS, "Total transactions", jmeterTransactions.size());
        output += totalTransactionsStats;
        return output;
    }
}
