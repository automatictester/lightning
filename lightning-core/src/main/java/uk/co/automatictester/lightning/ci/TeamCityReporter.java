package uk.co.automatictester.lightning.ci;

import uk.co.automatictester.lightning.TestSet;
import uk.co.automatictester.lightning.data.JMeterTransactions;
import uk.co.automatictester.lightning.tests.ClientSideTest;
import uk.co.automatictester.lightning.tests.ServerSideTest;

public class TeamCityReporter extends CIReporter {

    private static final String TEAMCITY_BUILD_STATUS = "##teamcity[buildStatus text='%s']";

    private static final String TEAMCITY_BUILD_PROBLEM = "##teamcity[buildProblem description='%s']%n";

    private static final String TEAMCITY_STATISTICS = "##teamcity[buildStatisticValue key='%s' value='%s']%n";

    public TeamCityReporter(TestSet testSet) {
        super(testSet);
    }

    public TeamCityReporter(JMeterTransactions jMeterTransactions) {
        super(jMeterTransactions);
    }

    public TeamCityReporter printTeamCityBuildStatusText() {
        System.out.println(getTeamCityBuildStatusText());
        return this;
    }

    public String getTeamCityBuildStatusText() {
        String output = jmeterTransactions.getFailCount() > 0 ? TEAMCITY_BUILD_PROBLEM : TEAMCITY_BUILD_STATUS;
        return String.format(output, getReportSummary(jmeterTransactions));
    }

    public TeamCityReporter printTeamCityVerifyStatistics() {
        System.out.println(getTeamCityVerifyStatistics());
        return this;
    }

    public String getTeamCityVerifyStatistics() {
        StringBuilder output = new StringBuilder();
        for (ClientSideTest test : testSet.getClientSideTests()) {
            output.append(String.format(TEAMCITY_STATISTICS, test.getName(), test.getActualResult()));
        }
        if (testSet.getServerSideTests() != null) {
            for (ServerSideTest test : testSet.getServerSideTests()) {
                output.append(String.format(TEAMCITY_STATISTICS, test.getName(), test.getActualResult()));
            }
        }
        return output.toString();
    }

    public TeamCityReporter printTeamCityReportStatistics() {
        System.out.println(getTeamCityReportStatistics());
        return this;
    }

    public String getTeamCityReportStatistics() {
        String output = "";
        String failedTransactionsStats = String.format(TEAMCITY_STATISTICS, "Failed transactions", jmeterTransactions.getFailCount());
        output += failedTransactionsStats;
        String totalTransactionsStats = String.format(TEAMCITY_STATISTICS, "Total transactions", jmeterTransactions.getTransactionCount());
        output += totalTransactionsStats;
        return output;
    }
}
