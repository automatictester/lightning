package uk.co.automatictester.lightning.core.tests;

import org.testng.annotations.Test;
import uk.co.automatictester.lightning.core.data.PerfMonEntries;
import uk.co.automatictester.lightning.core.enums.TestResult;
import uk.co.automatictester.lightning.core.structures.TestData;
import uk.co.automatictester.lightning.shared.LegacyTestData;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;

public class ServerSideLessThanTestUnitTest {

    @Test
    public void verifyExecutePass() {
        ServerSideLessThanTest test = new ServerSideLessThanTest.Builder("Test #1", 12501).withDescription("Verify CPU utilisation").withHostAndMetric("192.168.0.12 CPU").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.CPU_ENTRY_10000);
        testData.add(LegacyTestData.CPU_ENTRY_15000);
        PerfMonEntries dataEntries = PerfMonEntries.fromList(testData);
        TestData.getInstance().addServerSideTestData(dataEntries);
        test.execute();
        assertThat(test.getResult(), is(equalTo(TestResult.PASS)));
    }

    @Test
    public void verifyExecuteFail() {
        ServerSideLessThanTest test = new ServerSideLessThanTest.Builder("Test #1", 27500).withDescription("Verify CPU utilisation").withHostAndMetric("192.168.0.12 CPU").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.CPU_ENTRY_25000);
        testData.add(LegacyTestData.CPU_ENTRY_30000);
        PerfMonEntries dataEntries = PerfMonEntries.fromList(testData);
        TestData.getInstance().addServerSideTestData(dataEntries);
        test.execute();
        assertThat(test.getResult(), is(equalTo(TestResult.FAIL)));
    }

    @Test
    public void verifyExecuteOneEntryPass() {
        ServerSideLessThanTest test = new ServerSideLessThanTest.Builder("Test #1", 10001).withDescription("Verify CPU utilisation").withHostAndMetric("192.168.0.12 CPU").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.CPU_ENTRY_10000);
        PerfMonEntries dataEntries = PerfMonEntries.fromList(testData);
        TestData.getInstance().addServerSideTestData(dataEntries);
        test.execute();
        assertThat(test.getResult(), is(equalTo(TestResult.PASS)));
    }

    @Test
    public void verifyExecutePassNonDefaultLocale() {
        Locale.setDefault(Locale.FRENCH);
        ServerSideLessThanTest test = new ServerSideLessThanTest.Builder("Test #1", 10001).withDescription("Verify CPU utilisation").withHostAndMetric("192.168.0.12 CPU").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.CPU_ENTRY_10000);
        testData.add(LegacyTestData.CPU_ENTRY_10001);
        PerfMonEntries dataEntries = PerfMonEntries.fromList(testData);
        TestData.getInstance().addServerSideTestData(dataEntries);
        test.execute();
        assertThat(test.getResult(), is(equalTo(TestResult.PASS)));
    }

    @Test
    public void verifyExecuteError() {
        ServerSideLessThanTest test = new ServerSideLessThanTest.Builder("Test #1", 10001).withDescription("Verify CPU utilisation").withHostAndMetric("192.168.0.13 CPU").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.CPU_ENTRY_10000);
        testData.add(LegacyTestData.CPU_ENTRY_10001);
        PerfMonEntries dataEntries = PerfMonEntries.fromList(testData);
        TestData.getInstance().addServerSideTestData(dataEntries);
        test.execute();
        assertThat(test.getResult(), is(equalTo(TestResult.ERROR)));
    }

    @Test
    public void testPrintTestExecutionReportPass() {
        String expectedOutput = String.format("Test name:            Test #1%n" +
                "Test type:            serverSideTest%n" +
                "Test description:     Verify CPU utilisation%n" +
                "Host and metric:      192.168.0.12 CPU%n" +
                "Expected result:      Average value < 10001%n" +
                "Actual result:        Average value = 10000%n" +
                "Entries count:        2%n" +
                "Test result:          Pass");

        ServerSideLessThanTest test = new ServerSideLessThanTest.Builder("Test #1", 10001).withDescription("Verify CPU utilisation").withHostAndMetric("192.168.0.12 CPU").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.CPU_ENTRY_10000);
        testData.add(LegacyTestData.CPU_ENTRY_10001);
        PerfMonEntries dataEntries = PerfMonEntries.fromList(testData);
        TestData.getInstance().addServerSideTestData(dataEntries);
        test.execute();
        String output = test.getTestExecutionReport();
        assertThat(output, containsString(expectedOutput));
    }

    @Test
    public void verifyEquals() {
        ServerSideLessThanTest instanceA = new ServerSideLessThanTest.Builder("n", 10000).withDescription("d").withHostAndMetric("hm").build();
        ServerSideLessThanTest instanceB = new ServerSideLessThanTest.Builder("n", 10000).withDescription("d").withHostAndMetric("hm").build();
        ServerSideLessThanTest instanceC = new ServerSideLessThanTest.Builder("n", 10000).withDescription("d").withHostAndMetric("hm").build();
        ServerSideLessThanTest instanceD = new ServerSideLessThanTest.Builder("n", 10000).withHostAndMetric("hm").build();
        ServerSideGreaterThanTest instanceX = new ServerSideGreaterThanTest.Builder("n", 10000).withHostAndMetric("hm").build();
        instanceB.execute();

        EqualsAndHashCodeTester<ServerSideLessThanTest, ServerSideGreaterThanTest> tester = new EqualsAndHashCodeTester<>();
        tester.addEqualObjects(instanceA, instanceB, instanceC);
        tester.addNonEqualObject(instanceD);
        tester.addNotInstanceof(instanceX);
        assertThat(tester.test(), is(true));
    }
}
