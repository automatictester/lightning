package uk.co.automatictester.lightning.tests;

import org.testng.annotations.Test;
import uk.co.automatictester.lightning.ConsoleOutputTest;
import uk.co.automatictester.lightning.data.JMeterTransactions;
import uk.co.automatictester.lightning.enums.TestResult;

import java.util.Locale;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static uk.co.automatictester.lightning.shared.TestData.*;

public class RespTimeAvgTestTest extends ConsoleOutputTest {

    @Test
    public void verifyExecutePass() {
        RespTimeAvgTest test = new RespTimeAvgTest("Test #1", "avgRespTimeTest", "Verify response times", "Search", 800);
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        jmeterTransactions.add(SEARCH_800_SUCCESS);

        test.execute(jmeterTransactions);
        assertThat(test.getResult(), is(equalTo(TestResult.PASS)));
    }

    @Test
    public void verifyExecutePassOnNonDefaultLocale() {
        Locale.setDefault(Locale.FRENCH);

        RespTimeAvgTest test = new RespTimeAvgTest("Test #1", "avgRespTimeTest", "Verify response times", "Search", 6011);
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        jmeterTransactions.add(SEARCH_800_SUCCESS);
        jmeterTransactions.add(SEARCH_11221_SUCCESS);

        test.execute(jmeterTransactions);
        assertThat(test.getResult(), is(equalTo(TestResult.PASS)));
    }

    @Test
    public void verifyExecuteAllTransactionsPass() {
        RespTimeAvgTest test = new RespTimeAvgTest("Test #1", "avgRespTimeTest", "Verify response times", null, 900);
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        jmeterTransactions.add(SEARCH_800_SUCCESS);
        jmeterTransactions.add(LOGIN_1000_SUCCESS);

        test.execute(jmeterTransactions);
        assertThat(test.getResult(), is(equalTo(TestResult.PASS)));
    }

    @Test
    public void verifyExecuteFail() {
        RespTimeAvgTest test = new RespTimeAvgTest("Test #1", "avgRespTimeTest", "Verify response times", "Search", 800);
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        jmeterTransactions.add(SEARCH_11221_SUCCESS);

        test.execute(jmeterTransactions);
        assertThat(test.getResult(), is(equalTo(TestResult.FAIL)));
    }

    @Test
    public void verifyExecuteAllTransactionsFail() {
        RespTimeAvgTest test = new RespTimeAvgTest("Test #1", "avgRespTimeTest", "Verify response times", null, 899);
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        jmeterTransactions.add(SEARCH_800_SUCCESS);
        jmeterTransactions.add(LOGIN_1000_SUCCESS);

        test.execute(jmeterTransactions);
        assertThat(test.getResult(), is(equalTo(TestResult.FAIL)));
    }

    @Test
    public void verifyExecuteError() {
        RespTimeAvgTest test = new RespTimeAvgTest("Test #1", "avgRespTimeTest", "Verify response times", "nonexistent", 800);
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        jmeterTransactions.add(SEARCH_11221_SUCCESS);

        test.execute(jmeterTransactions);
        assertThat(test.getResult(), is(equalTo(TestResult.ERROR)));
    }

    @Test
    public void verifyIsEqual() {
        assertThat(AVG_RESP_TIME_TEST_A, is(equalTo(AVG_RESP_TIME_TEST_A)));
    }

    @Test
    public void verifyIsNotEqualOtherTestType() {
        assertThat((ClientSideTest) AVG_RESP_TIME_TEST_A, is(not(equalTo((ClientSideTest) RESP_TIME_PERC_TEST_A))));
    }

    @Test
    public void verifyIsNotEqual() {
        assertThat(AVG_RESP_TIME_TEST_A, is(not(equalTo(AVG_RESP_TIME_TEST_B))));
    }

    @Test
    public void testPrintTestExecutionReport() {
        RespTimeAvgTest test = new RespTimeAvgTest("my name", "my type", "my description", "Search", 800);
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        jmeterTransactions.add(SEARCH_800_SUCCESS);

        String expectedOutput = String.format("Test name:            my name%n" +
                "Test type:            my type%n" +
                "Test description:     my description%n" +
                "Transaction name:     Search%n" +
                "Expected result:      Average response time <= 800%n" +
                "Actual result:        Average response time = 800%n" +
                "Transaction count:    1%n" +
                "Longest transactions: [800]%n" +
                "Test result:          Pass");

        configureStream();
        test.execute(jmeterTransactions);
        test.printTestExecutionReport();
        assertThat(out.toString(), containsString(expectedOutput));
        revertStream();
    }
}