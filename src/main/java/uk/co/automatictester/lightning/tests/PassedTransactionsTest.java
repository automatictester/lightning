package uk.co.automatictester.lightning.tests;

import org.apache.commons.lang3.StringUtils;
import uk.co.automatictester.lightning.data.JMeterTransactions;
import uk.co.automatictester.lightning.enums.TestResult;
import uk.co.automatictester.lightning.enums.ThresholdType;
import uk.co.automatictester.lightning.utils.Percent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        expectedResult = String.format(EXPECTED_RESULT_MESSAGE, this.type.toString(), allowedNumberOfFailedTransactions);
    }

    public PassedTransactionsTest(String name, String type, String description, String transactionName, Percent percent) {
        super(name, type, description, transactionName);
        this.type = ThresholdType.PERCENT;
        this.allowedPercentOfFailedTransactions = percent;
        expectedResult = String.format(EXPECTED_RESULT_MESSAGE, this.type.toString(), allowedPercentOfFailedTransactions.getPercent());
    }

    public void execute(ArrayList<ArrayList<String>> originalJMeterTransactions) {
        try {
            JMeterTransactions transactions = filterTransactions((JMeterTransactions) originalJMeterTransactions);
            transactionCount = transactions.getTransactionCount();

            int failureCount = 0;
            for (List<String> transaction : transactions) {
                String success = transaction.get(2);
                if (!Boolean.parseBoolean(success)) failureCount++;
            }

            if (type.equals(ThresholdType.NUMBER)) {
                if (failureCount > allowedNumberOfFailedTransactions) {
                    result = TestResult.FAIL;
                } else {
                    result = TestResult.PASS;
                }
                actualResult = String.format(ACTUAL_RESULT_MESSAGE, this.type.toString(), failureCount);
            } else {
                float percentOfFailedTransactions = ((float) failureCount / transactionCount) * 100;
                if (percentOfFailedTransactions > (float) allowedPercentOfFailedTransactions.getPercent()) {
                    result = TestResult.FAIL;
                } else {
                    result = TestResult.PASS;
                }
                actualResult = String.format(ACTUAL_RESULT_MESSAGE, this.type.toString(), percentOfFailedTransactions);
            }
        } catch (Exception e) {
            result = TestResult.IGNORED;
            actualResult = e.getMessage();
        }
    }

    public boolean equals(Object obj) {
        if (obj instanceof PassedTransactionsTest) {
            PassedTransactionsTest test = (PassedTransactionsTest) obj;
            return name.equals(test.name) &&
                    description.equals(test.description) &&
                    Objects.equals(transactionName, test.transactionName) &&
                    expectedResult.equals(test.expectedResult) &&
                    actualResult.equals(test.actualResult) &&
                    result == test.result &&
                    allowedNumberOfFailedTransactions == test.allowedNumberOfFailedTransactions &&
                    percentEquals(test) &&
                    type == test.type &&
                    transactionCount == test.transactionCount &&
                    type.equals(test.type);
        } else {
            return false;
        }
    }

    private boolean percentEquals(PassedTransactionsTest test) {
        if (allowedPercentOfFailedTransactions != null && test.allowedPercentOfFailedTransactions != null) {
            return (allowedPercentOfFailedTransactions.getPercent() == test.allowedPercentOfFailedTransactions.getPercent());
        } else if (allowedPercentOfFailedTransactions == null && test.allowedPercentOfFailedTransactions == null) {
            return true;
        } else {
            return false;
        }
    }

}
