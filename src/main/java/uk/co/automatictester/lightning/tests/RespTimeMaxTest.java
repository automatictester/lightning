package uk.co.automatictester.lightning.tests;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import uk.co.automatictester.lightning.data.JMeterTransactions;
import uk.co.automatictester.lightning.enums.TestResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RespTimeMaxTest extends RespTimeBasedTest {

    private static final String EXPECTED_RESULT_MESSAGE = "Max response time <= %s";
    private static final String ACTUAL_RESULT_MESSAGE = "Max response time = %s";

    private final long maxRespTime;

    public RespTimeMaxTest(String name, String type, String description, String transactionName, long maxRespTime) {
        super(name, type, description, transactionName);
        this.maxRespTime = maxRespTime;
        expectedResultDescription = String.format(EXPECTED_RESULT_MESSAGE, maxRespTime);
    }

    public void execute(ArrayList<ArrayList<String>> originalJMeterTransactions) {

        try {
            JMeterTransactions transactions = filterTransactions((JMeterTransactions) originalJMeterTransactions);
            transactionCount = transactions.getTransactionCount();

            DescriptiveStatistics ds = new DescriptiveStatistics();
            for (List<String> transaction : transactions) {
                String elapsed = transaction.get(1);
                ds.addValue(Double.parseDouble(elapsed));
            }
            longestTransactions = transactions.getLongestTransactions();
            actualResult = (long) ds.getMax();
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

    public boolean equals(Object obj) {
        if (obj instanceof RespTimeMaxTest) {
            RespTimeMaxTest test = (RespTimeMaxTest) obj;
            return name.equals(test.name) &&
                    description.equals(test.description) &&
                    transactionName.equals(test.transactionName) &&
                    expectedResultDescription.equals(test.expectedResultDescription) &&
                    actualResultDescription.equals(test.actualResultDescription) &&
                    result == test.result &&
                    maxRespTime == test.maxRespTime &&
                    transactionCount == test.transactionCount &&
                    Objects.equals(actualResult, test.actualResult) &&
                    type.equals(test.type);
        } else {
            return false;
        }
    }
}
