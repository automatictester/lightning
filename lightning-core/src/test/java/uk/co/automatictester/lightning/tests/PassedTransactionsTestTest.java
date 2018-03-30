package uk.co.automatictester.lightning.tests;

import org.hamcrest.MatcherAssert;
import org.testng.annotations.Test;
import uk.co.automatictester.lightning.data.JMeterTransactions;
import uk.co.automatictester.lightning.enums.TestResult;
import uk.co.automatictester.lightning.shared.TestData;
import uk.co.automatictester.lightning.utils.Percent;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

public class PassedTransactionsTestTest {

    @Test
    public void verifyExecuteMethodPass() {
        PassedTransactionsTest test = new PassedTransactionsTest("Test #1", "passedTransactionsTest", "Verify number of passed tests", "Login", 0);
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        jmeterTransactions.add(TestData.LOGIN_1000_SUCCESS);

        test.execute(jmeterTransactions);
        assertThat(test.getResult(), is(equalTo(TestResult.PASS)));
        assertThat(test.getActualResultDescription(), containsString("Number of failed transactions = 0"));
    }

    @Test
    public void verifyExecuteMethodRegexpPass() {
        PassedTransactionsTest test = new PassedTransactionsTest("Test #1", "passedTransactionsTest", "Verify number of passed tests", "Log[a-z]{2,3}", 0);
        test.setRegexp(true);
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        jmeterTransactions.add(TestData.LOGIN_1000_SUCCESS);
        jmeterTransactions.add(TestData.LOGOUT_1000_SUCCESS);

        test.execute(jmeterTransactions);
        assertThat(test.getResult(), is(equalTo(TestResult.PASS)));
        assertThat(test.getActualResultDescription(), containsString("Number of failed transactions = 0"));
    }

    @Test
    public void verifyExecuteMethodRegexpFail() {
        PassedTransactionsTest test = new PassedTransactionsTest("Test #1", "passedTransactionsTest", "Verify number of passed tests", "Log[a-z]ut", 0);
        test.setRegexp(true);
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        jmeterTransactions.add(TestData.LOGOUT_1000_SUCCESS);
        jmeterTransactions.add(TestData.LOGOUT_1000_FAILURE);

        test.execute(jmeterTransactions);
        assertThat(test.getResult(), is(equalTo(TestResult.FAIL)));
        assertThat(test.getActualResultDescription(), containsString("Number of failed transactions = 1"));
    }

    @Test
    public void verifyExecuteMethodAllTransactionsPass() {
        PassedTransactionsTest test = new PassedTransactionsTest("Test #1", "passedTransactionsTest", "Verify number of passed tests", null, 0);
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        jmeterTransactions.add(TestData.LOGIN_1000_SUCCESS);
        jmeterTransactions.add(TestData.SEARCH_800_SUCCESS);

        test.execute(jmeterTransactions);
        assertThat(test.getResult(), is(equalTo(TestResult.PASS)));
        assertThat(test.getActualResultDescription(), containsString("Number of failed transactions = 0"));
    }

    @Test
    public void verifyExecuteMethodPercentPass() {
        PassedTransactionsTest test = new PassedTransactionsTest("Test #1", "passedTransactionsTest", "Verify percent of passed tests", "Search", new Percent(10));
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        jmeterTransactions.add(TestData.SEARCH_1_SUCCESS);
        jmeterTransactions.add(TestData.SEARCH_2_SUCCESS);
        jmeterTransactions.add(TestData.SEARCH_3_SUCCESS);
        jmeterTransactions.add(TestData.SEARCH_4_SUCCESS);
        jmeterTransactions.add(TestData.SEARCH_5_SUCCESS);
        jmeterTransactions.add(TestData.SEARCH_6_SUCCESS);
        jmeterTransactions.add(TestData.SEARCH_7_SUCCESS);
        jmeterTransactions.add(TestData.SEARCH_8_SUCCESS);
        jmeterTransactions.add(TestData.SEARCH_9_SUCCESS);
        jmeterTransactions.add(TestData.SEARCH_800_FAILURE);

        test.execute(jmeterTransactions);
        assertThat(test.getResult(), is(equalTo(TestResult.PASS)));
        assertThat(test.getActualResultDescription(), containsString("Percent of failed transactions = 10"));
    }

    @Test
    public void verifyExecuteMethodPercentFail() {
        PassedTransactionsTest test = new PassedTransactionsTest("Test #1", "passedTransactionsTest", "Verify percent of passed tests", "Search", new Percent(9));
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        jmeterTransactions.add(TestData.SEARCH_1_SUCCESS);
        jmeterTransactions.add(TestData.SEARCH_2_SUCCESS);
        jmeterTransactions.add(TestData.SEARCH_3_SUCCESS);
        jmeterTransactions.add(TestData.SEARCH_4_SUCCESS);
        jmeterTransactions.add(TestData.SEARCH_5_SUCCESS);
        jmeterTransactions.add(TestData.SEARCH_6_SUCCESS);
        jmeterTransactions.add(TestData.SEARCH_7_SUCCESS);
        jmeterTransactions.add(TestData.SEARCH_8_SUCCESS);
        jmeterTransactions.add(TestData.SEARCH_9_SUCCESS);
        jmeterTransactions.add(TestData.SEARCH_800_FAILURE);

        test.execute(jmeterTransactions);
        assertThat(test.getResult(), is(equalTo(TestResult.FAIL)));
        assertThat(test.getActualResultDescription(), containsString("Percent of failed transactions = 10"));
    }

    @Test
    public void verifyExecuteMethodFail() {
        PassedTransactionsTest test = new PassedTransactionsTest("Test #1", "passedTransactionsTest", "Verify number of passed tests", "Login", 0);
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        jmeterTransactions.add(TestData.LOGIN_1200_FAILURE);

        test.execute(jmeterTransactions);
        assertThat(test.getResult(), is(equalTo(TestResult.FAIL)));
        assertThat(test.getActualResultDescription(), containsString("Number of failed transactions = 1"));
    }

    @Test
    public void verifyExecuteMethodAllTransactionsFail() {
        PassedTransactionsTest test = new PassedTransactionsTest("Test #1", "passedTransactionsTest", "Verify number of passed tests", null, 0);
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        jmeterTransactions.add(TestData.LOGIN_1200_SUCCESS);
        jmeterTransactions.add(TestData.SEARCH_800_FAILURE);

        test.execute(jmeterTransactions);
        assertThat(test.getResult(), is(equalTo(TestResult.FAIL)));
        assertThat(test.getActualResultDescription(), containsString("Number of failed transactions = 1"));
    }

    @Test
    public void verifyExecuteMethodError() {
        PassedTransactionsTest test = new PassedTransactionsTest("Test #1", "passedTransactionsTest", "Verify number of passed tests", "nonexistent", 0);
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        jmeterTransactions.add(TestData.LOGIN_1200_FAILURE);

        test.execute(jmeterTransactions);
        assertThat(test.getResult(), is(equalTo(TestResult.ERROR)));
    }

    @Test
    public void verifyIsEqual() {
        MatcherAssert.assertThat(TestData.PASSED_TRANSACTIONS_TEST_A, is(equalTo(TestData.PASSED_TRANSACTIONS_TEST_A)));
    }

    @Test
    public void verifyIsEqualNoTransactionName() {
        MatcherAssert.assertThat(TestData.PASSED_TRANSACTIONS_TEST_NO_TRANS_NAME, is(equalTo(TestData.PASSED_TRANSACTIONS_TEST_NO_TRANS_NAME)));
    }

    @Test
    public void verifyIsNotEqual() {
        MatcherAssert.assertThat(TestData.PASSED_TRANSACTIONS_TEST_A, is(not(equalTo(TestData.PASSED_TRANSACTIONS_TEST_B))));
    }

    @Test
    public void verifyNumberIsNotEqualPerc() {
        MatcherAssert.assertThat(TestData.PASSED_TRANSACTIONS_TEST_B, is(not(equalTo(TestData.PASSED_TRANSACTIONS_TEST_PERC))));
    }

    @Test
    public void verifyIsNotEqualOtherTestType() {
        assertThat(TestData.PASSED_TRANSACTIONS_TEST_A, is(not(equalTo((ClientSideTest) TestData.RESP_TIME_PERC_TEST_A))));
    }

    @Test
    public void verifyIsNotEqualNoTransactionName() {
        MatcherAssert.assertThat(TestData.PASSED_TRANSACTIONS_TEST_B, is(not(equalTo(TestData.PASSED_TRANSACTIONS_TEST_NO_TRANS_NAME))));
    }

    @Test
    public void testPrintTestExecutionReportPass() {
        String expectedOutput = String.format("Test name:            Test #1%n" +
                "Test type:            passedTransactionsTest%n" +
                "Test description:     Verify number of passed tests%n" +
                "Transaction name:     Login%n" +
                "Expected result:      Number of failed transactions <= 0%n" +
                "Actual result:        Number of failed transactions = 0%n" +
                "Transaction count:    1%n" +
                "Test result:          Pass");

        PassedTransactionsTest test = new PassedTransactionsTest("Test #1", "passedTransactionsTest", "Verify number of passed tests", "Login", 0);
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        jmeterTransactions.add(TestData.LOGIN_1000_SUCCESS);

        test.execute(jmeterTransactions);
        String output = test.getTestExecutionReport();
        assertThat(output, containsString(expectedOutput));
    }

    @Test
    public void testPrintTestExecutionReportFail() {
        String expectedOutput = String.format("Test name:            Test #1%n" +
                "Test type:            passedTransactionsTest%n" +
                "Test description:     Verify number of passed tests%n" +
                "Transaction name:     Login%n" +
                "Expected result:      Number of failed transactions <= 0%n" +
                "Actual result:        Number of failed transactions = 1%n" +
                "Transaction count:    1%n" +
                "Test result:          FAIL");

        PassedTransactionsTest test = new PassedTransactionsTest("Test #1", "passedTransactionsTest", "Verify number of passed tests", "Login", 0);
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        jmeterTransactions.add(TestData.LOGIN_1200_FAILURE);

        test.execute(jmeterTransactions);
        String output = test.getTestExecutionReport();
        assertThat(output, containsString(expectedOutput));
    }

    @Test
    public void testPrintTestExecutionReportIgnored() {
        String expectedOutput = String.format("Test name:            Test #1%n" +
                "Test type:            passedTransactionsTest%n" +
                "Test description:     Verify number of passed tests%n" +
                "Transaction name:     incorrect%n" +
                "Expected result:      Number of failed transactions <= 0%n" +
                "Actual result:        No transactions with label equal to 'incorrect' found in CSV file%n" +
                "Transaction count:    0%n" +
                "Test result:          ERROR");

        PassedTransactionsTest test = new PassedTransactionsTest("Test #1", "passedTransactionsTest", "Verify number of passed tests", "incorrect", 0);
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        jmeterTransactions.add(TestData.LOGIN_1000_SUCCESS);

        test.execute(jmeterTransactions);
        String output = test.getTestExecutionReport();
        assertThat(output, containsString(expectedOutput));
    }

    @Test
    public void testPrintTestExecutionReportPassNoDescription() {
        String expectedOutput = String.format("Test name:            Test #1%n" +
                "Test type:            passedTransactionsTest%n" +
                "Transaction name:     Login%n" +
                "Expected result:      Number of failed transactions <= 0%n" +
                "Actual result:        Number of failed transactions = 0%n" +
                "Transaction count:    1%n" +
                "Test result:          Pass");

        PassedTransactionsTest test = new PassedTransactionsTest("Test #1", "passedTransactionsTest", "", "Login", 0);
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        jmeterTransactions.add(TestData.LOGIN_1000_SUCCESS);

        test.execute(jmeterTransactions);
        String output = test.getTestExecutionReport();
        assertThat(output, containsString(expectedOutput));
    }

    @Test
    public void testPrintTestExecutionReportPassNoTransactionName() {
        String expectedOutput = String.format("Test name:            Test #1%n" +
                "Test type:            passedTransactionsTest%n" +
                "Test description:     Verify number of passed tests%n" +
                "Expected result:      Number of failed transactions <= 0%n" +
                "Actual result:        Number of failed transactions = 0%n" +
                "Transaction count:    1%n" +
                "Test result:          Pass");

        PassedTransactionsTest test = new PassedTransactionsTest("Test #1", "passedTransactionsTest", "Verify number of passed tests", null, 0);
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        jmeterTransactions.add(TestData.LOGIN_1000_SUCCESS);

        test.execute(jmeterTransactions);
        String output = test.getTestExecutionReport();
        assertThat(output, containsString(expectedOutput));
    }
}