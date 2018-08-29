package uk.co.automatictester.lightning.core.structures;

import uk.co.automatictester.lightning.core.data.JMeterTransactions;
import uk.co.automatictester.lightning.core.data.PerfMonEntries;

public class TestData {

    private static TestData instance;
    private JMeterTransactions transactions;
    private PerfMonEntries entries;

    private TestData() {
    }

    public synchronized static TestData createInstance() {
        instance = new TestData();
        return instance;
    }

    public synchronized static TestData getInstance() {
        if (instance == null) {
            return createInstance();
        } else {
            return instance;
        }
    }

    public void addClientSideTestData(JMeterTransactions jmeterTransactions) {
        transactions = JMeterTransactions.fromList(jmeterTransactions.getEntries());
    }

    public void addServerSideTestData(PerfMonEntries perfMonEntries) {
        entries = PerfMonEntries.fromList(perfMonEntries.getEntries());
    }

    public JMeterTransactions getClientSideTestData() {
        return transactions;
    }

    public PerfMonEntries getServerSideTestData() {
        return entries;
    }
}
