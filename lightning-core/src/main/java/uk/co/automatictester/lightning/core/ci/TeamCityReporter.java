package uk.co.automatictester.lightning.core.ci;

import uk.co.automatictester.lightning.core.data.JMeterTransactions;
import uk.co.automatictester.lightning.core.state.TestSet;
import uk.co.automatictester.lightning.core.tests.base.LightningTest;

public class TeamCityReporter extends CIReporter {

    private static final String TEAMCITY_BUILD_STATUS = "##teamcity[buildStatus text='%s']";
    private static final String TEAMCITY_BUILD_PROBLEM = "##teamcity[buildProblem description='%s']%n";
    private static final String TEAMCITY_STATISTICS = "##teamcity[buildStatisticValue key='%s' value='%s']%n";

    private TeamCityReporter(TestSet testSet) {
        super(testSet);
    }

    private TeamCityReporter(JMeterTransactions jmeterTransactions) {
        super(jmeterTransactions);
    }

    public static TeamCityReporter fromTestSet(TestSet testSet) {
        return new TeamCityReporter(testSet);
    }

    public static TeamCityReporter fromJMeterTransactions(JMeterTransactions jmeterTransactions) {
        return new TeamCityReporter(jmeterTransactions);
    }

    public String getTeamCityBuildReportSummary() {
        String outputTemplate = jmeterTransactions.getFailCount() > 0 ? TEAMCITY_BUILD_PROBLEM : TEAMCITY_BUILD_STATUS;
        String reportSummary = getReportSummary();
        return String.format(outputTemplate, reportSummary);
    }

    public String getTeamCityVerifyStatistics() {
        StringBuilder output = new StringBuilder();
        for (LightningTest test : testSet.getTests()) {
            String teamCityConsoleOutputEntry = String.format(TEAMCITY_STATISTICS, test.getName(), test.getActualResult());
            output.append(teamCityConsoleOutputEntry);
        }
        return output.toString();
    }

    public String getTeamCityReportStatistics() {
        String output = "";
        String failedTransactionsStats = String.format(TEAMCITY_STATISTICS, "Failed transactions", jmeterTransactions.getFailCount());
        output += failedTransactionsStats;
        String totalTransactionsStats = String.format(TEAMCITY_STATISTICS, "Total transactions", jmeterTransactions.size());
        output += totalTransactionsStats;
        return output;
    }
}
