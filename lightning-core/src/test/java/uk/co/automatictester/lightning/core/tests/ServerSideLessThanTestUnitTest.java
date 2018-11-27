package uk.co.automatictester.lightning.core.tests;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import uk.co.automatictester.lightning.core.enums.TestResult;
import uk.co.automatictester.lightning.core.state.data.TestData;
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
        TestData.getInstance().addServerSideTestData(testData);
        test.execute();
        assertThat(test.result(), is(equalTo(TestResult.PASS)));
    }

    @Test
    public void verifyExecuteFail() {
        ServerSideLessThanTest test = new ServerSideLessThanTest.Builder("Test #1", 27500).withDescription("Verify CPU utilisation").withHostAndMetric("192.168.0.12 CPU").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.CPU_ENTRY_25000);
        testData.add(LegacyTestData.CPU_ENTRY_30000);
        TestData.getInstance().addServerSideTestData(testData);
        test.execute();
        assertThat(test.result(), is(equalTo(TestResult.FAIL)));
    }

    @DataProvider(name = "locale")
    private Object[][] locale() {
        return new Object[][]{
                {Locale.ENGLISH},
                {Locale.FRENCH},
        };
    }

    @Test(dataProvider = "locale")
    public void verifyExecuteOneEntryPass(Locale locale) {
        Locale.setDefault(locale);

        ServerSideLessThanTest test = new ServerSideLessThanTest.Builder("Test #1", 10001).withDescription("Verify CPU utilisation").withHostAndMetric("192.168.0.12 CPU").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.CPU_ENTRY_10000);
        testData.add(LegacyTestData.CPU_ENTRY_10001);
        TestData.getInstance().addServerSideTestData(testData);
        test.execute();
        assertThat(test.result(), is(equalTo(TestResult.PASS)));
    }

    @Test
    public void verifyExecuteError() {
        ServerSideLessThanTest test = new ServerSideLessThanTest.Builder("Test #1", 10001).withDescription("Verify CPU utilisation").withHostAndMetric("192.168.0.13 CPU").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.CPU_ENTRY_10000);
        testData.add(LegacyTestData.CPU_ENTRY_10001);
        TestData.getInstance().addServerSideTestData(testData);
        test.execute();
        assertThat(test.result(), is(equalTo(TestResult.ERROR)));
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
        TestData.getInstance().addServerSideTestData(testData);
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

    @Test
    public void testToString() {
        ServerSideLessThanTest test = new ServerSideLessThanTest.Builder("Test #1", 20000).withHostAndMetric("192.168.0.2 CPU").withDescription("d").build();
        assertThat(test.toString(), is(equalTo("Type: serverSideTest, name: Test #1, threshold: 20000, host and metric: 192.168.0.2 CPU, description: d")));
    }
}
