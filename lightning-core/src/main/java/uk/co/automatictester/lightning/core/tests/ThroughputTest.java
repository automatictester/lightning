package uk.co.automatictester.lightning.core.tests;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import uk.co.automatictester.lightning.core.enums.TestResult;
import uk.co.automatictester.lightning.core.state.data.JmeterTransactions;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ThroughputTest extends AbstractClientSideTest {

    private static final List<String> FIELDS_TO_EXCLUDE = Arrays.asList("actualResultDescription", "result", "actualResult");
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
    protected void calculateActualResult(JmeterTransactions transactions) {
        long firstTransactionTimestamp = transactions.firstTransactionTimestamp();
        long lastTransactionTimestamp = transactions.lastTransactionTimestamp();
        double transactionTimespanInMilliseconds = lastTransactionTimestamp - firstTransactionTimestamp;
        actualResult = transactionCount / (transactionTimespanInMilliseconds / 1000);
    }

    @Override
    protected void calculateTestResult() {
        result = (actualResult < minThroughput) ? TestResult.FAIL : TestResult.PASS;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj, FIELDS_TO_EXCLUDE);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, FIELDS_TO_EXCLUDE);
    }

    @Override
    public String toString() {
        Locale.setDefault(Locale.ENGLISH);
        NumberFormat formatter = new DecimalFormat("#0.00");
        return String.format("Type: %s, name: %s, threshold: %s, transaction: %s, description: %s, regexp: %s", TEST_TYPE, name, formatter.format(minThroughput), transactionName, description, regexp);
    }

    double getThroughput() {
        return actualResult;
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
