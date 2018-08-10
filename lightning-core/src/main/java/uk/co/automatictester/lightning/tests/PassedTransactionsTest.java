package uk.co.automatictester.lightning.tests;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import uk.co.automatictester.lightning.data.JMeterTransactions;
import uk.co.automatictester.lightning.enums.TestResult;
import uk.co.automatictester.lightning.enums.ThresholdType;
import uk.co.automatictester.lightning.utils.Percent;

import java.util.ArrayList;

import static uk.co.automatictester.lightning.constants.JMeterColumns.TRANSACTION_RESULT_INDEX;

public class PassedTransactionsTest extends ClientSideTest {

    private static final String EXPECTED_RESULT_MESSAGE = "%s of failed transactions <= %s";
    private static final String ACTUAL_RESULT_MESSAGE = "%s of failed transactions = %s";

    private ThresholdType type;
    private long allowedNumberOfFailedTransactions = 0;
    private Percent allowedPercentOfFailedTransactions;

    private PassedTransactionsTest(String testName, long allowedNumberOfFailedTransactions) {
        super("passedTransactionsTest", testName);
        this.allowedNumberOfFailedTransactions = allowedNumberOfFailedTransactions;
        this.type = ThresholdType.NUMBER;
        this.expectedResultDescription = String.format(EXPECTED_RESULT_MESSAGE, this.type.toString(), allowedNumberOfFailedTransactions);
    }

    private PassedTransactionsTest(String testName, Percent percent) {
        super("passedTransactionsTest", testName);
        this.allowedPercentOfFailedTransactions = percent;
        this.type = ThresholdType.PERCENT;
        this.expectedResultDescription = String.format(EXPECTED_RESULT_MESSAGE, this.type.toString(), allowedPercentOfFailedTransactions.getValue());
    }

    @Override
    public void execute(ArrayList<String[]> originalJMeterTransactions) {
        try {
            JMeterTransactions transactions = filterTransactions((JMeterTransactions) originalJMeterTransactions);
            transactionCount = transactions.size();
            int failureCount = getFailureCount(transactions);

            switch (type) {
                case NUMBER:
                    calculateResultForAbsoluteTreshold(failureCount);
                    break;
                case PERCENT:
                    calculateResultForRelativeTreshold(failureCount);
                    break;
            }
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

    private int getFailureCount(JMeterTransactions jmeterTransactions) {
        int failureCount = 0;
        for (String[] transaction : jmeterTransactions) {
            String success = transaction[TRANSACTION_RESULT_INDEX];
            if (!Boolean.parseBoolean(success)) {
                failureCount++;
            }
        }
        return failureCount;
    }

    private void calculateResultForAbsoluteTreshold(int failureCount) {
        if (failureCount > allowedNumberOfFailedTransactions) {
            result = TestResult.FAIL;
        } else {
            result = TestResult.PASS;
        }
        actualResult = failureCount;
        actualResultDescription = String.format(ACTUAL_RESULT_MESSAGE, this.type.toString(), failureCount);
    }

    private void calculateResultForRelativeTreshold(int failureCount) {
        int percentOfFailedTransactions = (int) (((float) failureCount / transactionCount) * 100);
        if (percentOfFailedTransactions > (float) allowedPercentOfFailedTransactions.getValue()) {
            result = TestResult.FAIL;
        } else {
            result = TestResult.PASS;
        }
        actualResult = percentOfFailedTransactions;
        actualResultDescription = String.format(ACTUAL_RESULT_MESSAGE, this.type.toString(), percentOfFailedTransactions);
    }

    public static class Builder {
        private String testName;
        private long allowedNumberOfFailedTransactions;
        private Percent allowedPercentOfFailedTransactions;
        private String description;
        private String transactionName;
        private boolean regexp = false;

        public Builder(String testName, long allowedNumberOfFailedTransactions) {
            this.testName = testName;
            this.allowedNumberOfFailedTransactions = allowedNumberOfFailedTransactions;
        }

        public Builder(String testName, Percent percent) {
            this.testName = testName;
            this.allowedPercentOfFailedTransactions = percent;
        }

        public PassedTransactionsTest.Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public PassedTransactionsTest.Builder withTransactionName(String transactionName) {
            this.transactionName = transactionName;
            return this;
        }

        public PassedTransactionsTest.Builder withRegexp() {
            this.regexp = true;
            return this;
        }

        public PassedTransactionsTest build() {
            PassedTransactionsTest test;
            if (allowedPercentOfFailedTransactions == null) {
                test = new PassedTransactionsTest(testName, allowedNumberOfFailedTransactions);
            } else {
                test = new PassedTransactionsTest(testName, allowedPercentOfFailedTransactions);
            }
            test.description = this.description;
            test.transactionName = this.transactionName;
            test.regexp = this.regexp;
            return test;
        }
    }
}
