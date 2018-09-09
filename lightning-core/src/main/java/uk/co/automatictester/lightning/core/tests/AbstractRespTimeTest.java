package uk.co.automatictester.lightning.core.tests;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import uk.co.automatictester.lightning.core.enums.TestResult;
import uk.co.automatictester.lightning.core.state.data.JmeterTransactions;
import uk.co.automatictester.lightning.core.state.data.TestData;

import java.util.List;

import static uk.co.automatictester.lightning.core.enums.JmeterColumns.TRANSACTION_DURATION;

public abstract class AbstractRespTimeTest extends AbstractClientSideTest {

    private List<Integer> longestTransactions;

    protected AbstractRespTimeTest(String testType, String testName) {
        super(testType, testName);
    }

    @Override
    public void execute() {
        try {
            JmeterTransactions originalJmeterTransactions = TestData.getInstance().clientSideTestData();
            JmeterTransactions transactions = filterTransactions(originalJmeterTransactions);
            transactionCount = transactions.size();
            calculateActualResult(transactions);
            longestTransactions = transactions.longestTransactions();
            calculateActualResultDescription();
            calculateTestResult();
        } catch (Exception e) {
            result = TestResult.ERROR;
            actualResultDescription = e.getMessage();
        }
    }

    @Override
    public List<Integer> longestTransactions() {
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
                name(),
                type(),
                descriptionForReport(),
                transactionNameForReport(),
                expectedResultDescription(),
                actualResultDescription(),
                transactionCount(),
                longestTransactions(),
                resultForReport());
    }

    @Override
    protected void calculateActualResult(JmeterTransactions transactions) {
        DescriptiveStatistics ds = new DescriptiveStatistics();
        transactions.asStream()
                .map(t -> Double.parseDouble(t[TRANSACTION_DURATION.getColumn()]))
                .forEach(ds::addValue);
        actualResult = calculateNumericResult(ds);
    }

    protected abstract int calculateNumericResult(DescriptiveStatistics ds);
}
