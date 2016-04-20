package uk.co.automatictester.lightning.tests;

import uk.co.automatictester.lightning.data.JMeterTransactions;
import uk.co.automatictester.lightning.enums.TestResult;

import java.util.ArrayList;
import java.util.Objects;

public class ThroughputTest extends ClientSideTest {

    private static final String EXPECTED_RESULT_MESSAGE = "Throughput >= %s / second";
    private static final String ACTUAL_RESULT_MESSAGE = "Throughput = %s / second";

    private final double minThroughput;

    public ThroughputTest(String name, String type, String description, String transactionName, double minThroughput) {
        super(name, type, description, transactionName);
        this.minThroughput = minThroughput;
        expectedResultDescription = String.format(EXPECTED_RESULT_MESSAGE, minThroughput);
    }

    public void execute(ArrayList<ArrayList<String>> originalJMeterTransactions) {
        try {
            JMeterTransactions transactions = filterTransactions((JMeterTransactions) originalJMeterTransactions);
            transactionCount = transactions.getTransactionCount();

            actualResult = transactions.getThroughput();
            actualResultDescription = String.format(ACTUAL_RESULT_MESSAGE, actualResult);

            if ((double) actualResult < minThroughput) {
                result = TestResult.FAIL;
            } else {
                result = TestResult.PASS;
            }
        } catch (Exception e) {
            result = TestResult.IGNORED;
            actualResultDescription = e.getMessage();
        }
    }

    public boolean equals(Object obj) {
        if (obj instanceof ThroughputTest) {
            ThroughputTest test = (ThroughputTest) obj;
            return name.equals(test.name) &&
                    description.equals(test.description) &&
                    Objects.equals(transactionName, test.transactionName) &&
                    expectedResultDescription.equals(test.expectedResultDescription) &&
                    actualResultDescription.equals(test.actualResultDescription) &&
                    result == test.result &&
                    minThroughput == test.minThroughput &&
                    transactionCount == test.transactionCount &&
                    Objects.equals(actualResult, test.actualResult) &&
                    type.equals(test.type);
        } else {
            return false;
        }
    }
}
