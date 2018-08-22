package uk.co.automatictester.lightning.core.tests.base;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import uk.co.automatictester.lightning.core.data.JMeterTransactions;
import uk.co.automatictester.lightning.core.enums.TestResult;
import uk.co.automatictester.lightning.core.structures.TestData;

import java.util.List;

import static uk.co.automatictester.lightning.core.enums.JMeterColumns.TRANSACTION_DURATION_INDEX;

public abstract class RespTimeBasedTest extends ClientSideTest {

    protected List<Integer> longestTransactions;

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

    protected void calculateActualResult(JMeterTransactions transactions) {
        DescriptiveStatistics ds = new DescriptiveStatistics();
        transactions.getEntries().stream()
                .map(t -> Double.parseDouble(t[TRANSACTION_DURATION_INDEX.getValue()]))
                .forEach(ds::addValue);
        actualResult = getResult(ds);
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

    protected abstract int getResult(DescriptiveStatistics ds);
}
