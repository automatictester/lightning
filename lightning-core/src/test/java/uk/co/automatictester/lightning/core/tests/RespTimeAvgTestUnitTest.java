package uk.co.automatictester.lightning.core.tests;

import org.hamcrest.MatcherAssert;
import org.testng.annotations.Test;
import uk.co.automatictester.lightning.core.data.JMeterTransactions;
import uk.co.automatictester.lightning.core.enums.TestResult;
import uk.co.automatictester.lightning.core.structures.TestData;
import uk.co.automatictester.lightning.core.tests.base.ClientSideTest;
import uk.co.automatictester.lightning.shared.LegacyTestData;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

public class RespTimeAvgTestUnitTest {

    @Test
    public void verifyExecutePass() {
        RespTimeAvgTest test = new RespTimeAvgTest.Builder("Test #1", 800).withDescription("Verify response times").withTransactionName("Search").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.SEARCH_800_SUCCESS);
        JMeterTransactions jmeterTransactions = JMeterTransactions.from(testData);

        TestData.addClientSideTestData(jmeterTransactions);
        test.execute();
        assertThat(test.getResult(), is(equalTo(TestResult.PASS)));
    }

    @Test
    public void verifyExecutePassOnNonDefaultLocale() {
        Locale.setDefault(Locale.FRENCH);

        RespTimeAvgTest test = new RespTimeAvgTest.Builder("Test #1", 6010).withDescription("Verify response times").withTransactionName("Search").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.SEARCH_800_SUCCESS);
        testData.add(LegacyTestData.SEARCH_11221_SUCCESS);
        JMeterTransactions jmeterTransactions = JMeterTransactions.from(testData);

        TestData.addClientSideTestData(jmeterTransactions);
        test.execute();
        assertThat(test.getResult(), is(equalTo(TestResult.PASS)));
    }

    @Test
    public void verifyExecuteAllTransactionsPass() {
        RespTimeAvgTest test = new RespTimeAvgTest.Builder("Test #1", 900).withDescription("Verify response times").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.SEARCH_800_SUCCESS);
        testData.add(LegacyTestData.LOGIN_1000_SUCCESS);
        JMeterTransactions jmeterTransactions = JMeterTransactions.from(testData);

        TestData.addClientSideTestData(jmeterTransactions);
        test.execute();
        assertThat(test.getResult(), is(equalTo(TestResult.PASS)));
    }

    @Test
    public void verifyExecuteFail() {
        RespTimeAvgTest test = new RespTimeAvgTest.Builder("Test #1", 11220).withDescription("Verify response times").withTransactionName("Search").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.SEARCH_11221_SUCCESS);
        JMeterTransactions jmeterTransactions = JMeterTransactions.from(testData);

        TestData.addClientSideTestData(jmeterTransactions);
        test.execute();
        assertThat(test.getResult(), is(equalTo(TestResult.FAIL)));
    }

    @Test
    public void verifyExecuteAllTransactionsFail() {
        RespTimeAvgTest test = new RespTimeAvgTest.Builder("Test #1", 899).withDescription("Verify response times").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.SEARCH_800_SUCCESS);
        testData.add(LegacyTestData.LOGIN_1000_SUCCESS);
        JMeterTransactions jmeterTransactions = JMeterTransactions.from(testData);

        TestData.addClientSideTestData(jmeterTransactions);
        test.execute();
        assertThat(test.getResult(), is(equalTo(TestResult.FAIL)));
    }

    @Test
    public void verifyExecuteError() {
        RespTimeAvgTest test = new RespTimeAvgTest.Builder("Test #1", 800).withDescription("Verify response times").withTransactionName("nonexistent").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.SEARCH_11221_SUCCESS);
        JMeterTransactions jmeterTransactions = JMeterTransactions.from(testData);

        TestData.addClientSideTestData(jmeterTransactions);
        test.execute();
        assertThat(test.getResult(), is(equalTo(TestResult.ERROR)));
        assertThat(test.getActualResultDescription(), is(equalTo("No transactions with label equal to 'nonexistent' found in CSV file")));
    }

    @Test
    public void verifyIsEqual() {
        MatcherAssert.assertThat(LegacyTestData.AVG_RESP_TIME_TEST_A, is(equalTo(LegacyTestData.AVG_RESP_TIME_TEST_A)));
    }

    @Test
    public void verifyIsNotEqualOtherTestType() {
        assertThat(LegacyTestData.AVG_RESP_TIME_TEST_A, is(not(equalTo((ClientSideTest) LegacyTestData.RESP_TIME_PERC_TEST_A))));
    }

    @Test
    public void verifyIsNotEqual() {
        MatcherAssert.assertThat(LegacyTestData.AVG_RESP_TIME_TEST_A, is(not(equalTo(LegacyTestData.AVG_RESP_TIME_TEST_B))));
    }

    @Test
    public void testPrintTestExecutionReport() {
        RespTimeAvgTest test = new RespTimeAvgTest.Builder("my name", 800).withDescription("my description").withTransactionName("Search").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.SEARCH_800_SUCCESS);
        JMeterTransactions jmeterTransactions = JMeterTransactions.from(testData);

        String expectedOutput = String.format("Test name:            my name%n" +
                "Test type:            avgRespTimeTest%n" +
                "Test description:     my description%n" +
                "Transaction name:     Search%n" +
                "Expected result:      Average response time <= 800%n" +
                "Actual result:        Average response time = 800%n" +
                "Transaction count:    1%n" +
                "Longest transactions: [800]%n" +
                "Test result:          Pass");

        TestData.addClientSideTestData(jmeterTransactions);
        test.execute();
        String output = test.getTestExecutionReport();
        assertThat(output, containsString(expectedOutput));
    }
}