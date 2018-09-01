package uk.co.automatictester.lightning.core.structures;

import org.testng.annotations.Test;
import uk.co.automatictester.lightning.core.data.JMeterTransactions;
import uk.co.automatictester.lightning.core.data.PerfMonEntries;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class TestDataTest {

    @Test
    public void testGetInstance() {
        List<String[]> transactions = new ArrayList<>();
        transactions.add(new String[]{"Login", "1200", "true"});
        transactions.add(new String[]{"Login", "1000", "true"});
        transactions.add(new String[]{"Search", "800", "true"});
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(transactions);

        TestData testDataA = TestData.getInstance();
        testDataA.addClientSideTestData(jmeterTransactions);

        TestData testDataB = TestData.getInstance();

        assertThat(testDataA == testDataB, is(true));
    }

    @Test
    public void testFlush() {
        List<String[]> transactions = new ArrayList<>();
        transactions.add(new String[]{"Login", "1200", "true"});
        transactions.add(new String[]{"Login", "1000", "true"});
        transactions.add(new String[]{"Search", "800", "true"});
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(transactions);

        List<String[]> entries = new ArrayList<>();
        entries.add(new String[]{"1455366135623", "9128", "192.168.0.12 CPU"});
        entries.add(new String[]{"1455366145623", "1232", "192.168.0.12 CPU"});
        PerfMonEntries perfMonEntries = PerfMonEntries.fromList(entries);

        TestData testData = TestData.getInstance();
        testData.addClientSideTestData(jmeterTransactions);
        testData.addServerSideTestData(perfMonEntries);
        
        assertThat(testData.getClientSideTestData().size(), is(equalTo(3)));
        assertThat(testData.getServerSideTestData().size(), is(equalTo(2)));
        assertThat(testData.toString(), is(equalTo("JMeter: 3, PerfMon: 2")));

        testData.flush();

        assertThat(testData.getClientSideTestData().size(), is(equalTo(0)));
        assertThat(testData.getServerSideTestData().size(), is(equalTo(0)));
        assertThat(testData.toString(), is(equalTo("JMeter: 0, PerfMon: 0")));
    }
}
