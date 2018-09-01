package uk.co.automatictester.lightning.core.reporters;

import uk.co.automatictester.lightning.core.data.JmeterTransactions;

public class JmeterReporter {

    private JmeterReporter() {
    }

    public static String getJMeterReport(JmeterTransactions jmeterTransactions) {
        int transactionCount = jmeterTransactions.size();
        int failCount = jmeterTransactions.getFailCount();
        return String.format("Transactions executed: %d, failed: %d", transactionCount, failCount);
    }
}
