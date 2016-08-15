package uk.co.automatictester.lightning.tests;

import java.util.List;

public abstract class RespTimeBasedTest extends ClientSideTest {

    protected List<Integer> longestTransactions;

    public RespTimeBasedTest(String name, String type, String description, String transactionName) {
        super(name, type, description, transactionName);
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
        System.out.println(getTestExecutionReport());
    }
}