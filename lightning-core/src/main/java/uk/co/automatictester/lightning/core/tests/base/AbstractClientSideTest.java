package uk.co.automatictester.lightning.core.tests.base;

import org.apache.commons.lang3.NotImplementedException;
import uk.co.automatictester.lightning.core.state.data.JmeterTransactions;
import uk.co.automatictester.lightning.core.enums.TestResult;
import uk.co.automatictester.lightning.core.state.data.TestData;

import java.util.List;

import static uk.co.automatictester.lightning.core.enums.JmeterColumns.TRANSACTION_RESULT;

public abstract class AbstractClientSideTest extends AbstractTest {

    protected String transactionName;
    protected int transactionCount;
    protected boolean regexp = false;

    protected AbstractClientSideTest(String testType, String testName) {
        super(testType, testName);
    }

    @Override
    public void execute() {
        try {
            JmeterTransactions originalJmeterTransactions = TestData.getInstance().getClientSideTestData();
            JmeterTransactions transactions = filterTransactions(originalJmeterTransactions);
            transactionCount = transactions.size();
            calculateActualResult(transactions);
            calculateActualResultDescription();
            calculateTestResult();
        } catch (Exception e) {
            result = TestResult.ERROR;
            actualResultDescription = e.getMessage();
        }
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
                        "Test result:          %s%n",
                getName(),
                getType(),
                getDescriptionForReport(),
                getTransactionNameForReport(),
                getExpectedResultDescription(),
                getActualResultDescription(),
                getTransactionCount(),
                getResultForReport());
    }

    public JmeterTransactions filterTransactions(JmeterTransactions originalJmeterTransactions) {
        String transactionName = getTransactionName();
        if (transactionName == null) {
            return originalJmeterTransactions;
        } else {
            if (isRegexp()) {
                return originalJmeterTransactions.getTransactionsMatching(transactionName);
            } else {
                return originalJmeterTransactions.getTransactionsWith(transactionName);
            }
        }
    }

    public String getTransactionName() {
        return transactionName;
    }

    public boolean isRegexp() {
        return regexp;
    }

    public List<Integer> getLongestTransactions() {
        throw new NotImplementedException("Method not implemented for AbstractTest which is not AbstractRespTimeTest");
    }

    protected int getFailureCount(JmeterTransactions transactions) {
        return (int) transactions.stream()
                .filter(t -> "false".equals(t[TRANSACTION_RESULT.getColumn()]))
                .count();
    }

    String getTransactionNameForReport() {
        String message = String.format("Transaction name:     %s%n", getTransactionName());
        return getTransactionName() != null ? message : "";
    }

    int getTransactionCount() {
        return transactionCount;
    }

    protected abstract void calculateActualResult(JmeterTransactions jmeterTransactions);

    protected abstract void calculateActualResultDescription();

    protected abstract void calculateTestResult();
}
