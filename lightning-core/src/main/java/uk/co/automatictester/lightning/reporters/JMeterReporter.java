package uk.co.automatictester.lightning.reporters;

import uk.co.automatictester.lightning.data.JMeterTransactions;

public class JMeterReporter {

    private JMeterTransactions jmeterTransactions;

    public JMeterReporter(JMeterTransactions jmeterTransactions) {
        this.jmeterTransactions = jmeterTransactions;
    }

    public void printJMeterReport() {
        System.out.println(getJMeterReport());
    }

    public String getJMeterReport() {
        int transactionCount = jmeterTransactions.getTransactionCount();
        int failCount = jmeterTransactions.getFailCount();
        return String.format("Transactions executed: %d, failed: %d", transactionCount, failCount);
    }
}
