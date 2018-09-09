package uk.co.automatictester.lightning.core.tests;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import uk.co.automatictester.lightning.core.enums.TestResult;
import uk.co.automatictester.lightning.core.state.data.JmeterTransactions;

import java.util.Arrays;
import java.util.List;

public class PassedTransactionsAbsoluteTest extends AbstractClientSideTest {

    private static final List<String> FIELDS_TO_EXCLUDE = Arrays.asList("failureCount", "transactionCount", "actualResultDescription", "result", "actualResult");
    private static final String TEST_TYPE = "passedTransactionsTest";
    private static final String EXPECTED_RESULT_MESSAGE = "Number of failed transactions <= %s";
    private static final String ACTUAL_RESULT_MESSAGE = "Number of failed transactions = %s";
    private final long allowedNumberOfFailedTransactions;
    private int failureCount;

    private PassedTransactionsAbsoluteTest(String testName, long allowedNumberOfFailedTransactions) {
        super(TEST_TYPE, testName);
        this.allowedNumberOfFailedTransactions = allowedNumberOfFailedTransactions;
        this.expectedResultDescription = String.format(EXPECTED_RESULT_MESSAGE, allowedNumberOfFailedTransactions);
    }

    @Override
    protected void calculateActualResult(JmeterTransactions jmeterTransactions) {
        failureCount = failureCount(jmeterTransactions);
        actualResult = failureCount;
    }

    @Override
    protected void calculateActualResultDescription() {
        actualResultDescription = String.format(ACTUAL_RESULT_MESSAGE, failureCount);
    }

    @Override
    protected void calculateTestResult() {
        result = (failureCount > allowedNumberOfFailedTransactions) ? TestResult.FAIL : TestResult.PASS;
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
        return String.format("Type: %s, name: %s, threshold: %d, transaction: %s, description: %s, regexp: %s", TEST_TYPE, name, allowedNumberOfFailedTransactions, transactionName, description, regexp);
    }

    public static class Builder {
        private String testName;
        private long allowedNumberOfFailedTransactions;
        private String description;
        private String transactionName;
        private boolean regexp = false;

        public Builder(String testName, long allowedNumberOfFailedTransactions) {
            this.testName = testName;
            this.allowedNumberOfFailedTransactions = allowedNumberOfFailedTransactions;
        }

        public PassedTransactionsAbsoluteTest.Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public PassedTransactionsAbsoluteTest.Builder withTransactionName(String transactionName) {
            this.transactionName = transactionName;
            return this;
        }

        public PassedTransactionsAbsoluteTest.Builder withRegexp() {
            this.regexp = true;
            return this;
        }

        public PassedTransactionsAbsoluteTest build() {
            PassedTransactionsAbsoluteTest test = new PassedTransactionsAbsoluteTest(testName, allowedNumberOfFailedTransactions);
            test.description = this.description;
            test.transactionName = this.transactionName;
            test.regexp = this.regexp;
            return test;
        }
    }
}
