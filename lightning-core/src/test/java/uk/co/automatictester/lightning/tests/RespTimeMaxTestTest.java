package uk.co.automatictester.lightning.tests;

import org.hamcrest.MatcherAssert;
import org.testng.annotations.Test;
import uk.co.automatictester.lightning.data.JMeterTransactions;
import uk.co.automatictester.lightning.enums.TestResult;
import uk.co.automatictester.lightning.shared.TestData;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;

public class RespTimeMaxTestTest {

    @Test
    public void verifyExecutePass() {
        RespTimeMaxTest test = new RespTimeMaxTest("Test #1", "avgRespTimeTest", "Verify response times", "Search", 800);
        List<String[]> testData = new ArrayList<>();
        testData.add(TestData.SEARCH_800_SUCCESS);
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);

        test.execute(jmeterTransactions);
        assertThat(test.getResult(), is(equalTo(TestResult.PASS)));
    }

    @Test
    public void verifyExecuteAllTransactionsPass() {
        RespTimeMaxTest test = new RespTimeMaxTest("Test #1", "avgRespTimeTest", "Verify response times", null, 1000);
        List<String[]> testData = new ArrayList<>();
        testData.add(TestData.SEARCH_800_SUCCESS);
        testData.add(TestData.LOGIN_1000_SUCCESS);
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);

        test.execute(jmeterTransactions);
        assertThat(test.getResult(), is(equalTo(TestResult.PASS)));
    }

    @Test
    public void verifyExecuteFail() {
        RespTimeMaxTest test = new RespTimeMaxTest("Test #1", "avgRespTimeTest", "Verify response times", "Search", 11220);
        List<String[]> testData = new ArrayList<>();
        testData.add(TestData.SEARCH_11221_SUCCESS);
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);

        test.execute(jmeterTransactions);
        assertThat(test.getResult(), is(equalTo(TestResult.FAIL)));
        assertThat(test.getActualResult(), equalTo(11221));
    }

    @Test
    public void verifyExecuteAllTransactionsFail() {
        RespTimeMaxTest test = new RespTimeMaxTest("Test #1", "avgRespTimeTest", "Verify response times", null, 999);
        List<String[]> testData = new ArrayList<>();
        testData.add(TestData.SEARCH_800_SUCCESS);
        testData.add(TestData.LOGIN_1000_SUCCESS);
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);

        test.execute(jmeterTransactions);
        assertThat(test.getResult(), is(equalTo(TestResult.FAIL)));
    }

    @Test
    public void verifyExecuteError() {
        RespTimeMaxTest test = new RespTimeMaxTest("Test #1", "avgRespTimeTest", "Verify response times", "nonexistent", 800);
        List<String[]> testData = new ArrayList<>();
        testData.add(TestData.SEARCH_11221_SUCCESS);
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);

        test.execute(jmeterTransactions);
        assertThat(test.getResult(), is(equalTo(TestResult.ERROR)));
    }

    @Test
    public void verifyIsEqual() {
        MatcherAssert.assertThat(TestData.MAX_RESP_TIME_TEST_A, is(equalTo(TestData.MAX_RESP_TIME_TEST_A)));
    }

    @Test
    public void verifyIsNotEqualOtherTestType() {
        assertThat(TestData.MAX_RESP_TIME_TEST_A, is(not(equalTo((ClientSideTest) TestData.RESP_TIME_PERC_TEST_A))));
    }

    @Test
    public void verifyIsNotEqual() {
        MatcherAssert.assertThat(TestData.MAX_RESP_TIME_TEST_A, is(not(equalTo(TestData.MAX_RESP_TIME_TEST_B))));
    }
}