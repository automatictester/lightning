package uk.co.automatictester.lightning.core.ci;

import uk.co.automatictester.lightning.core.state.TestSet;
import uk.co.automatictester.lightning.core.data.JmeterTransactions;

public abstract class CiReporter {

    protected TestSet testSet;
    protected JmeterTransactions jmeterTransactions;

    protected CiReporter(TestSet testSet) {
        this.testSet = testSet;
    }

    protected CiReporter(JmeterTransactions jmeterTransactions) {
        this.jmeterTransactions = jmeterTransactions;
    }

    protected String getReportSummary() {
        int executed = jmeterTransactions.size();
        int failed = jmeterTransactions.getFailCount();
        return String.format("Transactions executed: %s, failed: %s", executed, failed);
    }
}
