package uk.co.automatictester.lightning.tests;

import org.testng.annotations.Test;
import uk.co.automatictester.lightning.ConsoleOutputTest;
import uk.co.automatictester.lightning.data.JMeterTransactions;
import uk.co.automatictester.lightning.enums.TestResult;
import uk.co.automatictester.lightning.utils.Percent;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static uk.co.automatictester.lightning.shared.TestData.*;

public class PassedTransactionsTestTest extends ConsoleOutputTest {

    @Test
    public void verifyExecuteMethodPass() {
        PassedTransactionsTest test = new PassedTransactionsTest("Test #1", "passedTransactionsTest", "Verify number of passed tests", "Login", 0);
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        jmeterTransactions.add(LOGIN_1000_SUCCESS);

        test.execute(jmeterTransactions);
        assertThat(test.getResult(), is(equalTo(TestResult.PASS)));
        assertThat(test.getActualResultDescription(), containsString("Number of failed transactions = 0"));
    }

    @Test
    public void verifyExecuteMethodAllTransactionsPass() {
        PassedTransactionsTest test = new PassedTransactionsTest("Test #1", "passedTransactionsTest", "Verify number of passed tests", null, 0);
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        jmeterTransactions.add(LOGIN_1000_SUCCESS);
        jmeterTransactions.add(SEARCH_800_SUCCESS);

        test.execute(jmeterTransactions);
        assertThat(test.getResult(), is(equalTo(TestResult.PASS)));
        assertThat(test.getActualResultDescription(), containsString("Number of failed transactions = 0"));
    }

    @Test
    public void verifyExecuteMethodPercentPass() {
        PassedTransactionsTest test = new PassedTransactionsTest("Test #1", "passedTransactionsTest", "Verify percent of passed tests", "Search", new Percent(10));
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
        jmeterTransactions.add(SEARCH_800_FAILURE);

        test.execute(jmeterTransactions);
        assertThat(test.getResult(), is(equalTo(TestResult.PASS)));
        assertThat(test.getActualResultDescription(), containsString("Percent of failed transactions = 10.0"));
    }

    @Test
    public void verifyExecuteMethodPercentFail() {
        PassedTransactionsTest test = new PassedTransactionsTest("Test #1", "passedTransactionsTest", "Verify percent of passed tests", "Search", new Percent(9));
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
        jmeterTransactions.add(SEARCH_800_FAILURE);

        test.execute(jmeterTransactions);
        assertThat(test.getResult(), is(equalTo(TestResult.FAIL)));
        assertThat(test.getActualResultDescription(), containsString("Percent of failed transactions = 10.0"));
    }

    @Test
    public void verifyExecuteMethodFail() {
        PassedTransactionsTest test = new PassedTransactionsTest("Test #1", "passedTransactionsTest", "Verify number of passed tests", "Login", 0);
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        jmeterTransactions.add(LOGIN_1200_FAILURE);

        test.execute(jmeterTransactions);
        assertThat(test.getResult(), is(equalTo(TestResult.FAIL)));
        assertThat(test.getActualResultDescription(), containsString("Number of failed transactions = 1"));
    }

    @Test
    public void verifyExecuteMethodAllTransactionsFail() {
        PassedTransactionsTest test = new PassedTransactionsTest("Test #1", "passedTransactionsTest", "Verify number of passed tests", null, 0);
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        jmeterTransactions.add(LOGIN_1200_SUCCESS);
        jmeterTransactions.add(SEARCH_800_FAILURE);

        test.execute(jmeterTransactions);
        assertThat(test.getResult(), is(equalTo(TestResult.FAIL)));
        assertThat(test.getActualResultDescription(), containsString("Number of failed transactions = 1"));
    }

    @Test
    public void verifyExecuteMethodError() {
        PassedTransactionsTest test = new PassedTransactionsTest("Test #1", "passedTransactionsTest", "Verify number of passed tests", "nonexistent", 0);
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        jmeterTransactions.add(LOGIN_1200_FAILURE);

        test.execute(jmeterTransactions);
        assertThat(test.getResult(), is(equalTo(TestResult.ERROR)));
    }

    @Test
    public void verifyIsEqual() {
        assertThat(PASSED_TRANSACTIONS_TEST_A, is(equalTo(PASSED_TRANSACTIONS_TEST_A)));
    }

    @Test
    public void verifyIsEqualNoTransactionName() {
        assertThat(PASSED_TRANSACTIONS_TEST_NO_TRANS_NAME, is(equalTo(PASSED_TRANSACTIONS_TEST_NO_TRANS_NAME)));
    }

    @Test
    public void verifyIsNotEqual() {
        assertThat(PASSED_TRANSACTIONS_TEST_A, is(not(equalTo(PASSED_TRANSACTIONS_TEST_B))));
    }

    @Test
    public void verifyNumberIsNotEqualPerc() {
        assertThat(PASSED_TRANSACTIONS_TEST_B, is(not(equalTo(PASSED_TRANSACTIONS_TEST_PERC))));
    }

    @Test
    public void verifyIsNotEqualOtherTestType() {
        assertThat(PASSED_TRANSACTIONS_TEST_A, is(not(equalTo((ClientSideTest) RESP_TIME_PERC_TEST_A))));
    }

    @Test
    public void verifyIsNotEqualNoTransactionName() {
        assertThat(PASSED_TRANSACTIONS_TEST_B, is(not(equalTo(PASSED_TRANSACTIONS_TEST_NO_TRANS_NAME))));
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
        jmeterTransactions.add(LOGIN_1000_SUCCESS);

        configureStream();
        test.execute(jmeterTransactions);
        test.printTestExecutionReport();
        assertThat(out.toString(), containsString(expectedOutput));
        revertStream();
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
        jmeterTransactions.add(LOGIN_1200_FAILURE);

        configureStream();
        test.execute(jmeterTransactions);
        test.printTestExecutionReport();
        assertThat(out.toString(), containsString(expectedOutput));
        revertStream();
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
        jmeterTransactions.add(LOGIN_1000_SUCCESS);

        configureStream();
        test.execute(jmeterTransactions);
        test.printTestExecutionReport();
        assertThat(out.toString(), containsString(expectedOutput));
        revertStream();
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
        jmeterTransactions.add(LOGIN_1000_SUCCESS);

        configureStream();
        test.execute(jmeterTransactions);
        test.printTestExecutionReport();
        assertThat(out.toString(), containsString(expectedOutput));
        revertStream();
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
        jmeterTransactions.add(LOGIN_1000_SUCCESS);

        configureStream();
        test.execute(jmeterTransactions);
        test.printTestExecutionReport();
        assertThat(out.toString(), containsString(expectedOutput));
        revertStream();
    }
}