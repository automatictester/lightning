package uk.co.automatictester.lightning.core.readers;

import org.testng.annotations.Test;
import uk.co.automatictester.lightning.core.exceptions.*;
import uk.co.automatictester.lightning.core.readers.ConfigReader;
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
        ConfigReader.readTests(TEST_SET_PERCENTILE);
        List<AbstractTest> tests = LightningTestSet.getInstance().get();
        RespTimeNthPercentileTest test = new RespTimeNthPercentileTest.Builder("Test #4", 11245, 80).withDescription("Verify nth percentile").withTransactionName("Search").build();

        assertThat(tests, hasSize(1));
        assertThat(tests.contains(test), is(true));
    }

    @Test
    public void verifyGetTestsMethodStdDevTest() {
        ConfigReader.readTests(TEST_SET_STD_DEV);
        List<AbstractTest> tests = LightningTestSet.getInstance().get();
        RespTimeStdDevTest test = new RespTimeStdDevTest.Builder("Test #2", 500).withDescription("Verify standard deviation").withTransactionName("Search").build();

        assertThat(tests, hasSize(1));
        assertThat(tests.contains(test), is(true));
    }

    @Test
    public void verifyGetTestsMethodPassedTest() {
        ConfigReader.readTests(TEST_SET_PASSED);
        List<AbstractTest> tests = LightningTestSet.getInstance().get();
        PassedTransactionsAbsoluteTest test = new PassedTransactionsAbsoluteTest.Builder("Test #3", 0).withDescription("Verify number of passed tests").withTransactionName("Login").build();

        assertThat(tests, hasSize(1));
        assertThat(tests.contains(test), is(true));
    }

    @Test
    public void verifyGetTestsMethodPassedPercentTest() {
        ConfigReader.readTests(TEST_SET_PASSED_PERCENT);
        List<AbstractTest> tests = LightningTestSet.getInstance().get();
        PassedTransactionsRelativeTest test = new PassedTransactionsRelativeTest.Builder("Test #3", 0).withDescription("Verify percent of passed tests").withTransactionName("Login").build();

        assertThat(tests, hasSize(1));
        assertThat(tests.contains(test), is(true));
    }

    @Test
    public void verifyGetTestsMethodAvgRespTime() {
        ConfigReader.readTests(TEST_SET_AVG_RESP_TIME);
        List<AbstractTest> tests = LightningTestSet.getInstance().get();
        RespTimeAvgTest test = new RespTimeAvgTest.Builder("Test #1", 4000).withDescription("Verify average login times").withTransactionName("Login").build();

        assertThat(tests, hasSize(1));
        assertThat(tests.contains(test), is(true));
    }

    @Test
    public void verifyGetTestsMethodMaxRespTime() {
        ConfigReader.readTests(TEST_SET_MAX_RESP_TIME);
        List<AbstractTest> tests = LightningTestSet.getInstance().get();
        RespTimeMaxTest test = new RespTimeMaxTest.Builder("Test #1", 4000).withDescription("Verify max login times").withTransactionName("Login").build();

        assertThat(tests, hasSize(1));
        assertThat(tests.contains(test), is(true));
    }

    @Test
    public void verifyGetTestsMethodMedianRespTime() {
        ConfigReader.readTests(TEST_SET_MEDIAN);
        List<AbstractTest> tests = LightningTestSet.getInstance().get();
        RespTimeMedianTest test = new RespTimeMedianTest.Builder("Test #4", 11244).withDescription("Verify median response time").withTransactionName("Search").build();

        assertThat(tests, hasSize(1));
        assertThat(tests.contains(test), is(true));
    }

    @Test
    public void verifyGetTestsMethodThroughput() {
        ConfigReader.readTests(TEST_SET_THROUGHPUT);
        List<AbstractTest> tests = LightningTestSet.getInstance().get();
        ThroughputTest test = new ThroughputTest.Builder("Test #2", 2).withDescription("Verify throughput").build();

        assertThat(tests, hasSize(1));
        assertThat(tests.contains(test), is(true));
    }

    @Test
    public void verifyGetTestsMethod_Server_Less() {
        ConfigReader.readTests(TEST_SET_SERVER_LESS);
        List<AbstractTest> tests = LightningTestSet.getInstance().get();
        ServerSideLessThanTest test = new ServerSideLessThanTest.Builder("Test #2", 80000).withDescription("Verify server-side resource utilisation").withHostAndMetric("192.168.0.12 CPU").build();

        assertThat(tests, hasSize(1));
        assertThat(tests.contains(test), is(true));
    }

    @Test
    public void verifyGetTestsMethod_Server_Between() {
        ConfigReader.readTests(TEST_SET_SERVER_BETWEEN);
        List<AbstractTest> tests = LightningTestSet.getInstance().get();
        ServerSideBetweenTest test = new ServerSideBetweenTest.Builder("Test #2", 40000, 80000).withDescription("Verify server-side resource utilisation").withHostAndMetric("192.168.0.12 CPU").build();

        assertThat(tests, hasSize(1));
        assertThat(tests.contains(test), is(true));
    }

    @Test
    public void verifyGetTestsMethod_Server_Greater() {
        ConfigReader.readTests(TEST_SET_SERVER_GREATER);
        List<AbstractTest> tests = LightningTestSet.getInstance().get();
        ServerSideGreaterThanTest test = new ServerSideGreaterThanTest.Builder("Test #2", 20000).withDescription("Verify server-side resource utilisation").withHostAndMetric("192.168.0.12 CPU").build();

        assertThat(tests, hasSize(1));
        assertThat(tests.contains(test), is(true));
    }

    @Test
    public void verifyGetTestsMethodThreeTestsOfTwoKinds() {
        ConfigReader.readTests(TEST_SET_3_0_0);
        List<AbstractTest> tests = LightningTestSet.getInstance().get();

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

        ConfigReader.readTests(TEST_SET_NOT_WELL_FORMED);

        System.setErr(null);
    }

    @Test(expectedExceptions = XMLFileNumberFormatException.class)
    public void verifyGetTestsMethodThrowsXMLFileNumberFormatException() {
        ConfigReader.readTests(TEST_SET_XML_FILE_NUMBER_FORMAT_EXCEPTION);
    }

    @Test(expectedExceptions = XMLFileNoValidSubTypeException.class)
    public void verifyGetTestsMethodThrowsXMLFileNoValidSubTypeException() {
        ConfigReader.readTests(TEST_SET_XML_FILE_NO_VALID_SUB_TYPE_EXCEPTION);
    }

    @Test(expectedExceptions = XMLFileMissingElementValueException.class)
    public void verifyGetTestsMethodThrowsXMLFileMissingElementValueException() {
        ConfigReader.readTests(TEST_SET_XML_FILE_MISSING_ELEMENT_VALUE_EXCEPTION);
    }

    @Test(expectedExceptions = XMLFileMissingElementException.class)
    public void verifyGetTestsMethodThrowsXMLFileMissingElementException() {
        ConfigReader.readTests(TEST_SET_XML_FILE_MISSING_ELEMENT_EXCEPTION);
    }

    @Test(expectedExceptions = XMLFilePercentileException.class)
    public void verifyGetTestsMethodThrowsXMLFilePercentileException() {
        ConfigReader.readTests(TEST_SET_XML_FILE_PERCENTILE_EXCEPTION);
    }

    @Test(expectedExceptions = XMLFileNoTestsException.class)
    public void verifyGetTestsMethodThrowsXMLFileNoTestsException() {
        ConfigReader.readTests(TEST_SET_0_0_0);
    }

    @Test(expectedExceptions = XMLFileNoTestsException.class)
    public void verifyGetTestsMethodThrowsXMLFileNoTestsExceptionOnUnmatchedTestType() {
        ConfigReader.readTests(new File("src/test/resources/xml/unknownTestType.xml"));
    }

}