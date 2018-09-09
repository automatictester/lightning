package uk.co.automatictester.lightning.core.tests;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import uk.co.automatictester.lightning.core.enums.TestResult;
import uk.co.automatictester.lightning.core.state.data.JmeterTransactions;
import uk.co.automatictester.lightning.core.utils.Percent;

import java.util.Arrays;
import java.util.List;

public class PassedTransactionsRelativeTest extends AbstractClientSideTest {

    private static final List<String> FIELDS_TO_EXCLUDE = Arrays.asList("transactionCount", "actualResultDescription", "result", "actualResult");
    private static final String TEST_TYPE = "passedTransactionsTest";
    private static final String EXPECTED_RESULT_MESSAGE = "Percent of failed transactions <= %s";
    private static final String ACTUAL_RESULT_MESSAGE = "Percent of failed transactions = %s";
    private final Percent allowedPercentOfFailedTransactions;

    private PassedTransactionsRelativeTest(String testName, Percent percent) {
        super(TEST_TYPE, testName);
        this.allowedPercentOfFailedTransactions = percent;
        this.expectedResultDescription = String.format(EXPECTED_RESULT_MESSAGE, allowedPercentOfFailedTransactions.value());
    }

    @Override
    protected void calculateActualResult(JmeterTransactions jmeterTransactions) {
        int failureCount = failureCount(jmeterTransactions);
        actualResult = (int) (((float) failureCount / transactionCount) * 100);
    }

    @Override
    protected void calculateActualResultDescription() {
        actualResultDescription = String.format(ACTUAL_RESULT_MESSAGE, actualResult);
    }

    @Override
    protected void calculateTestResult() {
        result = (actualResult > (float) allowedPercentOfFailedTransactions.value()) ? TestResult.FAIL : TestResult.PASS;
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
        return String.format("Type: %s, name: %s, threshold: %d, transaction: %s, description: %s, regexp: %s", TEST_TYPE, name, allowedPercentOfFailedTransactions.value(), transactionName, description, regexp);
    }

    public static class Builder {
        private String testName;
        private Percent allowedPercentOfFailedTransactions;
        private String description;
        private String transactionName;
        private boolean regexp = false;

        public Builder(String testName, int percent) {
            this.testName = testName;
            this.allowedPercentOfFailedTransactions = Percent.from(percent);
        }

        public PassedTransactionsRelativeTest.Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public PassedTransactionsRelativeTest.Builder withTransactionName(String transactionName) {
            this.transactionName = transactionName;
            return this;
        }

        public PassedTransactionsRelativeTest.Builder withRegexp() {
            this.regexp = true;
            return this;
        }

        public PassedTransactionsRelativeTest build() {
            PassedTransactionsRelativeTest test;
            test = new PassedTransactionsRelativeTest(testName, allowedPercentOfFailedTransactions);
            test.description = this.description;
            test.transactionName = this.transactionName;
            test.regexp = this.regexp;
            return test;
        }
    }
}
