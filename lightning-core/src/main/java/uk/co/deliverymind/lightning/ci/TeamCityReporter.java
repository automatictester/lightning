package uk.co.deliverymind.lightning.ci;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.deliverymind.lightning.TestSet;
import uk.co.deliverymind.lightning.data.JMeterTransactions;
import uk.co.deliverymind.lightning.tests.ClientSideTest;
import uk.co.deliverymind.lightning.tests.ServerSideTest;

import java.util.Arrays;

public class TeamCityReporter extends CIReporter {

    private static final String TEAMCITY_BUILD_STATUS = "##teamcity[buildStatus text='%s']";
    private static final String TEAMCITY_BUILD_PROBLEM = "##teamcity[buildProblem description='%s']%n";
    private static final String TEAMCITY_STATISTICS = "##teamcity[buildStatisticValue key='%s' value='%s']%n";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public TeamCityReporter(TestSet testSet) {
        super(testSet);
    }

    public TeamCityReporter(JMeterTransactions jMeterTransactions) {
        super(jMeterTransactions);
    }

    public TeamCityReporter printTeamCityBuildStatusText() {
        log(getTeamCityBuildStatusText());
        return this;
    }

    public String getTeamCityBuildStatusText() {
        String output = jmeterTransactions.getFailCount() > 0 ? TEAMCITY_BUILD_PROBLEM : TEAMCITY_BUILD_STATUS;
        return String.format(output, getReportSummary(jmeterTransactions));
    }

    public TeamCityReporter printTeamCityVerifyStatistics() {
        log(getTeamCityVerifyStatistics());
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
        log(getTeamCityReportStatistics());
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

    private void log(String output) {
        for (String line : Arrays.asList(output.split(System.lineSeparator()))) {
            logger.info(line);
        }
    }
}
