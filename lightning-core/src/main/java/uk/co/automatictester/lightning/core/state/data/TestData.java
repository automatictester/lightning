package uk.co.automatictester.lightning.core.state.data;

public class TestData {

    private static TestData instance;
    private JmeterTransactions transactions;
    private PerfMonEntries entries;

    private TestData() {
    }

    public synchronized static TestData getInstance() {
        if (instance == null) {
            instance = new TestData();
        }
        return instance;
    }

    public void flush() {
        if (transactions != null) {
            transactions.entries().clear();
        }
        if (entries != null) {
            entries.entries().clear();
        }
    }

    public void addClientSideTestData(JmeterTransactions jmeterTransactions) {
        transactions = JmeterTransactions.fromList(jmeterTransactions.entries());
    }

    public void addServerSideTestData(PerfMonEntries perfMonEntries) {
        entries = PerfMonEntries.fromList(perfMonEntries.entries());
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
