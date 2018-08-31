package uk.co.automatictester.lightning.core.tests;

import org.apache.commons.lang3.builder.EqualsBuilder;
import uk.co.automatictester.lightning.core.data.JMeterTransactions;
import uk.co.automatictester.lightning.core.enums.TestResult;
import uk.co.automatictester.lightning.core.tests.base.ClientSideTest;

import java.util.Arrays;
import java.util.List;

public class ThroughputTest extends ClientSideTest {

    private static final String TEST_TYPE = "throughputTest";
    private static final String EXPECTED_RESULT_MESSAGE = "Throughput >= %.2f / second";
    private static final String ACTUAL_RESULT_MESSAGE = "Throughput = %.2f / second";
    private final double minThroughput;
    private double actualResult;

    private ThroughputTest(String testName, double minThroughput) {
        super(TEST_TYPE, testName);
        this.minThroughput = minThroughput;
        expectedResultDescription = String.format(EXPECTED_RESULT_MESSAGE, minThroughput);
    }

    @Override
    public void calculateActualResultDescription() {
        actualResultDescription = String.format(ACTUAL_RESULT_MESSAGE, actualResult);
    }

    @Override
    protected void calculateActualResult(JMeterTransactions transactions) {
        long firstTransactionTimestamp = transactions.getFirstTransactionTimestamp();
        long lastTransactionTimestamp = transactions.getLastTransactionTimestamp();
        double transactionTimespanInMilliseconds = lastTransactionTimestamp - firstTransactionTimestamp;
        actualResult = transactionCount / (transactionTimespanInMilliseconds / 1000);
    }

    @Override
    protected void calculateTestResult() {
        result = (actualResult < minThroughput) ? TestResult.FAIL : TestResult.PASS;
    }

    double getThroughput() {
        return actualResult;
    }

    @Override
    public boolean equals(Object obj) {
        List<String> fieldsToExclude = Arrays.asList("actualResultDescription", "result", "actualResult");
        return EqualsBuilder.reflectionEquals(this, obj, fieldsToExclude);
    }

    @Override
    public int hashCode() {
        return TEST_TYPE.hashCode() + name.hashCode() + (int) minThroughput;
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
