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

    public PassedTransactionsTest(String name, String type, String description, String transactionName, long allowedNumberOfFailedTransactions) {
        super(name, type, description, transactionName);
        this.type = ThresholdType.NUMBER;
        this.allowedNumberOfFailedTransactions = allowedNumberOfFailedTransactions;
        expectedResultDescription = String.format(EXPECTED_RESULT_MESSAGE, this.type.toString(), allowedNumberOfFailedTransactions);
    }

    public PassedTransactionsTest(String name, String type, String description, String transactionName, Percent percent) {
        super(name, type, description, transactionName);
        this.type = ThresholdType.PERCENT;
        this.allowedPercentOfFailedTransactions = percent;
        expectedResultDescription = String.format(EXPECTED_RESULT_MESSAGE, this.type.toString(), allowedPercentOfFailedTransactions.getValue());
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
}
