package uk.co.automatictester.lightning.core.reporters;

import uk.co.automatictester.lightning.core.data.JMeterTransactions;

public class JMeterReporter {

    private JMeterReporter() {
    }

    public static String getJMeterReport(JMeterTransactions jmeterTransactions) {
        int transactionCount = jmeterTransactions.size();
        int failCount = jmeterTransactions.getFailCount();
        return String.format("Transactions executed: %d, failed: %d", transactionCount, failCount);
    }
}
