package uk.co.automatictester.lightning.tests;

import org.hamcrest.MatcherAssert;
import org.testng.annotations.Test;
import uk.co.automatictester.lightning.data.JMeterTransactions;
import uk.co.automatictester.lightning.enums.TestResult;
import uk.co.automatictester.lightning.shared.TestData;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

public class RespTimeAvgTestTest {

    @Test
    public void verifyExecutePass() {
        RespTimeAvgTest test = new RespTimeAvgTest("Test #1", "avgRespTimeTest", "Verify response times", "Search", 800);
        List<String[]> testData = new ArrayList<>();
        testData.add(TestData.SEARCH_800_SUCCESS);
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);

        test.execute(jmeterTransactions);
        assertThat(test.getResult(), is(equalTo(TestResult.PASS)));
    }

    @Test
    public void verifyExecutePassOnNonDefaultLocale() {
        Locale.setDefault(Locale.FRENCH);

        RespTimeAvgTest test = new RespTimeAvgTest("Test #1", "avgRespTimeTest", "Verify response times", "Search", 6010);
        List<String[]> testData = new ArrayList<>();
        testData.add(TestData.SEARCH_800_SUCCESS);
        testData.add(TestData.SEARCH_11221_SUCCESS);
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);

        test.execute(jmeterTransactions);
        assertThat(test.getResult(), is(equalTo(TestResult.PASS)));
    }

    @Test
    public void verifyExecuteAllTransactionsPass() {
        RespTimeAvgTest test = new RespTimeAvgTest("Test #1", "avgRespTimeTest", "Verify response times", null, 900);
        List<String[]> testData = new ArrayList<>();
        testData.add(TestData.SEARCH_800_SUCCESS);
        testData.add(TestData.LOGIN_1000_SUCCESS);
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);

        test.execute(jmeterTransactions);
        assertThat(test.getResult(), is(equalTo(TestResult.PASS)));
    }

    @Test
    public void verifyExecuteFail() {
        RespTimeAvgTest test = new RespTimeAvgTest("Test #1", "avgRespTimeTest", "Verify response times", "Search", 11220);
        List<String[]> testData = new ArrayList<>();
        testData.add(TestData.SEARCH_11221_SUCCESS);
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);

        test.execute(jmeterTransactions);
        assertThat(test.getResult(), is(equalTo(TestResult.FAIL)));
    }

    @Test
    public void verifyExecuteAllTransactionsFail() {
        RespTimeAvgTest test = new RespTimeAvgTest("Test #1", "avgRespTimeTest", "Verify response times", null, 899);
        List<String[]> testData = new ArrayList<>();
        testData.add(TestData.SEARCH_800_SUCCESS);
        testData.add(TestData.LOGIN_1000_SUCCESS);
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);

        test.execute(jmeterTransactions);
        assertThat(test.getResult(), is(equalTo(TestResult.FAIL)));
    }

    @Test
    public void verifyExecuteError() {
        RespTimeAvgTest test = new RespTimeAvgTest("Test #1", "avgRespTimeTest", "Verify response times", "nonexistent", 800);
        List<String[]> testData = new ArrayList<>();
        testData.add(TestData.SEARCH_11221_SUCCESS);
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);

        test.execute(jmeterTransactions);
        assertThat(test.getResult(), is(equalTo(TestResult.ERROR)));
    }

    @Test
    public void verifyIsEqual() {
        MatcherAssert.assertThat(TestData.AVG_RESP_TIME_TEST_A, is(equalTo(TestData.AVG_RESP_TIME_TEST_A)));
    }

    @Test
    public void verifyIsNotEqualOtherTestType() {
        assertThat(TestData.AVG_RESP_TIME_TEST_A, is(not(equalTo((ClientSideTest) TestData.RESP_TIME_PERC_TEST_A))));
    }

    @Test
    public void verifyIsNotEqual() {
        MatcherAssert.assertThat(TestData.AVG_RESP_TIME_TEST_A, is(not(equalTo(TestData.AVG_RESP_TIME_TEST_B))));
    }

    @Test
    public void testPrintTestExecutionReport() {
        RespTimeAvgTest test = new RespTimeAvgTest("my name", "my type", "my description", "Search", 800);
        List<String[]> testData = new ArrayList<>();
        testData.add(TestData.SEARCH_800_SUCCESS);
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);

        String expectedOutput = String.format("Test name:            my name%n" +
                "Test type:            my type%n" +
                "Test description:     my description%n" +
                "Transaction name:     Search%n" +
                "Expected result:      Average response time <= 800%n" +
                "Actual result:        Average response time = 800%n" +
                "Transaction count:    1%n" +
                "Longest transactions: [800]%n" +
                "Test result:          Pass");

        test.execute(jmeterTransactions);
        String output = test.getTestExecutionReport();
        assertThat(output, containsString(expectedOutput));
    }
}