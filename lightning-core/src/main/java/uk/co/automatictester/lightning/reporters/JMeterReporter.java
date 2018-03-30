package uk.co.automatictester.lightning.reporters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.deliverymind.lightning.data.JMeterTransactions;

public class JMeterReporter {

    private JMeterTransactions jmeterTransactions;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public JMeterReporter(JMeterTransactions jmeterTransactions) {
        this.jmeterTransactions = jmeterTransactions;
    }

    public void printJMeterReport() {
        logger.info(getJMeterReport());
    }

    public String getJMeterReport() {
        int transactionCount = jmeterTransactions.getTransactionCount();
        int failCount = jmeterTransactions.getFailCount();
        return String.format("Transactions executed: %d, failed: %d", transactionCount, failCount);
    }
}
