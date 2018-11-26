package uk.co.automatictester.lightning.core.config;

import org.testng.annotations.Test;
import uk.co.automatictester.lightning.core.exceptions.XMLFileException;
import uk.co.automatictester.lightning.core.state.tests.TestSet;
import uk.co.automatictester.lightning.core.tests.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static uk.co.automatictester.lightning.shared.LegacyTestData.*;

public class LocalFilesystemConfigReaderTest {

    @Test
    public void verifyGetTestsMethodPercentileTest() {
        TestSet testSet = new LocalFileSystemConfigReader().readTests("src/test/resources/xml/nthPercRespTimeTest.xml");
        List<LightningTest> tests = testSet.get();
        RespTimeNthPercentileTest test = new RespTimeNthPercentileTest.Builder("Test #4", 11245, 80).withDescription("Verify nth percentile").withTransactionName("Search").build();

        assertThat(tests, hasSize(1));
        assertThat(tests.contains(test), is(true));
    }

    @Test
    public void verifyGetTestsMethodPercentileTestWithRegexp() {
        TestSet testSet = new LocalFileSystemConfigReader().readTests("src/test/resources/xml/nthPercRespTimeTest_regexp.xml");
        List<LightningTest> tests = testSet.get();
        RespTimeNthPercentileTest test = new RespTimeNthPercentileTest.Builder("Test #4", 11245, 80).withDescription("Verify nth percentile").withTransactionName("Log[a-z]{2,10}").withRegexp().build();

        assertThat(tests, hasSize(1));
        assertThat(tests.contains(test), is(true));
    }

    @Test
    public void verifyGetTestsMethodStdDevTest() {
        TestSet testSet = new LocalFileSystemConfigReader().readTests("src/test/resources/xml/respTimeStdDevTest.xml");
        List<LightningTest> tests = testSet.get();
        RespTimeStdDevTest test = new RespTimeStdDevTest.Builder("Test #2", 500).withDescription("Verify standard deviation").withTransactionName("Search").build();

        assertThat(tests, hasSize(1));
        assertThat(tests.contains(test), is(true));
    }

    @Test
    public void verifyGetTestsMethodStdDevTestWithRegexp() {
        TestSet testSet = new LocalFileSystemConfigReader().readTests("src/test/resources/xml/respTimeStdDevTest_regexp.xml");
        List<LightningTest> tests = testSet.get();
        RespTimeStdDevTest test = new RespTimeStdDevTest.Builder("Test #2", 500).withDescription("Verify standard deviation").withTransactionName("Log[a-z]{2,10}").withRegexp().build();

        assertThat(tests, hasSize(1));
        assertThat(tests.contains(test), is(true));
    }

    @Test
    public void verifyGetTestsMethodPassedTest() {
        TestSet testSet = new LocalFileSystemConfigReader().readTests("src/test/resources/xml/passedTransactionsTest.xml");
        List<LightningTest> tests = testSet.get();
        PassedTransactionsAbsoluteTest test = new PassedTransactionsAbsoluteTest.Builder("Test #3", 0).withDescription("Verify number of passed tests").withTransactionName("Login").build();

        assertThat(tests, hasSize(1));
        assertThat(tests.contains(test), is(true));
    }

    @Test
    public void verifyGetTestsMethodPassedTestWithRegexp() {
        TestSet testSet = new LocalFileSystemConfigReader().readTests("src/test/resources/xml/passedTransactionsTest_regexp.xml");
        List<LightningTest> tests = testSet.get();
        PassedTransactionsAbsoluteTest test = new PassedTransactionsAbsoluteTest.Builder("Test #3", 0).withDescription("Verify number of passed tests").withTransactionName("Log[a-z]{2,10}").withRegexp().build();

        assertThat(tests, hasSize(1));
        assertThat(tests.contains(test), is(true));
    }

    @Test
    public void verifyGetTestsMethodPassedPercentTest() {
        TestSet testSet = new LocalFileSystemConfigReader().readTests("src/test/resources/xml/passedTransactionsPercentTest.xml");
        List<LightningTest> tests = testSet.get();
        PassedTransactionsRelativeTest test = new PassedTransactionsRelativeTest.Builder("Test #3", 0).withDescription("Verify percent of passed tests").withTransactionName("Login").build();

        assertThat(tests, hasSize(1));
        assertThat(tests.contains(test), is(true));
    }

    @Test
    public void verifyGetTestsMethodPassedPercentTestWitgRegexp() {
        TestSet testSet = new LocalFileSystemConfigReader().readTests("src/test/resources/xml/passedTransactionsPercentTest_regexp.xml");
        List<LightningTest> tests = testSet.get();
        PassedTransactionsRelativeTest test = new PassedTransactionsRelativeTest.Builder("Test #3", 0).withDescription("Verify percent of passed tests").withTransactionName("Log[a-z]{2,10}").withRegexp().build();

        assertThat(tests, hasSize(1));
        assertThat(tests.contains(test), is(true));
    }

    @Test
    public void verifyGetTestsMethodAvgRespTime() {
        TestSet testSet = new LocalFileSystemConfigReader().readTests("src/test/resources/xml/avgRespTimeTest.xml");
        List<LightningTest> tests = testSet.get();
        RespTimeAvgTest test = new RespTimeAvgTest.Builder("Test #1", 4000).withDescription("Verify average login times").withTransactionName("Login").build();

        assertThat(tests, hasSize(1));
        assertThat(tests.contains(test), is(true));
    }

    @Test
    public void verifyGetTestsMethodAvgRespTimeWithRegexp() {
        TestSet testSet = new LocalFileSystemConfigReader().readTests("src/test/resources/xml/avgRespTimeTest_regexp.xml");
        List<LightningTest> tests = testSet.get();
        RespTimeAvgTest test = new RespTimeAvgTest.Builder("Test #1", 4000).withDescription("Verify average login times").withTransactionName("Log[a-z]{2,10}").withRegexp().build();

        assertThat(tests, hasSize(1));
        assertThat(tests.contains(test), is(true));
    }

    @Test
    public void verifyGetTestsMethodMaxRespTime() {
        TestSet testSet = new LocalFileSystemConfigReader().readTests("src/test/resources/xml/maxRespTimeTest.xml");
        List<LightningTest> tests = testSet.get();
        RespTimeMaxTest test = new RespTimeMaxTest.Builder("Test #1", 4000).withDescription("Verify max login times").withTransactionName("Login").build();

        assertThat(tests, hasSize(1));
        assertThat(tests.contains(test), is(true));
    }

    @Test
    public void verifyGetTestsMethodMaxRespTimeWithRegexp() {
        TestSet testSet = new LocalFileSystemConfigReader().readTests("src/test/resources/xml/maxRespTimeTest_regexp.xml");
        List<LightningTest> tests = testSet.get();
        RespTimeMaxTest test = new RespTimeMaxTest.Builder("Test #1", 4000).withDescription("Verify max login times").withTransactionName("Log[a-z]{2,10}").withRegexp().build();

        assertThat(tests, hasSize(1));
        assertThat(tests.contains(test), is(true));
    }

    @Test
    public void verifyGetTestsMethodMedianRespTime() {
        TestSet testSet = new LocalFileSystemConfigReader().readTests("src/test/resources/xml/medianRespTimeTest.xml");
        List<LightningTest> tests = testSet.get();
        RespTimeMedianTest test = new RespTimeMedianTest.Builder("Test #4", 11244).withDescription("Verify median response time").withTransactionName("Search").build();

        assertThat(tests, hasSize(1));
        assertThat(tests.contains(test), is(true));
    }

    @Test
    public void verifyGetTestsMethodMedianRespTimeWithRegexp() {
        TestSet testSet = new LocalFileSystemConfigReader().readTests("src/test/resources/xml/medianRespTimeTest_regexp.xml");
        List<LightningTest> tests = testSet.get();
        RespTimeMedianTest test = new RespTimeMedianTest.Builder("Test #4", 11244).withDescription("Verify median response time").withTransactionName("Log[a-z]{2,10}").withRegexp().build();

        assertThat(tests, hasSize(1));
        assertThat(tests.contains(test), is(true));
    }

    @Test
    public void verifyGetTestsMethodThroughput() {
        TestSet testSet = new LocalFileSystemConfigReader().readTests("src/test/resources/xml/throughputTest.xml");
        List<LightningTest> tests = testSet.get();
        ThroughputTest test = new ThroughputTest.Builder("Test #2", 2).withDescription("Verify throughput").build();

        assertThat(tests, hasSize(1));
        assertThat(tests.contains(test), is(true));
    }

    @Test
    public void verifyGetTestsMethodThroughputWithTransactionNameAndRegexp() {
        TestSet testSet = new LocalFileSystemConfigReader().readTests("src/test/resources/xml/throughputTest_regexp.xml");
        List<LightningTest> tests = testSet.get();
        ThroughputTest test = new ThroughputTest.Builder("Test #2", 2).withDescription("Verify throughput").withTransactionName("Log[a-z]{2,10}").withRegexp().build();

        assertThat(tests, hasSize(1));
        assertThat(tests.contains(test), is(true));
    }

    @Test
    public void verifyGetTestsMethod_Server_Less() {
        TestSet testSet = new LocalFileSystemConfigReader().readTests("src/test/resources/xml/serverSideTest_lessThan.xml");
        List<LightningTest> tests = testSet.get();
        ServerSideLessThanTest test = new ServerSideLessThanTest.Builder("Test #2", 80000).withDescription("Verify server-side resource utilisation").withHostAndMetric("192.168.0.12 CPU").build();

        assertThat(tests, hasSize(1));
        assertThat(tests.contains(test), is(true));
    }

    @Test
    public void verifyGetTestsMethod_Server_Between() {
        TestSet testSet = new LocalFileSystemConfigReader().readTests("src/test/resources/xml/serverSideTest_between.xml");
        List<LightningTest> tests = testSet.get();
        ServerSideBetweenTest test = new ServerSideBetweenTest.Builder("Test #2", 40000, 80000).withDescription("Verify server-side resource utilisation").withHostAndMetric("192.168.0.12 CPU").build();

        assertThat(tests, hasSize(1));
        assertThat(tests.contains(test), is(true));
    }

    @Test
    public void verifyGetTestsMethod_Server_Greater() {
        TestSet testSet = new LocalFileSystemConfigReader().readTests("src/test/resources/xml/serverSideTest_greaterThan.xml");
        List<LightningTest> tests = testSet.get();
        ServerSideGreaterThanTest test = new ServerSideGreaterThanTest.Builder("Test #2", 20000).withDescription("Verify server-side resource utilisation").withHostAndMetric("192.168.0.12 CPU").build();

        assertThat(tests, hasSize(1));
        assertThat(tests.contains(test), is(true));
    }

    @Test
    public void verifyGetTestsMethodThreeTestsOfTwoKinds() {
        TestSet testSet = new LocalFileSystemConfigReader().readTests("src/test/resources/xml/3_0_0.xml");
        List<LightningTest> tests = testSet.get();

        assertThat(tests, hasSize(3));
        assertThat(tests.contains(PASSED_TRANSACTIONS_TEST_3_0_0_A), is(true));
        assertThat(tests.contains(PASSED_TRANSACTIONS_TEST_3_0_0_B), is(true));
        assertThat(tests.contains(RESP_TIME_PERC_TEST_3_0_0_C), is(true));
    }

    @Test(expectedExceptions = XMLFileException.class)
    public void verifyGetTestsMethodThrowsXMLFileLoadingException() {
        // suppress error output - coming NOT from own code
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(outContent));

        new LocalFileSystemConfigReader().readTests("src/test/resources/xml/not_well_formed.xml");

        System.setErr(null);
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void verifyGetTestsMethodThrowsXMLFileNoTestsException() {
        new LocalFileSystemConfigReader().readTests("src/test/resources/xml/0_0_0.xml");
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void verifyGetTestsMethodThrowsXMLFileNoTestsExceptionOnOnlyUnmatchedTest() {
        new LocalFileSystemConfigReader().readTests("src/test/resources/xml/unknownTestType.xml");
    }

    @Test
    public void verifyGetTestsMethodIgnoresUnmatchedTest() {
        TestSet testSet = new LocalFileSystemConfigReader().readTests("src/test/resources/xml/knownAndUnknownTestType.xml");
        List<LightningTest> tests = testSet.get();
        assertThat(tests, hasSize(1));
    }

}