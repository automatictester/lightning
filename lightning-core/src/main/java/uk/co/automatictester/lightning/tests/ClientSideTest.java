package uk.co.automatictester.lightning.tests;

import org.apache.commons.lang3.NotImplementedException;
import uk.co.automatictester.lightning.data.JMeterTransactions;

import java.util.List;

public abstract class ClientSideTest extends LightningTest {

    protected final String transactionName;
    protected int transactionCount;

    protected ClientSideTest(String name, String type, String description, String transactionName) {
        super(name, type, description);
        this.transactionName = transactionName;
    }

    public JMeterTransactions filterTransactions(JMeterTransactions originalJMeterTransactions) {
        if (getTransactionName() != null) {
            return originalJMeterTransactions.excludeLabelsOtherThan(getTransactionName());
        } else {
            return originalJMeterTransactions;
        }
    }

    public int getTransactionCount() {
        return transactionCount;
    }

    public String getTransactionName() {
        return transactionName;
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
        System.out.println(getTestExecutionReport());
    }

    protected String getTransactionNameForReport() {
        return (getTransactionName() != null) ? String.format("Transaction name:     %s%n", getTransactionName()) : "";
    }

    public List<Integer> getLongestTransactions() {
        throw new NotImplementedException("Method not implemented for LightningTest which is not RespTimeBasedTest");
    }
}
