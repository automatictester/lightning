package uk.co.automatictester.lightning.tests;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import uk.co.automatictester.lightning.data.JMeterTransactions;
import uk.co.automatictester.lightning.enums.TestResult;
import uk.co.automatictester.lightning.tests.base.ClientSideTest;

public class ThroughputTest extends ClientSideTest {

    private static final String EXPECTED_RESULT_MESSAGE = "Throughput >= %.2f / second";
    private static final String ACTUAL_RESULT_MESSAGE = "Throughput = %.2f / second";
    private final double minThroughput;
    private double actualResult;

    private ThroughputTest(String testName, double minThroughput) {
        super("throughputTest", testName);
        this.minThroughput = minThroughput;
        expectedResultDescription = String.format(EXPECTED_RESULT_MESSAGE, minThroughput);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    public void calculateActualResultDescription() {
        actualResultDescription = String.format(ACTUAL_RESULT_MESSAGE, actualResult);
    }

    protected void calculateActualResult(JMeterTransactions transactions) {
        long firstTransactionTimestamp = transactions.getFirstTransactionTimestamp();
        long lastTransactionTimestamp = transactions.getLastTransactionTimestamp();
        double transactionTimespanInMilliseconds = lastTransactionTimestamp - firstTransactionTimestamp;
        actualResult = transactionCount / (transactionTimespanInMilliseconds / 1000);
    }

    public double getThroughput() {
        return actualResult;
    }

    protected void calculateTestResult() {
        if (actualResult < minThroughput) {
            result = TestResult.FAIL;
        } else {
            result = TestResult.PASS;
        }
    }

    public static class Builder {
        private String testName;
        private double minThroughput;
        private String description;
        private String transactionName;
        private boolean regexp = false;

        public Builder(String testName, double minThroughput) {
            this.testName = testName;
            this.minThroughput = minThroughput;
        }

        public ThroughputTest.Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public ThroughputTest.Builder withTransactionName(String transactionName) {
            this.transactionName = transactionName;
            return this;
        }

        public ThroughputTest.Builder withRegexp() {
            this.regexp = true;
            return this;
        }

        public ThroughputTest build() {
            ThroughputTest test = new ThroughputTest(testName, minThroughput);
            test.description = this.description;
            test.transactionName = this.transactionName;
            test.regexp = this.regexp;
            return test;
        }
    }
}
