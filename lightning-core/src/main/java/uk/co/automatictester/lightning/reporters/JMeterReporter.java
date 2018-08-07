package uk.co.automatictester.lightning.reporters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.automatictester.lightning.data.JMeterTransactions;

public class JMeterReporter {

    private static final Logger logger = LoggerFactory.getLogger(JMeterReporter.class);

    public static void printJMeterReport(JMeterTransactions jmeterTransactions) {
        String report = getJMeterReport(jmeterTransactions);
        logger.info(report);
    }

    public static String getJMeterReport(JMeterTransactions jmeterTransactions) {
        int transactionCount = jmeterTransactions.size();
        int failCount = jmeterTransactions.getFailCount();
        return String.format("Transactions executed: %d, failed: %d", transactionCount, failCount);
    }
}
