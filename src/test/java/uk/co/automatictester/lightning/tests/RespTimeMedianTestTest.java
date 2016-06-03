package uk.co.automatictester.lightning.tests;

import org.testng.annotations.Test;
import uk.co.automatictester.lightning.data.JMeterTransactions;
import uk.co.automatictester.lightning.enums.TestResult;

import java.util.Locale;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static uk.co.automatictester.lightning.shared.TestData.*;

public class RespTimeMedianTestTest {

    @Test
    public void testExecutePass() {
        RespTimeMedianTest test = new RespTimeMedianTest("Test #1", "medianRespTimeTest", "Verify median", "Search", 6);
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        jmeterTransactions.add(SEARCH_1_SUCCESS);
        jmeterTransactions.add(SEARCH_2_SUCCESS);
        jmeterTransactions.add(SEARCH_3_SUCCESS);
        jmeterTransactions.add(SEARCH_4_SUCCESS);
        jmeterTransactions.add(SEARCH_5_SUCCESS);
        jmeterTransactions.add(SEARCH_6_SUCCESS);
        jmeterTransactions.add(SEARCH_7_SUCCESS);
        jmeterTransactions.add(SEARCH_8_SUCCESS);
        jmeterTransactions.add(SEARCH_9_SUCCESS);
        jmeterTransactions.add(SEARCH_10_SUCCESS);

        test.execute(jmeterTransactions);
        assertThat(test.getResult(), is(equalTo(TestResult.PASS)));
    }

    @Test
    public void testExecutePassOnNonDefaultLocale() {
        Locale.setDefault(Locale.FRENCH);

        RespTimeMedianTest test = new RespTimeMedianTest("Test #1", "medianRespTimeTest", "Verify median", "Search", 6);
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        jmeterTransactions.add(SEARCH_1_SUCCESS);
        jmeterTransactions.add(SEARCH_2_SUCCESS);
        jmeterTransactions.add(SEARCH_3_SUCCESS);
        jmeterTransactions.add(SEARCH_4_SUCCESS);
        jmeterTransactions.add(SEARCH_5_SUCCESS);
        jmeterTransactions.add(SEARCH_6_SUCCESS);
        jmeterTransactions.add(SEARCH_7_SUCCESS);
        jmeterTransactions.add(SEARCH_8_SUCCESS);
        jmeterTransactions.add(SEARCH_9_SUCCESS);
        jmeterTransactions.add(SEARCH_10_SUCCESS);

        test.execute(jmeterTransactions);
        assertThat(test.getResult(), is(equalTo(TestResult.PASS)));
    }

    @Test
    public void testExecuteAllTransactionsPass() {
        RespTimeMedianTest test = new RespTimeMedianTest("Test #1", "medianRespTimeTest", "Verify median", null, 6);
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        jmeterTransactions.add(LOGIN_1_SUCCESS);
        jmeterTransactions.add(LOGIN_2_SUCCESS);
        jmeterTransactions.add(SEARCH_3_SUCCESS);
        jmeterTransactions.add(SEARCH_4_SUCCESS);
        jmeterTransactions.add(SEARCH_5_SUCCESS);
        jmeterTransactions.add(SEARCH_6_SUCCESS);
        jmeterTransactions.add(SEARCH_7_SUCCESS);
        jmeterTransactions.add(SEARCH_8_SUCCESS);
        jmeterTransactions.add(SEARCH_9_SUCCESS);
        jmeterTransactions.add(SEARCH_10_SUCCESS);

        test.execute(jmeterTransactions);
        assertThat(test.getResult(), is(equalTo(TestResult.PASS)));
    }

    @Test
    public void testExecuteFail() {
        RespTimeMedianTest test = new RespTimeMedianTest("Test #1", "medianRespTimeTest", "Verify median", "Search", 4);
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        jmeterTransactions.add(SEARCH_1_SUCCESS);
        jmeterTransactions.add(SEARCH_2_SUCCESS);
        jmeterTransactions.add(SEARCH_3_SUCCESS);
        jmeterTransactions.add(SEARCH_4_SUCCESS);
        jmeterTransactions.add(SEARCH_5_SUCCESS);
        jmeterTransactions.add(SEARCH_6_SUCCESS);
        jmeterTransactions.add(SEARCH_7_SUCCESS);
        jmeterTransactions.add(SEARCH_8_SUCCESS);
        jmeterTransactions.add(SEARCH_9_SUCCESS);
        jmeterTransactions.add(SEARCH_10_SUCCESS);

        test.execute(jmeterTransactions);
        assertThat(test.getResult(), is(equalTo(TestResult.FAIL)));
    }

    @Test
    public void testExecuteAllTransactionsFail() {
        RespTimeMedianTest test = new RespTimeMedianTest("Test #1", "medianRespTimeTest", "Verify median", null, 4);
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        jmeterTransactions.add(LOGIN_1_SUCCESS);
        jmeterTransactions.add(LOGIN_2_SUCCESS);
        jmeterTransactions.add(SEARCH_3_SUCCESS);
        jmeterTransactions.add(SEARCH_4_SUCCESS);
        jmeterTransactions.add(SEARCH_5_SUCCESS);
        jmeterTransactions.add(SEARCH_6_SUCCESS);
        jmeterTransactions.add(SEARCH_7_SUCCESS);
        jmeterTransactions.add(SEARCH_8_SUCCESS);
        jmeterTransactions.add(SEARCH_9_SUCCESS);
        jmeterTransactions.add(SEARCH_10_SUCCESS);

        test.execute(jmeterTransactions);
        assertThat(test.getResult(), is(equalTo(TestResult.FAIL)));
    }

    @Test
    public void verifyExecuteError() {
        RespTimeMedianTest test = new RespTimeMedianTest("Test #1", "medianRespTimeTest", "Verify median", "nonexistent", 800);
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        jmeterTransactions.add(SEARCH_11221_SUCCESS);

        test.execute(jmeterTransactions);
        assertThat(test.getResult(), is(equalTo(TestResult.ERROR)));
    }

    @Test
    public void verifyIsEqual() {
        assertThat(RESP_TIME_MEDIAN_TEST_A, is(equalTo(RESP_TIME_MEDIAN_TEST_A)));
    }

    @Test
    public void verifyIsNotEqualOtherTestType() {
        assertThat(RESP_TIME_MEDIAN_TEST_A, is(not(equalTo((ClientSideTest) AVG_RESP_TIME_TEST_A))));
    }

    @Test
    public void verifyIsNotEqual() {
        assertThat(RESP_TIME_MEDIAN_TEST_A, is(not(equalTo(RESP_TIME_MEDIAN_TEST_B))));
    }
}