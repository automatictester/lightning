package uk.co.deliverymind.lightning.tests;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import uk.co.deliverymind.lightning.data.JMeterTransactions;
import uk.co.deliverymind.lightning.enums.TestResult;
import uk.co.deliverymind.lightning.enums.ThresholdType;
import uk.co.deliverymind.lightning.utils.Percent;

import java.util.ArrayList;

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
            transactionCount = transactions.getTransactionCount();

            int failureCount = 0;
            for (String[] transaction : transactions) {
                String success = transaction[2];
                if (!Boolean.parseBoolean(success)) {
                    failureCount++;
                }
            }

            if (type.equals(ThresholdType.NUMBER)) {
                if (failureCount > allowedNumberOfFailedTransactions) {
                    result = TestResult.FAIL;
                } else {
                    result = TestResult.PASS;
                }
                this.actualResult = failureCount;
                actualResultDescription = String.format(ACTUAL_RESULT_MESSAGE, this.type.toString(), failureCount);
            } else {
                int percentOfFailedTransactions = (int) (((float) failureCount / transactionCount) * 100);
                if (percentOfFailedTransactions > (float) allowedPercentOfFailedTransactions.getValue()) {
                    result = TestResult.FAIL;
                } else {
                    result = TestResult.PASS;
                }
                this.actualResult = percentOfFailedTransactions;
                actualResultDescription = String.format(ACTUAL_RESULT_MESSAGE, this.type.toString(), percentOfFailedTransactions);
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

}
