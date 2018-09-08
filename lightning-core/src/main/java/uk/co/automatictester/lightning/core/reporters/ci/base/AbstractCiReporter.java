package uk.co.automatictester.lightning.core.reporters.ci.base;

import uk.co.automatictester.lightning.core.state.data.JmeterTransactions;
import uk.co.automatictester.lightning.core.state.tests.results.LightningTestSetResult;

public abstract class AbstractCiReporter {

    protected LightningTestSetResult testSet;
    protected JmeterTransactions jmeterTransactions;

    protected AbstractCiReporter(LightningTestSetResult testSet) {
        this.testSet = testSet;
    }

    protected AbstractCiReporter(JmeterTransactions jmeterTransactions) {
        this.jmeterTransactions = jmeterTransactions;
    }

    protected String reportSummary() {
        int executed = jmeterTransactions.size();
        int failed = jmeterTransactions.failCount();
        return String.format("Transactions executed: %s, failed: %s", executed, failed);
    }
}
