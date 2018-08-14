package uk.co.automatictester.lightning.structures;

import uk.co.automatictester.lightning.data.JMeterTransactions;
import uk.co.automatictester.lightning.data.PerfMonEntries;

public class TestData {

    private static JMeterTransactions transactions;
    private static PerfMonEntries entries;

    public static void addClientSideTestData(JMeterTransactions jmeterTransactions) {
        transactions = JMeterTransactions.fromList(jmeterTransactions);
    }

    public static void addServerSideTestData(PerfMonEntries perfMonEntries) {
        entries = PerfMonEntries.fromList(perfMonEntries);
    }

    public static JMeterTransactions getClientSideTestData() {
        return transactions;
    }

    public static PerfMonEntries getServerSideTestData() {
        return entries;
    }
}
