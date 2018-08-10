package uk.co.automatictester.lightning.tests;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import uk.co.automatictester.lightning.data.JMeterTransactions;
import uk.co.automatictester.lightning.enums.TestResult;

import java.util.ArrayList;

public class ThroughputTest extends ClientSideTest {

    private static final String EXPECTED_RESULT_MESSAGE = "Throughput >= %.2f / second";
    private static final String ACTUAL_RESULT_MESSAGE = "Throughput = %.2f / second";

    private final double minThroughput;
    private double actualResult;

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

    private ThroughputTest(String testName, double minThroughput) {
        super("throughputTest", testName);
        this.minThroughput = minThroughput;
        expectedResultDescription = String.format(EXPECTED_RESULT_MESSAGE, minThroughput);
    }

    @Override
    public void execute(ArrayList<String[]> originalJMeterTransactions) {
        try {
            JMeterTransactions transactions = filterTransactions((JMeterTransactions) originalJMeterTransactions);
            transactionCount = transactions.size();
            actualResult = transactions.getThroughput();
            actualResultDescription = String.format(ACTUAL_RESULT_MESSAGE, actualResult);
            calculateTestResult();
        } catch (Exception e) {
            result = TestResult.ERROR;
            actualResultDescription = e.getMessage();
        }
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    private void calculateTestResult() {
        if (actualResult < minThroughput) {
            result = TestResult.FAIL;
        } else {
            result = TestResult.PASS;
        }
    }
}
