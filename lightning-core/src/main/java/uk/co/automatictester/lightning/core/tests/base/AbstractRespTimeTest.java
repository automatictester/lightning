package uk.co.automatictester.lightning.core.tests.base;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import uk.co.automatictester.lightning.core.state.data.JmeterTransactions;
import uk.co.automatictester.lightning.core.enums.TestResult;
import uk.co.automatictester.lightning.core.state.data.TestData;

import java.util.List;

import static uk.co.automatictester.lightning.core.enums.JmeterColumns.TRANSACTION_DURATION_INDEX;

public abstract class AbstractRespTimeTest extends AbstractClientSideTest {

    private List<Integer> longestTransactions;

    protected AbstractRespTimeTest(String testType, String testName) {
        super(testType, testName);
    }

    @Override
    public void execute() {
        try {
            JmeterTransactions originalJmeterTransactions = TestData.getInstance().getClientSideTestData();
            JmeterTransactions transactions = filterTransactions(originalJmeterTransactions);
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
    protected void calculateActualResult(JmeterTransactions transactions) {
        DescriptiveStatistics ds = new DescriptiveStatistics();
        transactions.getEntries().stream()
                .map(t -> Double.parseDouble(t[TRANSACTION_DURATION_INDEX.getValue()]))
                .forEach(ds::addValue);
        actualResult = getResult(ds);
    }

    protected abstract int getResult(DescriptiveStatistics ds);
}
