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
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

public class ServerSideTestUnitTest {

    @Test
    public void verifyExecute_LessThan_Pass() {
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
    public void verifyExecute_LessThan_Fail() {
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
    public void verifyExecute_GreaterThan_Pass() {
        ServerSideGreaterThanTest test = new ServerSideGreaterThanTest.Builder("Test #1", 27499).withDescription("Verify CPU utilisation").withHostAndMetric("192.168.0.12 CPU").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.CPU_ENTRY_25000);
        testData.add(LegacyTestData.CPU_ENTRY_30000);
        PerfMonEntries dataEntries = PerfMonEntries.fromList(testData);
        TestData.getInstance().addServerSideTestData(dataEntries);
        test.execute();
        assertThat(test.getResult(), is(equalTo(TestResult.PASS)));
    }

    @Test
    public void verifyExecute_GreaterThan_Fail() {
        ServerSideGreaterThanTest test = new ServerSideGreaterThanTest.Builder("Test #1", 12501).withDescription("Verify CPU utilisation").withHostAndMetric("192.168.0.12 CPU").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.CPU_ENTRY_10000);
        testData.add(LegacyTestData.CPU_ENTRY_15000);
        PerfMonEntries dataEntries = PerfMonEntries.fromList(testData);
        TestData.getInstance().addServerSideTestData(dataEntries);
        test.execute();
        assertThat(test.getResult(), is(equalTo(TestResult.FAIL)));
    }

    @Test
    public void verifyExecute_Between_Pass() {
        ServerSideBetweenTest test = new ServerSideBetweenTest.Builder("Test #1", 20000, 27501).withDescription("Verify CPU utilisation").withHostAndMetric("192.168.0.12 CPU").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.CPU_ENTRY_25000);
        testData.add(LegacyTestData.CPU_ENTRY_30000);
        PerfMonEntries dataEntries = PerfMonEntries.fromList(testData);
        TestData.getInstance().addServerSideTestData(dataEntries);
        test.execute();
        assertThat(test.getResult(), is(equalTo(TestResult.PASS)));
    }

    @Test
    public void verifyExecute_Between_Fail() {
        ServerSideBetweenTest test = new ServerSideBetweenTest.Builder("Test #1", 10000, 12499).withDescription("Verify CPU utilisation").withHostAndMetric("192.168.0.12 CPU").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.CPU_ENTRY_10000);
        testData.add(LegacyTestData.CPU_ENTRY_15000);
        PerfMonEntries dataEntries = PerfMonEntries.fromList(testData);
        TestData.getInstance().addServerSideTestData(dataEntries);
        test.execute();
        assertThat(test.getResult(), is(equalTo(TestResult.FAIL)));
    }

    @Test
    public void verifyExecute_OneEntry_LessThan_Pass() {
        ServerSideLessThanTest test = new ServerSideLessThanTest.Builder("Test #1", 10001).withDescription("Verify CPU utilisation").withHostAndMetric("192.168.0.12 CPU").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.CPU_ENTRY_10000);
        PerfMonEntries dataEntries = PerfMonEntries.fromList(testData);
        TestData.getInstance().addServerSideTestData(dataEntries);
        test.execute();
        assertThat(test.getResult(), is(equalTo(TestResult.PASS)));
    }

    @Test
    public void verifyExecute_LessThan_Pass_NonDefaultLocale() {
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
    public void verifyExecute_LessThan_Error() {
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
    public void verifyIsEqual() {
        ServerSideLessThanTest testA = new ServerSideLessThanTest.Builder("Test #1", 10000).withDescription("Verify CPU utilisation").withHostAndMetric("192.168.0.13 CPU").build();
        ServerSideLessThanTest testB = new ServerSideLessThanTest.Builder("Test #1", 10000).withDescription("Verify CPU utilisation").withHostAndMetric("192.168.0.13 CPU").build();
        assertThat(testA, is(equalTo(testB)));
    }

    @Test
    public void verifyIsNotEqual() {
        ServerSideLessThanTest testA = new ServerSideLessThanTest.Builder("Test #1", 10000).withDescription("Verify CPU utilisation").withHostAndMetric("192.168.0.13 CPU").build();
        ServerSideLessThanTest testB = new ServerSideLessThanTest.Builder("Test #1", 10001).withDescription("Verify CPU utilisation").withHostAndMetric("192.168.0.13 CPU").build();
        assertThat(testA, is(not(equalTo(testB))));
    }

    @Test
    public void verifyIsNotEqualOtherTestType() {
        ServerSideLessThanTest test = new ServerSideLessThanTest.Builder("Test #1", 10000).withDescription("Verify CPU utilisation").withHostAndMetric("192.168.0.13 CPU").build();
        assertThat((Object) test, is(not(equalTo((Object) LegacyTestData.RESP_TIME_PERC_TEST_A))));
    }

}