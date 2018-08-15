package uk.co.automatictester.lightning.tests;

import org.hamcrest.MatcherAssert;
import org.testng.annotations.Test;
import uk.co.automatictester.lightning.data.JMeterTransactions;
import uk.co.automatictester.lightning.enums.TestResult;
import uk.co.automatictester.lightning.shared.LegacyTestData;
import uk.co.automatictester.lightning.structures.TestData;
import uk.co.automatictester.lightning.tests.base.ClientSideTest;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;

public class RespTimeMaxTestUnitTest {

    @Test
    public void verifyExecutePass() {
        RespTimeMaxTest test = new RespTimeMaxTest.Builder("Test #1", 800).withDescription("Verify response times").withTransactionName("Search").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.SEARCH_800_SUCCESS);
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);

        TestData.addClientSideTestData(jmeterTransactions);
        test.execute();
        assertThat(test.getResult(), is(equalTo(TestResult.PASS)));
    }

    @Test
    public void verifyExecuteAllTransactionsPass() {
        RespTimeMaxTest test = new RespTimeMaxTest.Builder("Test #1", 1000).withDescription("Verify response times").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.SEARCH_800_SUCCESS);
        testData.add(LegacyTestData.LOGIN_1000_SUCCESS);
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);

        TestData.addClientSideTestData(jmeterTransactions);
        test.execute();
        assertThat(test.getResult(), is(equalTo(TestResult.PASS)));
    }

    @Test
    public void verifyExecuteFail() {
        RespTimeMaxTest test = new RespTimeMaxTest.Builder("Test #1", 11220).withDescription("Verify response times").withTransactionName("Search").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.SEARCH_11221_SUCCESS);
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);

        TestData.addClientSideTestData(jmeterTransactions);
        test.execute();
        assertThat(test.getResult(), is(equalTo(TestResult.FAIL)));
        assertThat(test.getActualResult(), equalTo(11221));
    }

    @Test
    public void verifyExecuteAllTransactionsFail() {
        RespTimeMaxTest test = new RespTimeMaxTest.Builder("Test #1", 999).withDescription("Verify response times").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.SEARCH_800_SUCCESS);
        testData.add(LegacyTestData.LOGIN_1000_SUCCESS);
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);

        TestData.addClientSideTestData(jmeterTransactions);
        test.execute();
        assertThat(test.getResult(), is(equalTo(TestResult.FAIL)));
    }

    @Test
    public void verifyExecuteError() {
        RespTimeMaxTest test = new RespTimeMaxTest.Builder("Test #1", 800).withDescription("Verify response times").withTransactionName("nonexistent").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.SEARCH_11221_SUCCESS);
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);

        TestData.addClientSideTestData(jmeterTransactions);
        test.execute();
        assertThat(test.getResult(), is(equalTo(TestResult.ERROR)));
        assertThat(test.getActualResultDescription(), is(equalTo("No transactions with label equal to 'nonexistent' found in CSV file")));
    }

    @Test
    public void verifyIsEqual() {
        MatcherAssert.assertThat(LegacyTestData.MAX_RESP_TIME_TEST_A, is(equalTo(LegacyTestData.MAX_RESP_TIME_TEST_A)));
    }

    @Test
    public void verifyIsNotEqualOtherTestType() {
        assertThat(LegacyTestData.MAX_RESP_TIME_TEST_A, is(not(equalTo((ClientSideTest) LegacyTestData.RESP_TIME_PERC_TEST_A))));
    }

    @Test
    public void verifyIsNotEqual() {
        MatcherAssert.assertThat(LegacyTestData.MAX_RESP_TIME_TEST_A, is(not(equalTo(LegacyTestData.MAX_RESP_TIME_TEST_B))));
    }
}