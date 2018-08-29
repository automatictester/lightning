package uk.co.automatictester.lightning.core.structures;

import uk.co.automatictester.lightning.core.data.JMeterTransactions;
import uk.co.automatictester.lightning.core.data.PerfMonEntries;

public class TestData {

    private static JMeterTransactions transactions;
    private static PerfMonEntries entries;

    private TestData() {
    }

    public static void addClientSideTestData(JMeterTransactions jmeterTransactions) {
        transactions = JMeterTransactions.from(jmeterTransactions.getEntries());
    }

    public static void addServerSideTestData(PerfMonEntries perfMonEntries) {
        entries = PerfMonEntries.from(perfMonEntries.getEntries());
    }

    public static JMeterTransactions getClientSideTestData() {
        return transactions;
    }

    public static PerfMonEntries getServerSideTestData() {
        return entries;
    }
}
