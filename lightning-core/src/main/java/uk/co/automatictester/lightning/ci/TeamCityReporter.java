package uk.co.automatictester.lightning.ci;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.automatictester.lightning.TestSet;
import uk.co.automatictester.lightning.data.JMeterTransactions;
import uk.co.automatictester.lightning.tests.base.LightningTest;

import java.util.Arrays;

public class TeamCityReporter extends CIReporter {

    private static final String TEAMCITY_BUILD_STATUS = "##teamcity[buildStatus text='%s']";
    private static final String TEAMCITY_BUILD_PROBLEM = "##teamcity[buildProblem description='%s']%n";
    private static final String TEAMCITY_STATISTICS = "##teamcity[buildStatisticValue key='%s' value='%s']%n";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

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

    public TeamCityReporter printTeamCityBuildReportSummary() {
        log(getTeamCityBuildReportSummary());
        return this;
    }

    public String getTeamCityBuildReportSummary() {
        String outputTemplate = jmeterTransactions.getFailCount() > 0 ? TEAMCITY_BUILD_PROBLEM : TEAMCITY_BUILD_STATUS;
        String reportSummary = getReportSummary();
        return String.format(outputTemplate, reportSummary);
    }

    public TeamCityReporter printTeamCityVerifyStatistics() {
        log(getTeamCityVerifyStatistics());
        return this;
    }

    public String getTeamCityVerifyStatistics() {
        StringBuilder output = new StringBuilder();
        for (LightningTest test : testSet.getTests()) {
            String teamCityConsoleOutputEntry = String.format(TEAMCITY_STATISTICS, test.getName(), test.getActualResult());
            output.append(teamCityConsoleOutputEntry);
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
        String totalTransactionsStats = String.format(TEAMCITY_STATISTICS, "Total transactions", jmeterTransactions.size());
        output += totalTransactionsStats;
        return output;
    }

    private void log(String output) {
        for (String line : Arrays.asList(output.split(System.lineSeparator()))) {
            logger.info(line);
        }
    }
}
