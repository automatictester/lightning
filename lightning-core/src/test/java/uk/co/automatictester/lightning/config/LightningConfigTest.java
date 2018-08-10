package uk.co.automatictester.lightning.config;

import org.testng.annotations.Test;
import uk.co.automatictester.lightning.enums.ServerSideTestType;
import uk.co.automatictester.lightning.exceptions.*;
import uk.co.automatictester.lightning.tests.*;
import uk.co.automatictester.lightning.utils.Percent;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static uk.co.automatictester.lightning.shared.TestData.*;

public class LightningConfigTest {

    @Test
    public void verifyGetTestsMethodPercentileTest() {
        LightningConfig xmlFileReader = new LightningConfig();
        xmlFileReader.readTests(TEST_SET_PERCENTILE);
        List<ClientSideTest> tests = xmlFileReader.getClientSideTests();
        RespTimeNthPercentileTest test = new RespTimeNthPercentileTest.Builder("Test #4", 11245, 80).withDescription("Verify nth percentile").withTransactionName("Search").build();

        assertThat(tests, hasSize(1));
        assertThat(tests.contains(test), is(true));
    }

    @Test
    public void verifyGetTestsMethodStdDevTest() {
        LightningConfig xmlFileReader = new LightningConfig();
        xmlFileReader.readTests(TEST_SET_STD_DEV);
        List<ClientSideTest> tests = xmlFileReader.getClientSideTests();
        RespTimeStdDevTest test = new RespTimeStdDevTest("Test #2", "respTimeStdDevTest", "Verify standard deviation", "Search", 500);

        assertThat(tests, hasSize(1));
        assertThat(tests.contains(test), is(true));
    }

    @Test
    public void verifyGetTestsMethodPassedTest() {
        LightningConfig xmlFileReader = new LightningConfig();
        xmlFileReader.readTests(TEST_SET_PASSED);
        List<ClientSideTest> tests = xmlFileReader.getClientSideTests();
        PassedTransactionsTest test = new PassedTransactionsTest("Test #3", "passedTransactionsTest", "Verify number of passed tests", "Login", 0);

        assertThat(tests, hasSize(1));
        assertThat(tests.contains(test), is(true));
    }

    @Test
    public void verifyGetTestsMethodPassedPercentTest() {
        LightningConfig xmlFileReader = new LightningConfig();
        xmlFileReader.readTests(TEST_SET_PASSED_PERCENT);
        List<ClientSideTest> tests = xmlFileReader.getClientSideTests();
        PassedTransactionsTest test = new PassedTransactionsTest("Test #3", "passedTransactionsTest", "Verify percent of passed tests", "Login", new Percent(0));

        assertThat(tests, hasSize(1));
        assertThat(tests.contains(test), is(true));
    }

    @Test
    public void verifyGetTestsMethodAvgRespTime() {
        LightningConfig xmlFileReader = new LightningConfig();
        xmlFileReader.readTests(TEST_SET_AVG_RESP_TIME);
        List<ClientSideTest> tests = xmlFileReader.getClientSideTests();
        RespTimeAvgTest test = new RespTimeAvgTest.Builder("Test #1", 4000).withDescription("Verify average login times").withTransactionName("Login").build();

        assertThat(tests, hasSize(1));
        assertThat(tests.contains(test), is(true));
    }

    @Test
    public void verifyGetTestsMethodMaxRespTime() {
        LightningConfig xmlFileReader = new LightningConfig();
        xmlFileReader.readTests(TEST_SET_MAX_RESP_TIME);
        List<ClientSideTest> tests = xmlFileReader.getClientSideTests();
        RespTimeMaxTest test = new RespTimeMaxTest.Builder("Test #1", 4000).withDescription("Verify max login times").withTransactionName("Login").build();

        assertThat(tests, hasSize(1));
        assertThat(tests.contains(test), is(true));
    }

    @Test
    public void verifyGetTestsMethodMedianRespTime() {
        LightningConfig xmlFileReader = new LightningConfig();
        xmlFileReader.readTests(TEST_SET_MEDIAN);
        List<ClientSideTest> tests = xmlFileReader.getClientSideTests();
        RespTimeMedianTest test = new RespTimeMedianTest("Test #4", "medianRespTimeTest", "Verify median response time", "Search", 11244);

        assertThat(tests, hasSize(1));
        assertThat(tests.contains(test), is(true));
    }

    @Test
    public void verifyGetTestsMethodThroughput() {
        LightningConfig xmlFileReader = new LightningConfig();
        xmlFileReader.readTests(TEST_SET_THROUGHPUT);
        List<ClientSideTest> tests = xmlFileReader.getClientSideTests();
        ThroughputTest test = new ThroughputTest("Test #2", "throughputTest", "Verify throughput", null, 2);

        assertThat(tests, hasSize(1));
        assertThat(tests.contains(test), is(true));
    }

    @Test
    public void verifyGetTestsMethod_Server_Less() {
        LightningConfig xmlFileReader = new LightningConfig();
        xmlFileReader.readTests(TEST_SET_SERVER_LESS);
        List<ServerSideTest> tests = xmlFileReader.getServerSideTests();
        ServerSideTest test = new ServerSideTest("Test #2", "serverSideTest", ServerSideTestType.LESS_THAN, "Verify server-side resource utilisation", "192.168.0.12 CPU", 80000);

        assertThat(tests, hasSize(1));
        assertThat(tests.contains(test), is(true));
    }

    @Test
    public void verifyGetTestsMethod_Server_Between() {
        LightningConfig xmlFileReader = new LightningConfig();
        xmlFileReader.readTests(TEST_SET_SERVER_BETWEEN);
        List<ServerSideTest> tests = xmlFileReader.getServerSideTests();
        ServerSideTest test = new ServerSideTest("Test #2", "serverSideTest", ServerSideTestType.BETWEEN, "Verify server-side resource utilisation", "192.168.0.12 CPU", 40000, 80000);

        assertThat(tests, hasSize(1));
        assertThat(tests.contains(test), is(true));
    }

    @Test
    public void verifyGetTestsMethod_Server_Greater() {
        LightningConfig xmlFileReader = new LightningConfig();
        xmlFileReader.readTests(TEST_SET_SERVER_GREATER);
        List<ServerSideTest> tests = xmlFileReader.getServerSideTests();
        ServerSideTest test = new ServerSideTest("Test #2", "serverSideTest", ServerSideTestType.GREATER_THAN, "Verify server-side resource utilisation", "192.168.0.12 CPU", 20000);

        assertThat(tests, hasSize(1));
        assertThat(tests.contains(test), is(true));
    }

    @Test
    public void verifyGetTestsMethodThreeTestsOfTwoKinds() {
        LightningConfig xmlFileReader = new LightningConfig();
        xmlFileReader.readTests(TEST_SET_3_0_0);
        List<ClientSideTest> tests = xmlFileReader.getClientSideTests();

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
        new LightningConfig().readTests(TEST_SET_XML_FILE_NO_VALID_SUB_TYPE_EXCAPTION);
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

}