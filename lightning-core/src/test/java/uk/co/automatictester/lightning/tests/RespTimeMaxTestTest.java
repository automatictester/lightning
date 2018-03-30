package uk.co.automatictester.lightning.tests;

import org.hamcrest.MatcherAssert;
import org.testng.annotations.Test;
import uk.co.automatictester.lightning.shared.TestData;
import uk.co.deliverymind.lightning.data.JMeterTransactions;
import uk.co.deliverymind.lightning.enums.TestResult;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static uk.co.deliverymind.lightning.shared.TestData.*;

public class RespTimeMaxTestTest {

    @Test
    public void verifyExecutePass() {
        RespTimeMaxTest test = new RespTimeMaxTest("Test #1", "avgRespTimeTest", "Verify response times", "Search", 800);
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        jmeterTransactions.add(TestData.SEARCH_800_SUCCESS);

        test.execute(jmeterTransactions);
        assertThat(test.getResult(), is(equalTo(TestResult.PASS)));
    }

    @Test
    public void verifyExecuteAllTransactionsPass() {
        RespTimeMaxTest test = new RespTimeMaxTest("Test #1", "avgRespTimeTest", "Verify response times", null, 1000);
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        jmeterTransactions.add(TestData.SEARCH_800_SUCCESS);
        jmeterTransactions.add(TestData.LOGIN_1000_SUCCESS);

        test.execute(jmeterTransactions);
        assertThat(test.getResult(), is(equalTo(TestResult.PASS)));
    }

    @Test
    public void verifyExecuteFail() {
        RespTimeMaxTest test = new RespTimeMaxTest("Test #1", "avgRespTimeTest", "Verify response times", "Search", 11220);
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        jmeterTransactions.add(TestData.SEARCH_11221_SUCCESS);

        test.execute(jmeterTransactions);
        assertThat(test.getResult(), is(equalTo(TestResult.FAIL)));
        assertThat(test.getActualResult(), equalTo(11221));
    }

    @Test
    public void verifyExecuteAllTransactionsFail() {
        RespTimeMaxTest test = new RespTimeMaxTest("Test #1", "avgRespTimeTest", "Verify response times", null, 999);
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        jmeterTransactions.add(TestData.SEARCH_800_SUCCESS);
        jmeterTransactions.add(TestData.LOGIN_1000_SUCCESS);

        test.execute(jmeterTransactions);
        assertThat(test.getResult(), is(equalTo(TestResult.FAIL)));
    }

    @Test
    public void verifyExecuteError() {
        RespTimeMaxTest test = new RespTimeMaxTest("Test #1", "avgRespTimeTest", "Verify response times", "nonexistent", 800);
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        jmeterTransactions.add(TestData.SEARCH_11221_SUCCESS);

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