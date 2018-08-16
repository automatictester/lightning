package uk.co.automatictester.lightning.tests.base;

import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.automatictester.lightning.data.JMeterTransactions;
import uk.co.automatictester.lightning.enums.TestResult;
import uk.co.automatictester.lightning.structures.TestData;

import java.util.List;

import static uk.co.automatictester.lightning.enums.JMeterColumns.TRANSACTION_RESULT_INDEX;

public abstract class ClientSideTest extends LightningTest {

    protected String transactionName;
    protected int transactionCount;
    protected boolean regexp = false;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    protected ClientSideTest(String testType, String testName) {
        super(testType, testName);
    }

    public JMeterTransactions filterTransactions(JMeterTransactions originalJMeterTransactions) {
        String transactionName = getTransactionName();
        if (transactionName == null) {
            return originalJMeterTransactions;
        } else {
            if (isRegexp()) {
                return originalJMeterTransactions.getTransactionsMatching(transactionName);
            } else {
                return originalJMeterTransactions.getTransactionsWith(transactionName);
            }
        }
    }

    public int getTransactionCount() {
        return transactionCount;
    }

    public String getTransactionName() {
        return transactionName;
    }

    public boolean isRegexp() {
        return regexp;
    }

    public void execute() {
        try {
            JMeterTransactions originalJMeterTransactions = TestData.getClientSideTestData();
            JMeterTransactions transactions = filterTransactions(originalJMeterTransactions);
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

    @Override
    public void printTestExecutionReport() {
        log.info(getTestExecutionReport());
    }

    protected String getTransactionNameForReport() {
        String message = String.format("Transaction name:     %s%n", getTransactionName());
        return getTransactionName() != null ? message : "";
    }

    public List<Integer> getLongestTransactions() {
        throw new NotImplementedException("Method not implemented for LightningTest which is not RespTimeBasedTest");
    }

    protected int getFailureCount(JMeterTransactions jmeterTransactions) {
        int failureCount = 0;
        for (String[] transaction : jmeterTransactions.getEntries()) {
            String success = transaction[TRANSACTION_RESULT_INDEX.getValue()];
            if (!Boolean.parseBoolean(success)) {
                failureCount++;
            }
        }
        return failureCount;
    }

    protected abstract void calculateActualResult(JMeterTransactions jmeterTransactions);

    protected abstract void calculateActualResultDescription();

    protected abstract void calculateTestResult();
}
