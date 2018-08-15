package uk.co.automatictester.lightning.tests.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.automatictester.lightning.data.JMeterTransactions;
import uk.co.automatictester.lightning.enums.TestResult;
import uk.co.automatictester.lightning.structures.TestData;

import java.util.List;

public abstract class RespTimeBasedTest extends ClientSideTest {

    protected List<Integer> longestTransactions;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected RespTimeBasedTest(String testType, String testName) {
        super(testType, testName);
    }

    public void execute() {
        try {
            JMeterTransactions originalJMeterTransactions = TestData.getClientSideTestData();
            JMeterTransactions transactions = filterTransactions(originalJMeterTransactions);
            transactionCount = transactions.size();
            calculateActualResult(transactions);
            longestTransactions = transactions.getLongestTransactions();
            calculateActualResultDescription();
            calculateTestResult();
        } catch (Exception e) {
            result = TestResult.ERROR;
            actualResultDescription = e.getMessage();
        }
    }

    @Override
    public List<Integer> getLongestTransactions() {
        return longestTransactions;
    }

    @Override
    public String getTestExecutionReport() {
        return String.format("Test name:            %s%n" +
                        "Test type:            %s%n" +
                        "%s" +
                        "%s" +
                        "Expected result:      %s%n" +
                        "Actual result:        %s%n" +
                        "Transaction count:    %s%n" +
                        "Longest transactions: %s%n" +
                        "Test result:          %s%n",
                getName(),
                getType(),
                getDescriptionForReport(),
                getTransactionNameForReport(),
                getExpectedResultDescription(),
                getActualResultDescription(),
                getTransactionCount(),
                getLongestTransactions(),
                getResultForReport());
    }

    @Override
    public void printTestExecutionReport() {
        logger.info(getTestExecutionReport());
    }
}
