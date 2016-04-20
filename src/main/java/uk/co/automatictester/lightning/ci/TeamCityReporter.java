package uk.co.automatictester.lightning.ci;

import uk.co.automatictester.lightning.TestSet;
import uk.co.automatictester.lightning.data.JMeterTransactions;
import uk.co.automatictester.lightning.tests.ClientSideTest;
import uk.co.automatictester.lightning.tests.ServerSideTest;

public class TeamCityReporter extends CIReporter {

    private static final String TEAMCITY_BUILD_STATUS = "%n======= ENHANCED TEAMCITY INTEGRATION =======%n##teamcity[buildStatus text='%s']";

    private static final String TEAMCITY_BUILD_PROBLEM = "%n======= ENHANCED TEAMCITY INTEGRATION =======%n##teamcity[buildProblem description='%s']";

    private static final String TEAMCITY_STATISTICS = "##teamcity[buildStatisticValue key='%s' value='%s']";

    public TeamCityReporter(TestSet testSet) {
        super(testSet);
    }

    public TeamCityReporter(JMeterTransactions jMeterTransactions) {
        super(jMeterTransactions);
    }

    public TeamCityReporter setTeamCityVerifyBuildStatusText() {
        String teamCityOutput = (testSet.getFailCount() + testSet.getIgnoreCount()) > 0 ? TEAMCITY_BUILD_PROBLEM : TEAMCITY_BUILD_STATUS;
        System.out.println(String.format(teamCityOutput, getVerifySummary(testSet)));
        return this;
    }

    public TeamCityReporter setTeamCityReportBuildStatusText() {
        String teamCityOutput = jmeterTransactions.getFailCount() > 0 ? TEAMCITY_BUILD_PROBLEM : TEAMCITY_BUILD_STATUS;
        System.out.println(String.format(teamCityOutput, getReportSummary(jmeterTransactions)));
        return this;
    }

    public TeamCityReporter printTeamCityVerifyStatistics() {
        for (ClientSideTest test : testSet.getClientSideTests()) {
            String statsEntry = String.format(TEAMCITY_STATISTICS, test.getName(), test.getActualResult());
            System.out.println(statsEntry);
        }
        if (testSet.getServerSideTests() != null) {
            for (ServerSideTest test : testSet.getServerSideTests()) {
                String statsEntry = String.format(TEAMCITY_STATISTICS, test.getName(), test.getActualResult());
                System.out.println(statsEntry);
            }
        }
        return this;
    }

    public TeamCityReporter printTeamCityReportStatistics() {
        String failedTransactionsStats = String.format(TEAMCITY_STATISTICS, "Failed transactions", jmeterTransactions.getFailCount());
        System.out.println(failedTransactionsStats);
        String totalTransactionsStats = String.format(TEAMCITY_STATISTICS, "Total transactions", jmeterTransactions.getTransactionCount());
        System.out.println(totalTransactionsStats);
        return this;
    }
}
