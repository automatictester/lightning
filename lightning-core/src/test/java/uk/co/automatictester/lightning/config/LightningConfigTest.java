package uk.co.automatictester.lightning.config;

import org.testng.annotations.Test;
import uk.co.automatictester.lightning.enums.ServerSideTestType;
import uk.co.automatictester.lightning.exceptions.*;
import uk.co.automatictester.lightning.tests.*;
import uk.co.automatictester.lightning.utils.Percent;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static uk.co.automatictester.lightning.shared.TestData.*;

public class LightningConfigTest {

    @Test
    public void verifyGetTestsMethodPercentileTest() {
        LightningConfig lightningConfig = new LightningConfig();
        lightningConfig.readTests(TEST_SET_PERCENTILE);
        List<ClientSideTest> tests = lightningConfig.getClientSideTests();
        RespTimeNthPercentileTest test = new RespTimeNthPercentileTest.Builder("Test #4", 11245, 80).withDescription("Verify nth percentile").withTransactionName("Search").build();

        assertThat(tests, hasSize(1));
        assertThat(tests.contains(test), is(true));
    }

    @Test
    public void verifyGetTestsMethodStdDevTest() {
        LightningConfig lightningConfig = new LightningConfig();
        lightningConfig.readTests(TEST_SET_STD_DEV);
        List<ClientSideTest> tests = lightningConfig.getClientSideTests();
        RespTimeStdDevTest test = new RespTimeStdDevTest.Builder("Test #2", 500).withDescription("Verify standard deviation").withTransactionName("Search").build();

        assertThat(tests, hasSize(1));
        assertThat(tests.contains(test), is(true));
    }

    @Test
    public void verifyGetTestsMethodPassedTest() {
        LightningConfig lightningConfig = new LightningConfig();
        lightningConfig.readTests(TEST_SET_PASSED);
        List<ClientSideTest> tests = lightningConfig.getClientSideTests();
        PassedTransactionsTest test = new PassedTransactionsTest.Builder("Test #3", 0).withDescription("Verify number of passed tests").withTransactionName("Login").build();

        assertThat(tests, hasSize(1));
        assertThat(tests.contains(test), is(true));
    }

    @Test
    public void verifyGetTestsMethodPassedPercentTest() {
        LightningConfig lightningConfig = new LightningConfig();
        lightningConfig.readTests(TEST_SET_PASSED_PERCENT);
        List<ClientSideTest> tests = lightningConfig.getClientSideTests();
        PassedTransactionsTest test = new PassedTransactionsTest.Builder("Test #3", new Percent(0)).withDescription("Verify percent of passed tests").withTransactionName("Login").build();

        assertThat(tests, hasSize(1));
        assertThat(tests.contains(test), is(true));
    }

    @Test
    public void verifyGetTestsMethodAvgRespTime() {
        LightningConfig lightningConfig = new LightningConfig();
        lightningConfig.readTests(TEST_SET_AVG_RESP_TIME);
        List<ClientSideTest> tests = lightningConfig.getClientSideTests();
        RespTimeAvgTest test = new RespTimeAvgTest.Builder("Test #1", 4000).withDescription("Verify average login times").withTransactionName("Login").build();

        assertThat(tests, hasSize(1));
        assertThat(tests.contains(test), is(true));
    }

    @Test
    public void verifyGetTestsMethodMaxRespTime() {
        LightningConfig lightningConfig = new LightningConfig();
        lightningConfig.readTests(TEST_SET_MAX_RESP_TIME);
        List<ClientSideTest> tests = lightningConfig.getClientSideTests();
        RespTimeMaxTest test = new RespTimeMaxTest.Builder("Test #1", 4000).withDescription("Verify max login times").withTransactionName("Login").build();

        assertThat(tests, hasSize(1));
        assertThat(tests.contains(test), is(true));
    }

    @Test
    public void verifyGetTestsMethodMedianRespTime() {
        LightningConfig lightningConfig = new LightningConfig();
        lightningConfig.readTests(TEST_SET_MEDIAN);
        List<ClientSideTest> tests = lightningConfig.getClientSideTests();
        RespTimeMedianTest test = new RespTimeMedianTest.Builder("Test #4", 11244).withDescription("Verify median response time").withTransactionName("Search").build();

        assertThat(tests, hasSize(1));
        assertThat(tests.contains(test), is(true));
    }

    @Test
    public void verifyGetTestsMethodThroughput() {
        LightningConfig lightningConfig = new LightningConfig();
        lightningConfig.readTests(TEST_SET_THROUGHPUT);
        List<ClientSideTest> tests = lightningConfig.getClientSideTests();
        ThroughputTest test = new ThroughputTest.Builder("Test #2", 2).withDescription("Verify throughput").build();

        assertThat(tests, hasSize(1));
        assertThat(tests.contains(test), is(true));
    }

    @Test
    public void verifyGetTestsMethod_Server_Less() {
        LightningConfig lightningConfig = new LightningConfig();
        lightningConfig.readTests(TEST_SET_SERVER_LESS);
        List<ServerSideTest> tests = lightningConfig.getServerSideTests();
        ServerSideTest test = new ServerSideTest.Builder("Test #2", ServerSideTestType.LESS_THAN, 80000).withDescription("Verify server-side resource utilisation").withHostAndMetric("192.168.0.12 CPU").build();

        assertThat(tests, hasSize(1));
        assertThat(tests.contains(test), is(true));
    }

    @Test
    public void verifyGetTestsMethod_Server_Between() {
        LightningConfig lightningConfig = new LightningConfig();
        lightningConfig.readTests(TEST_SET_SERVER_BETWEEN);
        List<ServerSideTest> tests = lightningConfig.getServerSideTests();
        ServerSideTest test = new ServerSideTest.Builder("Test #2", ServerSideTestType.BETWEEN, 40000, 80000).withDescription("Verify server-side resource utilisation").withHostAndMetric("192.168.0.12 CPU").build();

        assertThat(tests, hasSize(1));
        assertThat(tests.contains(test), is(true));
    }

    @Test
    public void verifyGetTestsMethod_Server_Greater() {
        LightningConfig lightningConfig = new LightningConfig();
        lightningConfig.readTests(TEST_SET_SERVER_GREATER);
        List<ServerSideTest> tests = lightningConfig.getServerSideTests();
        ServerSideTest test = new ServerSideTest.Builder("Test #2", ServerSideTestType.GREATER_THAN, 20000).withDescription("Verify server-side resource utilisation").withHostAndMetric("192.168.0.12 CPU").build();

        assertThat(tests, hasSize(1));
        assertThat(tests.contains(test), is(true));
    }

    @Test
    public void verifyGetTestsMethodThreeTestsOfTwoKinds() {
        LightningConfig lightningConfig = new LightningConfig();
        lightningConfig.readTests(TEST_SET_3_0_0);
        List<ClientSideTest> tests = lightningConfig.getClientSideTests();

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

        new LightningConfig().readTests(TEST_SET_NOT_WELL_FORMED);

        System.setErr(null);
    }

    @Test(expectedExceptions = XMLFileNumberFormatException.class)
    public void verifyGetTestsMethodThrowsXMLFileNumberFormatException() {
        new LightningConfig().readTests(TEST_SET_XML_FILE_NUMBER_FORMAT_EXCEPTION);
    }

    @Test(expectedExceptions = XMLFileNoValidSubTypeException.class)
    public void verifyGetTestsMethodThrowsXMLFileNoValidSubTypeException() {
        new LightningConfig().readTests(TEST_SET_XML_FILE_NO_VALID_SUB_TYPE_EXCEPTION);
    }

    @Test(expectedExceptions = XMLFileMissingElementValueException.class)
    public void verifyGetTestsMethodThrowsXMLFileMissingElementValueException() {
        new LightningConfig().readTests(TEST_SET_XML_FILE_MISSING_ELEMENT_VALUE_EXCEPTION);
    }

    @Test(expectedExceptions = XMLFileMissingElementException.class)
    public void verifyGetTestsMethodThrowsXMLFileMissingElementException() {
        new LightningConfig().readTests(TEST_SET_XML_FILE_MISSING_ELEMENT_EXCEPTION);
    }

    @Test(expectedExceptions = XMLFilePercentileException.class)
    public void verifyGetTestsMethodThrowsXMLFilePercentileException() {
        new LightningConfig().readTests(TEST_SET_XML_FILE_PERCENTILE_EXCEPTION);
    }

    @Test(expectedExceptions = XMLFileNoTestsException.class)
    public void verifyGetTestsMethodThrowsXMLFileNoTestsException() {
        new LightningConfig().readTests(TEST_SET_0_0_0);
    }

    @Test(expectedExceptions = XMLFileNoTestsException.class)
    public void verifyGetTestsMethodThrowsXMLFileNoTestsExceptionOnUnmatchedTestType() {
        new LightningConfig().readTests(new File("src/test/resources/xml/unknownTestType.xml"));
    }

}