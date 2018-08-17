package uk.co.automatictester.lightning.core.tests;

import org.apache.commons.lang3.builder.EqualsBuilder;
import uk.co.automatictester.lightning.core.data.JMeterTransactions;
import uk.co.automatictester.lightning.core.enums.TestResult;
import uk.co.automatictester.lightning.core.tests.base.ClientSideTest;

public class PassedTransactionsAbsoluteTest extends ClientSideTest {

    private static final String TEST_TYPE = "passedTransactionsTest";
    private static final String EXPECTED_RESULT_MESSAGE = "Number of failed transactions <= %s";
    private static final String ACTUAL_RESULT_MESSAGE = "Number of failed transactions = %s";

    private long allowedNumberOfFailedTransactions;
    private int failureCount;

    private PassedTransactionsAbsoluteTest(String testName, long allowedNumberOfFailedTransactions) {
        super(TEST_TYPE, testName);
        this.allowedNumberOfFailedTransactions = allowedNumberOfFailedTransactions;
        this.expectedResultDescription = String.format(EXPECTED_RESULT_MESSAGE, allowedNumberOfFailedTransactions);
    }

    protected void calculateActualResult(JMeterTransactions jmeterTransactions) {
        failureCount = getFailureCount(jmeterTransactions);
        actualResult = failureCount;
    }

    protected void calculateActualResultDescription() {
        actualResultDescription = String.format(ACTUAL_RESULT_MESSAGE, failureCount);
    }

    protected void calculateTestResult() {
        if (failureCount > allowedNumberOfFailedTransactions) {
            result = TestResult.FAIL;
        } else {
            result = TestResult.PASS;
        }
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return TEST_TYPE.hashCode() + name.hashCode() + (int) allowedNumberOfFailedTransactions;
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
