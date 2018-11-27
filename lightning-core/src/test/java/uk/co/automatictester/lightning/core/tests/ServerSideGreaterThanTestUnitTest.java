package uk.co.automatictester.lightning.core.tests;

import org.testng.annotations.Test;
import uk.co.automatictester.lightning.core.enums.TestResult;
import uk.co.automatictester.lightning.core.state.data.TestData;
import uk.co.automatictester.lightning.shared.LegacyTestData;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;

public class ServerSideGreaterThanTestUnitTest {

    @Test
    public void verifyExecutePass() {
        ServerSideGreaterThanTest test = new ServerSideGreaterThanTest.Builder("Test #1", 27499).withDescription("Verify CPU utilisation").withHostAndMetric("192.168.0.12 CPU").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.CPU_ENTRY_25000);
        testData.add(LegacyTestData.CPU_ENTRY_30000);
        TestData.getInstance().addServerSideTestData(testData);
        test.execute();
        assertThat(test.result(), is(equalTo(TestResult.PASS)));
    }

    @Test
    public void verifyExecuteFail() {
        ServerSideGreaterThanTest test = new ServerSideGreaterThanTest.Builder("Test #1", 12501).withDescription("Verify CPU utilisation").withHostAndMetric("192.168.0.12 CPU").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.CPU_ENTRY_10000);
        testData.add(LegacyTestData.CPU_ENTRY_15000);
        TestData.getInstance().addServerSideTestData(testData);
        test.execute();
        assertThat(test.result(), is(equalTo(TestResult.FAIL)));
    }

    @Test
    public void verifyEquals() {
        ServerSideGreaterThanTest instanceA = new ServerSideGreaterThanTest.Builder("n", 10000).withDescription("d").withHostAndMetric("hm").build();
        ServerSideGreaterThanTest instanceB = new ServerSideGreaterThanTest.Builder("n", 10000).withDescription("d").withHostAndMetric("hm").build();
        ServerSideGreaterThanTest instanceC = new ServerSideGreaterThanTest.Builder("n", 10000).withDescription("d").withHostAndMetric("hm").build();
        ServerSideGreaterThanTest instanceD = new ServerSideGreaterThanTest.Builder("n", 10000).withHostAndMetric("hm").build();
        ServerSideLessThanTest instanceX = new ServerSideLessThanTest.Builder("n", 10000).withHostAndMetric("hm").build();
        instanceB.execute();

        EqualsAndHashCodeTester<ServerSideGreaterThanTest, ServerSideLessThanTest> tester = new EqualsAndHashCodeTester<>();
        tester.addEqualObjects(instanceA, instanceB, instanceC);
        tester.addNonEqualObject(instanceD);
        tester.addNotInstanceof(instanceX);
        assertThat(tester.test(), is(true));
    }

    @Test
    public void testToString() {
        ServerSideGreaterThanTest test = new ServerSideGreaterThanTest.Builder("Test #1", 20000).withHostAndMetric("192.168.0.2 CPU").withDescription("d").build();
        assertThat(test.toString(), is(equalTo("Type: serverSideTest, name: Test #1, threshold: 20000, host and metric: 192.168.0.2 CPU, description: d")));
    }
}
