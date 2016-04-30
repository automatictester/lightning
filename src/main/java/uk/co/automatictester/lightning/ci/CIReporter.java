package uk.co.automatictester.lightning.ci;

import uk.co.automatictester.lightning.TestSet;
import uk.co.automatictester.lightning.data.JMeterTransactions;

public abstract class CIReporter {

    protected TestSet testSet;
    protected JMeterTransactions jmeterTransactions;

    protected CIReporter(TestSet testSet) {
        this.testSet = testSet;
    }

    protected CIReporter(JMeterTransactions jmeterTransactions) {
        this.jmeterTransactions = jmeterTransactions;
    }

    public static String getVerifySummary(TestSet testSet) {
        int executed = testSet.getTestCount();
        int failed = testSet.getFailCount();
        int ignored = testSet.getErrorCount();
        return String.format("Tests executed: %s, failed: %s, ignored: %s", executed, failed, ignored);
    }

    public static String getReportSummary(JMeterTransactions jmeterTransactions) {
        int executed = jmeterTransactions.getTransactionCount();
        int failed = jmeterTransactions.getFailCount();
        return String.format("Transactions executed: %s, failed: %s", executed, failed);
    }

}
