package uk.co.automatictester.lightning.core.tests;

import org.testng.annotations.Test;
import uk.co.automatictester.lightning.core.data.PerfMonEntries;
import uk.co.automatictester.lightning.core.enums.TestResult;
import uk.co.automatictester.lightning.core.structures.TestData;
import uk.co.automatictester.lightning.shared.LegacyTestData;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;

public class ServerSideBetweenTestUnitTest {

    @Test
    public void verifyExecutePass() {
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
    public void verifyExecuteFail() {
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
    public void verifyEquals() {
        ServerSideBetweenTest instanceA = new ServerSideBetweenTest.Builder("n", 10000, 40000).withDescription("d").withHostAndMetric("hm").build();
        ServerSideBetweenTest instanceB = new ServerSideBetweenTest.Builder("n", 10000, 40000).withDescription("d").withHostAndMetric("hm").build();
        ServerSideBetweenTest instanceC = new ServerSideBetweenTest.Builder("n", 10000, 40000).withDescription("d").withHostAndMetric("hm").build();
        ServerSideBetweenTest instanceD = new ServerSideBetweenTest.Builder("n", 10000, 40000).withHostAndMetric("hm").build();
        ServerSideGreaterThanTest instanceX = new ServerSideGreaterThanTest.Builder("n", 10000).withHostAndMetric("hm").build();
        instanceB.execute();

        EqualsTester<ServerSideBetweenTest, ServerSideGreaterThanTest> tester = new EqualsTester<>();
        tester.addEqualObjects(instanceA, instanceB, instanceC);
        tester.addNonEqualObject(instanceD);
        tester.addNotInstanceof(instanceX);
        assertThat(tester.test(), is(true));
    }
}
