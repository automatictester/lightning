package uk.co.automatictester.lightning.tests;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import uk.co.automatictester.lightning.data.JMeterTransactions;
import uk.co.automatictester.lightning.enums.TestResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class RespTimeAvgTest extends RespTimeBasedTest {

    private static final String EXPECTED_RESULT_MESSAGE = "Average response time <= %s";
    private static final String ACTUAL_RESULT_MESSAGE = "Average response time = %s";

    private final long maxAvgRespTime;

    public RespTimeAvgTest(String name, String type, String description, String transactionName, long maxAvgRespTime) {
        super(name, type, description, transactionName);
        this.maxAvgRespTime = maxAvgRespTime;
        expectedResultDescription = String.format(EXPECTED_RESULT_MESSAGE, maxAvgRespTime);
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
            actualResult = (int) ds.getMean();
            actualResultDescription = String.format(ACTUAL_RESULT_MESSAGE, actualResult);

            if (actualResult > maxAvgRespTime) {
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
        if (obj instanceof RespTimeAvgTest) {
            RespTimeAvgTest test = (RespTimeAvgTest) obj;
            return name.equals(test.name) &&
                    description.equals(test.description) &&
                    transactionName.equals(test.transactionName) &&
                    expectedResultDescription.equals(test.expectedResultDescription) &&
                    actualResultDescription.equals(test.actualResultDescription) &&
                    result == test.result &&
                    maxAvgRespTime == test.maxAvgRespTime &&
                    transactionCount == test.transactionCount &&
                    Objects.equals(actualResult, test.actualResult) &&
                    type.equals(test.type);
        } else {
            return false;
        }
    }
}
