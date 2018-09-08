package uk.co.automatictester.lightning.core.readers;

import org.testng.annotations.Test;
import uk.co.automatictester.lightning.core.exceptions.XMLFileException;
import uk.co.automatictester.lightning.core.state.tests.LightningTestSet;
import uk.co.automatictester.lightning.core.tests.*;
import uk.co.automatictester.lightning.core.tests.base.AbstractTest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static uk.co.automatictester.lightning.shared.LegacyTestData.*;

public class ConfigReaderTest {

    @Test
    public void verifyGetTestsMethodPercentileTest() {
        LightningTestSet testSet = new ConfigReader().readTests(TEST_SET_PERCENTILE);
        List<AbstractTest> tests = testSet.get();
        RespTimeNthPercentileTest test = new RespTimeNthPercentileTest.Builder("Test #4", 11245, 80).withDescription("Verify nth percentile").withTransactionName("Search").build();

        assertThat(tests, hasSize(1));
        assertThat(tests.contains(test), is(true));
    }

    @Test
    public void verifyGetTestsMethodStdDevTest() {
        LightningTestSet testSet = new ConfigReader().readTests(TEST_SET_STD_DEV);
        List<AbstractTest> tests = testSet.get();
        RespTimeStdDevTest test = new RespTimeStdDevTest.Builder("Test #2", 500).withDescription("Verify standard deviation").withTransactionName("Search").build();

        assertThat(tests, hasSize(1));
        assertThat(tests.contains(test), is(true));
    }

    @Test
    public void verifyGetTestsMethodPassedTest() {
        LightningTestSet testSet = new ConfigReader().readTests(TEST_SET_PASSED);
        List<AbstractTest> tests = testSet.get();
        PassedTransactionsAbsoluteTest test = new PassedTransactionsAbsoluteTest.Builder("Test #3", 0).withDescription("Verify number of passed tests").withTransactionName("Login").build();

        assertThat(tests, hasSize(1));
        assertThat(tests.contains(test), is(true));
    }

    @Test
    public void verifyGetTestsMethodPassedPercentTest() {
        LightningTestSet testSet = new ConfigReader().readTests(TEST_SET_PASSED_PERCENT);
        List<AbstractTest> tests = testSet.get();
        PassedTransactionsRelativeTest test = new PassedTransactionsRelativeTest.Builder("Test #3", 0).withDescription("Verify percent of passed tests").withTransactionName("Login").build();

        assertThat(tests, hasSize(1));
        assertThat(tests.contains(test), is(true));
    }

    @Test
    public void verifyGetTestsMethodAvgRespTime() {
        LightningTestSet testSet = new ConfigReader().readTests(TEST_SET_AVG_RESP_TIME);
        List<AbstractTest> tests = testSet.get();
        RespTimeAvgTest test = new RespTimeAvgTest.Builder("Test #1", 4000).withDescription("Verify average login times").withTransactionName("Login").build();

        assertThat(tests, hasSize(1));
        assertThat(tests.contains(test), is(true));
    }

    @Test
    public void verifyGetTestsMethodMaxRespTime() {
        LightningTestSet testSet = new ConfigReader().readTests(TEST_SET_MAX_RESP_TIME);
        List<AbstractTest> tests = testSet.get();
        RespTimeMaxTest test = new RespTimeMaxTest.Builder("Test #1", 4000).withDescription("Verify max login times").withTransactionName("Login").build();

        assertThat(tests, hasSize(1));
        assertThat(tests.contains(test), is(true));
    }

    @Test
    public void verifyGetTestsMethodMedianRespTime() {
        LightningTestSet testSet = new ConfigReader().readTests(TEST_SET_MEDIAN);
        List<AbstractTest> tests = testSet.get();
        RespTimeMedianTest test = new RespTimeMedianTest.Builder("Test #4", 11244).withDescription("Verify median response time").withTransactionName("Search").build();

        assertThat(tests, hasSize(1));
        assertThat(tests.contains(test), is(true));
    }

    @Test
    public void verifyGetTestsMethodThroughput() {
        LightningTestSet testSet = new ConfigReader().readTests(TEST_SET_THROUGHPUT);
        List<AbstractTest> tests = testSet.get();
        ThroughputTest test = new ThroughputTest.Builder("Test #2", 2).withDescription("Verify throughput").build();

        assertThat(tests, hasSize(1));
        assertThat(tests.contains(test), is(true));
    }

    @Test
    public void verifyGetTestsMethod_Server_Less() {
        LightningTestSet testSet = new ConfigReader().readTests(TEST_SET_SERVER_LESS);
        List<AbstractTest> tests = testSet.get();
        ServerSideLessThanTest test = new ServerSideLessThanTest.Builder("Test #2", 80000).withDescription("Verify server-side resource utilisation").withHostAndMetric("192.168.0.12 CPU").build();

        assertThat(tests, hasSize(1));
        assertThat(tests.contains(test), is(true));
    }

    @Test
    public void verifyGetTestsMethod_Server_Between() {
        LightningTestSet testSet = new ConfigReader().readTests(TEST_SET_SERVER_BETWEEN);
        List<AbstractTest> tests = testSet.get();
        ServerSideBetweenTest test = new ServerSideBetweenTest.Builder("Test #2", 40000, 80000).withDescription("Verify server-side resource utilisation").withHostAndMetric("192.168.0.12 CPU").build();

        assertThat(tests, hasSize(1));
        assertThat(tests.contains(test), is(true));
    }

    @Test
    public void verifyGetTestsMethod_Server_Greater() {
        LightningTestSet testSet = new ConfigReader().readTests(TEST_SET_SERVER_GREATER);
        List<AbstractTest> tests = testSet.get();
        ServerSideGreaterThanTest test = new ServerSideGreaterThanTest.Builder("Test #2", 20000).withDescription("Verify server-side resource utilisation").withHostAndMetric("192.168.0.12 CPU").build();

        assertThat(tests, hasSize(1));
        assertThat(tests.contains(test), is(true));
    }

    @Test
    public void verifyGetTestsMethodThreeTestsOfTwoKinds() {
        LightningTestSet testSet = new ConfigReader().readTests(TEST_SET_3_0_0);
        List<AbstractTest> tests = testSet.get();

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

        new ConfigReader().readTests(TEST_SET_NOT_WELL_FORMED);

        System.setErr(null);
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void verifyGetTestsMethodThrowsXMLFileNoTestsException() {
        new ConfigReader().readTests(new File("src/test/resources/xml/0_0_0.xml"));
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void verifyGetTestsMethodThrowsXMLFileNoTestsExceptionOnOnlyUnmatchedTest() {
        new ConfigReader().readTests(new File("src/test/resources/xml/unknownTestType.xml"));
    }

    @Test
    public void verifyGetTestsMethodIgnoresUnmatchedTest() {
        LightningTestSet testSet = new ConfigReader().readTests(new File("src/test/resources/xml/knownAndUnknownTestType.xml"));
        List<AbstractTest> tests = testSet.get();
        assertThat(tests, hasSize(1));
    }

}