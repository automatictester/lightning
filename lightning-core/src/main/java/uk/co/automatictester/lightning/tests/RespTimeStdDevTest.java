package uk.co.automatictester.lightning.tests;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import uk.co.automatictester.lightning.data.JMeterTransactions;
import uk.co.automatictester.lightning.enums.TestResult;

import java.util.ArrayList;

import static uk.co.automatictester.lightning.constants.JMeterColumns.TRANSACTION_DURATION_INDEX;

public class RespTimeStdDevTest extends ClientSideTest {

    private static final String EXPECTED_RESULT_MESSAGE = "Average standard deviance time <= %s";
    private static final String ACTUAL_RESULT_MESSAGE = "Average standard deviance time = %s";

    private final long maxRespTimeStdDev;

    public RespTimeStdDevTest(String name, String type, String description, String transactionName, long maxRespTimeStdDev) {
        super(name, type, description, transactionName);
        this.maxRespTimeStdDev = maxRespTimeStdDev;
        expectedResultDescription = String.format(EXPECTED_RESULT_MESSAGE, maxRespTimeStdDev);
    }

    @Override
    public void execute(ArrayList<String[]> originalJMeterTransactions) {
        try {
            JMeterTransactions transactions = filterTransactions((JMeterTransactions) originalJMeterTransactions);
            transactionCount = transactions.size();
            calculateActualResult(transactions);
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

    private void calculateActualResult(JMeterTransactions jmeterTransactions) {
        DescriptiveStatistics ds = new DescriptiveStatistics();
        for (String[] transaction : jmeterTransactions) {
            String elapsed = transaction[TRANSACTION_DURATION_INDEX];
            ds.addValue(Double.parseDouble(elapsed));
        }
        actualResult = (int) ds.getStandardDeviation();
    }

    private void calculateTestResult() {
        if (actualResult > maxRespTimeStdDev) {
            result = TestResult.FAIL;
        } else {
            result = TestResult.PASS;
        }
    }
}
