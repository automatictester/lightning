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

public class RespTimeStdDevTestTest {

    private static final ArrayList<String> LOGIN_198_SUCCESS = new ArrayList<>(Arrays.asList("Search", "198", "true"));
    private static final ArrayList<String> LOGIN_221_SUCCESS = new ArrayList<>(Arrays.asList("Search", "221", "true"));
    private static final ArrayList<String> SEARCH_198_SUCCESS = new ArrayList<>(Arrays.asList("Search", "198", "true"));
    private static final ArrayList<String> SEARCH_221_SUCCESS = new ArrayList<>(Arrays.asList("Search", "221", "true"));
    private static final ArrayList<String> SEARCH_249_SUCCESS = new ArrayList<>(Arrays.asList("Search", "249", "true"));

    @Test
    public void verifyExecutePass() {
        RespTimeStdDevTest test = new RespTimeStdDevTest("Test #1", "respTimeStdDevTest", "Verify standard deviance", "Search", 25);
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        jmeterTransactions.add(SEARCH_198_SUCCESS);
        jmeterTransactions.add(SEARCH_221_SUCCESS);
        jmeterTransactions.add(SEARCH_249_SUCCESS);

        test.execute(jmeterTransactions);
        assertThat(test.getResult(), is(equalTo(TestResult.PASS)));
    }

    @Test
    public void verifyExecutePassOnNonDefaultLocale() {
        Locale.setDefault(Locale.FRANCE);

        RespTimeStdDevTest test = new RespTimeStdDevTest("Test #1", "respTimeStdDevTest", "Verify standard deviance", "Search", 25);
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        jmeterTransactions.add(SEARCH_198_SUCCESS);
        jmeterTransactions.add(SEARCH_221_SUCCESS);
        jmeterTransactions.add(SEARCH_249_SUCCESS);

        test.execute(jmeterTransactions);
        assertThat(test.getResult(), is(equalTo(TestResult.PASS)));
    }

    @Test
    public void verifyExecuteAllTransactionsPass() {
        RespTimeStdDevTest test = new RespTimeStdDevTest("Test #1", "respTimeStdDevTest", "Verify standard deviance", null, 25);
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        jmeterTransactions.add(LOGIN_198_SUCCESS);
        jmeterTransactions.add(LOGIN_221_SUCCESS);
        jmeterTransactions.add(SEARCH_249_SUCCESS);

        test.execute(jmeterTransactions);
        assertThat(test.getResult(), is(equalTo(TestResult.PASS)));
    }

    @Test
    public void verifyExecuteFail() {
        RespTimeStdDevTest test = new RespTimeStdDevTest("Test #1", "respTimeStdDevTest", "Verify standard deviance", "Search", 24);
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        jmeterTransactions.add(SEARCH_198_SUCCESS);
        jmeterTransactions.add(SEARCH_221_SUCCESS);
        jmeterTransactions.add(SEARCH_249_SUCCESS);

        test.execute(jmeterTransactions);
        assertThat(test.getResult(), is(equalTo(TestResult.FAIL)));
    }

    @Test
    public void verifyExecuteAllTransactionsFail() {
        RespTimeStdDevTest test = new RespTimeStdDevTest("Test #1", "respTimeStdDevTest", "Verify standard deviance", null, 24);
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        jmeterTransactions.add(LOGIN_198_SUCCESS);
        jmeterTransactions.add(LOGIN_221_SUCCESS);
        jmeterTransactions.add(SEARCH_249_SUCCESS);

        test.execute(jmeterTransactions);
        assertThat(test.getResult(), is(equalTo(TestResult.FAIL)));
    }

    @Test
    public void verifyExecuteError() {
        RespTimeStdDevTest test = new RespTimeStdDevTest("Test #1", "respTimeStdDevTest", "Verify standard deviance", "nonexistent", 8);
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        jmeterTransactions.add(LOGIN_198_SUCCESS);

        test.execute(jmeterTransactions);
        assertThat(test.getResult(), is(equalTo(TestResult.ERROR)));
    }

    @Test
    public void verifyIsEqual() {
        assertThat(RESP_TIME_STD_DEV_TEST_A, is(equalTo(RESP_TIME_STD_DEV_TEST_A)));
    }

    @Test
    public void verifyIsNotEqualOtherTestType() {
        assertThat(RESP_TIME_STD_DEV_TEST_A, is(not(equalTo((ClientSideTest) AVG_RESP_TIME_TEST_A))));
    }

    @Test
    public void verifyIsNotEqual() {
        assertThat(RESP_TIME_STD_DEV_TEST_A, is(not(equalTo(RESP_TIME_STD_DEV_TEST_B))));
    }
}