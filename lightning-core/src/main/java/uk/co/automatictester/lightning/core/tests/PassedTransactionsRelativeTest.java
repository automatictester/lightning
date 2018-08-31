package uk.co.automatictester.lightning.core.tests;

import org.apache.commons.lang3.builder.EqualsBuilder;
import uk.co.automatictester.lightning.core.data.JMeterTransactions;
import uk.co.automatictester.lightning.core.enums.TestResult;
import uk.co.automatictester.lightning.core.tests.base.ClientSideTest;
import uk.co.automatictester.lightning.core.utils.Percent;

import java.util.Arrays;
import java.util.List;

public class PassedTransactionsRelativeTest extends ClientSideTest {

    private static final String TEST_TYPE = "passedTransactionsTest";
    private static final String EXPECTED_RESULT_MESSAGE = "Percent of failed transactions <= %s";
    private static final String ACTUAL_RESULT_MESSAGE = "Percent of failed transactions = %s";
    private final Percent allowedPercentOfFailedTransactions;

    private PassedTransactionsRelativeTest(String testName, Percent percent) {
        super(TEST_TYPE, testName);
        this.allowedPercentOfFailedTransactions = percent;
        this.expectedResultDescription = String.format(EXPECTED_RESULT_MESSAGE, allowedPercentOfFailedTransactions.getValue());
    }

    @Override
    protected void calculateActualResult(JMeterTransactions jmeterTransactions) {
        int failureCount = getFailureCount(jmeterTransactions);
        actualResult = (int) (((float) failureCount / transactionCount) * 100);
    }

    @Override
    protected void calculateActualResultDescription() {
        actualResultDescription = String.format(ACTUAL_RESULT_MESSAGE, actualResult);
    }

    @Override
    protected void calculateTestResult() {
        result = (actualResult > (float) allowedPercentOfFailedTransactions.getValue()) ? TestResult.FAIL : TestResult.PASS;
    }

    @Override
    public boolean equals(Object obj) {
        List<String> fieldsToExclude = Arrays.asList("transactionCount", "actualResultDescription", "result", "actualResult");
        return EqualsBuilder.reflectionEquals(this, obj, fieldsToExclude);
    }

    @Override
    public int hashCode() {
        return TEST_TYPE.hashCode() + name.hashCode() + allowedPercentOfFailedTransactions.getValue();
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
