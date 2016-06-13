package uk.co.automatictester.lightning.tests;

import org.testng.annotations.Test;
import uk.co.automatictester.lightning.data.JMeterTransactions;
import uk.co.automatictester.lightning.enums.TestResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static uk.co.automatictester.lightning.shared.TestData.*;

public class RespTimeNthPercentileTestTest {

    private static final ArrayList<String> SEARCH_121_SUCCESS = new ArrayList<>(Arrays.asList("Search", "121", "true"));
    private static final ArrayList<String> SEARCH_125_SUCCESS = new ArrayList<>(Arrays.asList("Search", "125", "true"));
    private static final ArrayList<String> SEARCH_129_SUCCESS = new ArrayList<>(Arrays.asList("Search", "129", "true"));
    private static final ArrayList<String> SEARCH_135_SUCCESS = new ArrayList<>(Arrays.asList("Search", "135", "true"));
    private static final ArrayList<String> SEARCH_143_SUCCESS = new ArrayList<>(Arrays.asList("Search", "143", "true"));
    private static final ArrayList<String> SEARCH_148_SUCCESS = new ArrayList<>(Arrays.asList("Search", "148", "true"));
    private static final ArrayList<String> SEARCH_178_SUCCESS = new ArrayList<>(Arrays.asList("Search", "178", "true"));
    private static final ArrayList<String> SEARCH_198_SUCCESS = new ArrayList<>(Arrays.asList("Search", "198", "true"));
    private static final ArrayList<String> SEARCH_221_SUCCESS = new ArrayList<>(Arrays.asList("Search", "221", "true"));
    private static final ArrayList<String> SEARCH_249_SUCCESS = new ArrayList<>(Arrays.asList("Search", "249", "true"));
    private static final ArrayList<String> LOGIN_121_SUCCESS = new ArrayList<>(Arrays.asList("Login", "121", "true"));
    private static final ArrayList<String> LOGIN_125_SUCCESS = new ArrayList<>(Arrays.asList("Login", "125", "true"));

    @Test
    public void testExecutePass() {
        RespTimeNthPercentileTest test = new RespTimeNthPercentileTest("Test #1", "nthPercRespTimeTest", "Verify 90th percentile", "Search", 90, 246);
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        jmeterTransactions.add(SEARCH_121_SUCCESS);
        jmeterTransactions.add(SEARCH_125_SUCCESS);
        jmeterTransactions.add(SEARCH_129_SUCCESS);
        jmeterTransactions.add(SEARCH_135_SUCCESS);
        jmeterTransactions.add(SEARCH_143_SUCCESS);
        jmeterTransactions.add(SEARCH_148_SUCCESS);
        jmeterTransactions.add(SEARCH_178_SUCCESS);
        jmeterTransactions.add(SEARCH_198_SUCCESS);
        jmeterTransactions.add(SEARCH_221_SUCCESS);
        jmeterTransactions.add(SEARCH_249_SUCCESS);

        test.execute(jmeterTransactions);
        assertThat(test.getResult(), is(equalTo(TestResult.PASS)));
    }

    @Test
    public void testExecutePassOnNonDefaultLocale() {
        Locale.setDefault(Locale.FRENCH);

        RespTimeNthPercentileTest test = new RespTimeNthPercentileTest("Test #1", "nthPercRespTimeTest", "Verify 90th percentile", "Search", 90, 246);
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        jmeterTransactions.add(SEARCH_121_SUCCESS);
        jmeterTransactions.add(SEARCH_125_SUCCESS);
        jmeterTransactions.add(SEARCH_129_SUCCESS);
        jmeterTransactions.add(SEARCH_135_SUCCESS);
        jmeterTransactions.add(SEARCH_143_SUCCESS);
        jmeterTransactions.add(SEARCH_148_SUCCESS);
        jmeterTransactions.add(SEARCH_178_SUCCESS);
        jmeterTransactions.add(SEARCH_198_SUCCESS);
        jmeterTransactions.add(SEARCH_221_SUCCESS);
        jmeterTransactions.add(SEARCH_249_SUCCESS);

        test.execute(jmeterTransactions);
        assertThat(test.getResult(), is(equalTo(TestResult.PASS)));
    }

    @Test
    public void testExecuteAllTransactionsPass() {
        RespTimeNthPercentileTest test = new RespTimeNthPercentileTest("Test #1", "nthPercRespTimeTest", "Verify 90th percentile", null, 90, 246);
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        jmeterTransactions.add(LOGIN_121_SUCCESS);
        jmeterTransactions.add(LOGIN_125_SUCCESS);
        jmeterTransactions.add(SEARCH_129_SUCCESS);
        jmeterTransactions.add(SEARCH_135_SUCCESS);
        jmeterTransactions.add(SEARCH_143_SUCCESS);
        jmeterTransactions.add(SEARCH_148_SUCCESS);
        jmeterTransactions.add(SEARCH_178_SUCCESS);
        jmeterTransactions.add(SEARCH_198_SUCCESS);
        jmeterTransactions.add(SEARCH_221_SUCCESS);
        jmeterTransactions.add(SEARCH_249_SUCCESS);

        test.execute(jmeterTransactions);
        assertThat(test.getResult(), is(equalTo(TestResult.PASS)));
    }

    @Test
    public void testExecuteFail() {
        RespTimeNthPercentileTest test = new RespTimeNthPercentileTest("Test #1", "nthPercRespTimeTest", "Verify 90th percentile", "Search", 90, 245);
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        jmeterTransactions.add(SEARCH_121_SUCCESS);
        jmeterTransactions.add(SEARCH_125_SUCCESS);
        jmeterTransactions.add(SEARCH_129_SUCCESS);
        jmeterTransactions.add(SEARCH_135_SUCCESS);
        jmeterTransactions.add(SEARCH_143_SUCCESS);
        jmeterTransactions.add(SEARCH_148_SUCCESS);
        jmeterTransactions.add(SEARCH_178_SUCCESS);
        jmeterTransactions.add(SEARCH_198_SUCCESS);
        jmeterTransactions.add(SEARCH_221_SUCCESS);
        jmeterTransactions.add(SEARCH_249_SUCCESS);

        test.execute(jmeterTransactions);
        assertThat(test.getResult(), is(equalTo(TestResult.FAIL)));
    }

    @Test
    public void testExecuteAllTransactionsFail() {
        RespTimeNthPercentileTest test = new RespTimeNthPercentileTest("Test #1", "nthPercRespTimeTest", "Verify 90th percentile", null, 90, 245);
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        jmeterTransactions.add(LOGIN_121_SUCCESS);
        jmeterTransactions.add(LOGIN_125_SUCCESS);
        jmeterTransactions.add(SEARCH_129_SUCCESS);
        jmeterTransactions.add(SEARCH_135_SUCCESS);
        jmeterTransactions.add(SEARCH_143_SUCCESS);
        jmeterTransactions.add(SEARCH_148_SUCCESS);
        jmeterTransactions.add(SEARCH_178_SUCCESS);
        jmeterTransactions.add(SEARCH_198_SUCCESS);
        jmeterTransactions.add(SEARCH_221_SUCCESS);
        jmeterTransactions.add(SEARCH_249_SUCCESS);

        test.execute(jmeterTransactions);
        assertThat(test.getResult(), is(equalTo(TestResult.FAIL)));
    }

    @Test
    public void testExecuteError() {
        RespTimeNthPercentileTest test = new RespTimeNthPercentileTest("Test #1", "nthPercRespTimeTest", "Verify 90th percentile", "Search", -90, 9);
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        jmeterTransactions.add(SEARCH_143_SUCCESS);
        test.execute(jmeterTransactions);
        assertThat(test.getResult(), is(equalTo(TestResult.ERROR)));
    }

    @Test
    public void verifyIsEqual() {
        assertThat(RESP_TIME_PERC_TEST_A, is(equalTo(RESP_TIME_PERC_TEST_A)));
    }

    @Test
    public void verifyIsNotEqualOtherTestType() {
        assertThat(RESP_TIME_PERC_TEST_A, is(not(equalTo((ClientSideTest) AVG_RESP_TIME_TEST_A))));
    }

    @Test
    public void verifyIsNotEqual() {
        assertThat(RESP_TIME_PERC_TEST_A, is(not(equalTo(RESP_TIME_PERC_TEST_B))));
    }
}