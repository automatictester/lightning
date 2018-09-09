package uk.co.automatictester.lightning.core.state.data;

import java.util.List;

public class TestData {

    private static final TestData INSTANCE = new TestData();
    private JmeterTransactions transactions;
    private PerfMonEntries entries;

    private TestData() {
    }

    public static TestData getInstance() {
        return INSTANCE;
    }

    public void flush() {
        if (transactions != null) {
            transactions.asList().clear();
        }
        if (entries != null) {
            entries.asList().clear();
        }
    }

    public void addClientSideTestData(List<String[]> jmeterTransactions) {
        transactions = JmeterTransactions.fromList(jmeterTransactions);
    }

    public void addServerSideTestData(List<String[]> perfMonEntries) {
        entries = PerfMonEntries.fromList(perfMonEntries);
    }

    public JmeterTransactions clientSideTestData() {
        return transactions;
    }

    public PerfMonEntries serverSideTestData() {
        return entries;
    }

    @Override
    public String toString() {
        return String.format("JMeter: %d, PerfMon: %d", transactions.size(), entries.size());
    }
}
