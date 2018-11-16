package uk.co.automatictester.lightning.core.reporters;

import uk.co.automatictester.lightning.core.state.data.JmeterTransactions;

public class TransactionReporter {

    private static final String TEAMCITY_BUILD_STATUS = "##teamcity[buildStatus text='%s']";
    private static final String TEAMCITY_BUILD_PROBLEM = "##teamcity[buildProblem description='%s']%n";
    private static final String TEAMCITY_STATISTICS = "##teamcity[buildStatisticValue key='%s' value='%s']%n";
    private JmeterTransactions transactions;

    public TransactionReporter(JmeterTransactions transactions) {
        this.transactions = transactions;
    }

    public String teamCityBuildReportSummary() {
        String outputTemplate = transactions.failCount() > 0 ? TEAMCITY_BUILD_PROBLEM : TEAMCITY_BUILD_STATUS;
        return String.format(outputTemplate, summaryReport());
    }

    public String summaryReport() {
        return String.format("Transactions executed: %d, failed: %d", transactions.size(), transactions.failCount());
    }

    public String teamCityReportStatistics() {
        String failedTransactionsStats = String.format(TEAMCITY_STATISTICS, "Failed transactions", transactions.failCount());
        String totalTransactionsStats = String.format(TEAMCITY_STATISTICS, "Total transactions", transactions.size());
        return String.format("%s%s", failedTransactionsStats, totalTransactionsStats);
    }
}
