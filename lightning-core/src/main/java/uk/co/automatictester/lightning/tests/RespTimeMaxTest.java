package uk.co.automatictester.lightning.tests;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import uk.co.automatictester.lightning.data.JMeterTransactions;
import uk.co.automatictester.lightning.enums.TestResult;

import java.util.ArrayList;
import java.util.List;

public class RespTimeMaxTest extends RespTimeBasedTest {

    private static final String EXPECTED_RESULT_MESSAGE = "Max response time <= %s";
    private static final String ACTUAL_RESULT_MESSAGE = "Max response time = %s";

    private final long maxRespTime;

    public RespTimeMaxTest(String name, String type, String description, String transactionName, long maxRespTime) {
        super(name, type, description, transactionName);
        this.maxRespTime = maxRespTime;
        expectedResultDescription = String.format(EXPECTED_RESULT_MESSAGE, maxRespTime);
    }

    @Override
    public void execute(ArrayList<String[]> originalJMeterTransactions) {

        try {
            JMeterTransactions transactions = filterTransactions((JMeterTransactions) originalJMeterTransactions);
            transactionCount = transactions.getTransactionCount();

            DescriptiveStatistics ds = new DescriptiveStatistics();
            for (String[] transaction : transactions) {
                String elapsed = transaction[1];
                ds.addValue(Double.parseDouble(elapsed));
            }
            longestTransactions = transactions.getLongestTransactions();
            actualResult = (int) ds.getMax();
            actualResultDescription = String.format(ACTUAL_RESULT_MESSAGE, actualResult);

            if ((long) actualResult > this.maxRespTime) {
                result = TestResult.FAIL;
            } else {
                result = TestResult.PASS;
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
