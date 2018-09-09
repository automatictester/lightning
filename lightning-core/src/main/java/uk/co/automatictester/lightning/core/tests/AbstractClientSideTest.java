package uk.co.automatictester.lightning.core.tests;

import uk.co.automatictester.lightning.core.enums.TestResult;
import uk.co.automatictester.lightning.core.state.data.JmeterTransactions;
import uk.co.automatictester.lightning.core.state.data.TestData;

import java.util.List;

import static uk.co.automatictester.lightning.core.enums.JmeterColumns.TRANSACTION_RESULT;

public abstract class AbstractClientSideTest extends AbstractLightningTest {

    protected String transactionName;
    protected int transactionCount;
    protected boolean regexp = false;

    protected AbstractClientSideTest(String testType, String testName) {
        super(testType, testName);
    }

    @Override
    public void execute() {
        try {
            JmeterTransactions originalJmeterTransactions = TestData.getInstance().clientSideTestData();
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
                name(),
                type(),
                descriptionForReport(),
                transactionNameForReport(),
                expectedResultDescription(),
                actualResultDescription(),
                transactionCount(),
                resultForReport());
    }

    public JmeterTransactions filterTransactions(JmeterTransactions originalJmeterTransactions) {
        String transactionName = transactionName();
        if (transactionName == null) {
            return originalJmeterTransactions;
        } else {
            if (isRegexp()) {
                return originalJmeterTransactions.transactionsMatching(transactionName);
            } else {
                return originalJmeterTransactions.transactionsWith(transactionName);
            }
        }
    }

    public String transactionName() {
        return transactionName;
    }

    public boolean isRegexp() {
        return regexp;
    }

    public List<Integer> longestTransactions() {
        throw new UnsupportedOperationException("Method not implemented for LightningTest which is not AbstractRespTimeTest");
    }

    protected int failureCount(JmeterTransactions transactions) {
        return (int) transactions.asStream()
                .filter(t -> "false".equals(t[TRANSACTION_RESULT.getColumn()]))
                .count();
    }

    String transactionNameForReport() {
        String message = String.format("Transaction name:     %s%n", transactionName());
        return transactionName() != null ? message : "";
    }

    int transactionCount() {
        return transactionCount;
    }

    protected abstract void calculateActualResult(JmeterTransactions jmeterTransactions);

    protected abstract void calculateActualResultDescription();

    protected abstract void calculateTestResult();
}
