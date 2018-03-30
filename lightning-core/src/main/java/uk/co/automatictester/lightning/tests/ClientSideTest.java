package uk.co.automatictester.lightning.tests;

import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.automatictester.lightning.data.JMeterTransactions;

import java.util.List;

public abstract class ClientSideTest extends LightningTest {

    protected final String transactionName;
    protected int transactionCount;
    protected boolean regexp = false;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected ClientSideTest(String name, String type, String description, String transactionName) {
        super(name, type, description);
        this.transactionName = transactionName;
    }

    public JMeterTransactions filterTransactions(JMeterTransactions originalJMeterTransactions) {
        if (getTransactionName() != null) {
            if (isRegexp()) {
                return originalJMeterTransactions.excludeLabelsNotMatching(getTransactionName());
            } else {
                return originalJMeterTransactions.excludeLabelsOtherThan(getTransactionName());
            }
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

    public boolean isRegexp() {
        return regexp;
    }

    public void setRegexp(boolean regexp) {
        this.regexp = regexp;
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
        logger.info(getTestExecutionReport());
    }

    protected String getTransactionNameForReport() {
        return (getTransactionName() != null) ? String.format("Transaction name:     %s%n", getTransactionName()) : "";
    }

    public List<Integer> getLongestTransactions() {
        throw new NotImplementedException("Method not implemented for LightningTest which is not RespTimeBasedTest");
    }
}
