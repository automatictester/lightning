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
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;

public class RespTimeStdDevTestUnitTest {

    private static final String[] LOGIN_198_SUCCESS = new String[] {"Search", "198", "true"};
    private static final String[] LOGIN_221_SUCCESS = new String[] {"Search", "221", "true"};
    private static final String[] SEARCH_198_SUCCESS = new String[] {"Search", "198", "true"};
    private static final String[] SEARCH_221_SUCCESS = new String[] {"Search", "221", "true"};
    private static final String[] SEARCH_249_SUCCESS = new String[] {"Search", "249", "true"};

    @Test
    public void verifyExecutePass() {
        RespTimeStdDevTest test = new RespTimeStdDevTest.Builder("Test #1", 25).withDescription("Verify standard deviance").withTransactionName("Search").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(SEARCH_198_SUCCESS);
        testData.add(SEARCH_221_SUCCESS);
        testData.add(SEARCH_249_SUCCESS);
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);

        test.execute(jmeterTransactions);
        assertThat(test.getResult(), is(equalTo(TestResult.PASS)));
    }

    @Test
    public void verifyExecutePassOnNonDefaultLocale() {
        Locale.setDefault(Locale.FRANCE);

        RespTimeStdDevTest test = new RespTimeStdDevTest.Builder("Test #1", 25).withDescription("Verify standard deviance").withTransactionName("Search").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(SEARCH_198_SUCCESS);
        testData.add(SEARCH_221_SUCCESS);
        testData.add(SEARCH_249_SUCCESS);
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);

        test.execute(jmeterTransactions);
        assertThat(test.getResult(), is(equalTo(TestResult.PASS)));
    }

    @Test
    public void verifyExecuteAllTransactionsPass() {
        RespTimeStdDevTest test = new RespTimeStdDevTest.Builder("Test #1", 25).withDescription("Verify standard deviance").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LOGIN_198_SUCCESS);
        testData.add(LOGIN_221_SUCCESS);
        testData.add(SEARCH_249_SUCCESS);
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);

        test.execute(jmeterTransactions);
        assertThat(test.getResult(), is(equalTo(TestResult.PASS)));
    }

    @Test
    public void verifyExecuteFail() {
        RespTimeStdDevTest test = new RespTimeStdDevTest.Builder("Test #1", 24).withDescription("Verify standard deviance").withTransactionName("Search").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(SEARCH_198_SUCCESS);
        testData.add(SEARCH_221_SUCCESS);
        testData.add(SEARCH_249_SUCCESS);
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);

        test.execute(jmeterTransactions);
        assertThat(test.getResult(), is(equalTo(TestResult.FAIL)));
    }

    @Test
    public void verifyExecuteAllTransactionsFail() {
        RespTimeStdDevTest test = new RespTimeStdDevTest.Builder("Test #1", 24).withDescription("Verify standard deviance").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LOGIN_198_SUCCESS);
        testData.add(LOGIN_221_SUCCESS);
        testData.add(SEARCH_249_SUCCESS);
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);

        test.execute(jmeterTransactions);
        assertThat(test.getResult(), is(equalTo(TestResult.FAIL)));
    }

    @Test
    public void verifyExecuteError() {
        RespTimeStdDevTest test = new RespTimeStdDevTest.Builder("Test #1", 8).withDescription("Verify standard deviance").withTransactionName("nonexistent").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LOGIN_198_SUCCESS);
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);

        test.execute(jmeterTransactions);
        assertThat(test.getResult(), is(equalTo(TestResult.ERROR)));
    }

    @Test
    public void verifyIsEqual() {
        MatcherAssert.assertThat(TestData.RESP_TIME_STD_DEV_TEST_A, is(equalTo(TestData.RESP_TIME_STD_DEV_TEST_A)));
    }

    @Test
    public void verifyIsNotEqualOtherTestType() {
        assertThat(TestData.RESP_TIME_STD_DEV_TEST_A, is(not(equalTo((ClientSideTest) TestData.AVG_RESP_TIME_TEST_A))));
    }

    @Test
    public void verifyIsNotEqual() {
        MatcherAssert.assertThat(TestData.RESP_TIME_STD_DEV_TEST_A, is(not(equalTo(TestData.RESP_TIME_STD_DEV_TEST_B))));
    }
}