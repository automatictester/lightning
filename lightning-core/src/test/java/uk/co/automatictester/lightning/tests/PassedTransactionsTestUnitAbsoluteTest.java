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
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

public class PassedTransactionsTestUnitAbsoluteTest {

    @Test
    public void verifyExecuteMethodPass() {
        PassedTransactionsAbsoluteTest test = new PassedTransactionsAbsoluteTest.Builder("Test #1", 0).withDescription("Verify number of passed tests").withTransactionName("Login").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.LOGIN_1000_SUCCESS);
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);

        TestData.addClientSideTestData(jmeterTransactions);
        test.execute();
        assertThat(test.getResult(), is(equalTo(TestResult.PASS)));
        assertThat(test.getActualResultDescription(), containsString("Number of failed transactions = 0"));
    }

    @Test
    public void verifyExecuteMethodRegexpPass() {
        PassedTransactionsAbsoluteTest test = new PassedTransactionsAbsoluteTest.Builder("Test #1", 0).withDescription("Verify number of passed tests").withTransactionName("Log[a-z]{2,3}").withRegexp().build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.LOGIN_1000_SUCCESS);
        testData.add(LegacyTestData.LOGOUT_1000_SUCCESS);
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);

        TestData.addClientSideTestData(jmeterTransactions);
        test.execute();
        assertThat(test.getResult(), is(equalTo(TestResult.PASS)));
        assertThat(test.getActualResultDescription(), containsString("Number of failed transactions = 0"));
    }

    @Test
    public void verifyExecuteMethodRegexpFail() {
        PassedTransactionsAbsoluteTest test = new PassedTransactionsAbsoluteTest.Builder("Test #1", 0).withDescription("Verify number of passed tests").withTransactionName("Log[a-z]ut").withRegexp().build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.LOGOUT_1000_SUCCESS);
        testData.add(LegacyTestData.LOGOUT_1000_FAILURE);
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);

        TestData.addClientSideTestData(jmeterTransactions);
        test.execute();
        assertThat(test.getResult(), is(equalTo(TestResult.FAIL)));
        assertThat(test.getActualResultDescription(), containsString("Number of failed transactions = 1"));
    }

    @Test
    public void verifyExecuteMethodAllTransactionsPass() {
        PassedTransactionsAbsoluteTest test = new PassedTransactionsAbsoluteTest.Builder("Test #1", 0).withDescription("Verify number of passed tests").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.LOGIN_1000_SUCCESS);
        testData.add(LegacyTestData.SEARCH_800_SUCCESS);
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);

        TestData.addClientSideTestData(jmeterTransactions);
        test.execute();
        assertThat(test.getResult(), is(equalTo(TestResult.PASS)));
        assertThat(test.getActualResultDescription(), containsString("Number of failed transactions = 0"));
    }

    @Test
    public void verifyExecuteMethodFail() {
        PassedTransactionsAbsoluteTest test = new PassedTransactionsAbsoluteTest.Builder("Test #1", 0).withDescription("Verify number of passed tests").withTransactionName("Login").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.LOGIN_1200_FAILURE);
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);

        TestData.addClientSideTestData(jmeterTransactions);
        test.execute();
        assertThat(test.getResult(), is(equalTo(TestResult.FAIL)));
        assertThat(test.getActualResultDescription(), containsString("Number of failed transactions = 1"));
    }

    @Test
    public void verifyExecuteMethodAllTransactionsFail() {
        PassedTransactionsAbsoluteTest test = new PassedTransactionsAbsoluteTest.Builder("Test #1", 0).withDescription("Verify number of passed tests").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.LOGIN_1200_SUCCESS);
        testData.add(LegacyTestData.SEARCH_800_FAILURE);
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);

        TestData.addClientSideTestData(jmeterTransactions);
        test.execute();
        assertThat(test.getResult(), is(equalTo(TestResult.FAIL)));
        assertThat(test.getActualResultDescription(), containsString("Number of failed transactions = 1"));
    }

    @Test
    public void verifyExecuteMethodError() {
        PassedTransactionsAbsoluteTest test = new PassedTransactionsAbsoluteTest.Builder("Test #1", 0).withDescription("Verify number of passed tests").withTransactionName("nonexistent").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.LOGIN_1200_FAILURE);
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);

        TestData.addClientSideTestData(jmeterTransactions);
        test.execute();
        assertThat(test.getResult(), is(equalTo(TestResult.ERROR)));
    }

    @Test
    public void verifyIsEqual() {
        MatcherAssert.assertThat(LegacyTestData.PASSED_TRANSACTIONS_TEST_A, is(equalTo(LegacyTestData.PASSED_TRANSACTIONS_TEST_A)));
    }

    @Test
    public void verifyIsEqualNoTransactionName() {
        MatcherAssert.assertThat(LegacyTestData.PASSED_TRANSACTIONS_TEST_NO_TRANS_NAME, is(equalTo(LegacyTestData.PASSED_TRANSACTIONS_TEST_NO_TRANS_NAME)));
    }

    @Test
    public void verifyIsNotEqual() {
        MatcherAssert.assertThat(LegacyTestData.PASSED_TRANSACTIONS_TEST_A, is(not(equalTo(LegacyTestData.PASSED_TRANSACTIONS_TEST_B))));
    }

    @Test
    public void verifyIsNotEqualOtherTestType() {
        assertThat(LegacyTestData.PASSED_TRANSACTIONS_TEST_A, is(not(equalTo((ClientSideTest) LegacyTestData.RESP_TIME_PERC_TEST_A))));
    }

    @Test
    public void verifyIsNotEqualNoTransactionName() {
        MatcherAssert.assertThat(LegacyTestData.PASSED_TRANSACTIONS_TEST_B, is(not(equalTo(LegacyTestData.PASSED_TRANSACTIONS_TEST_NO_TRANS_NAME))));
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

        PassedTransactionsAbsoluteTest test = new PassedTransactionsAbsoluteTest.Builder("Test #1", 0).withDescription("Verify number of passed tests").withTransactionName("Login").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.LOGIN_1000_SUCCESS);
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);

        TestData.addClientSideTestData(jmeterTransactions);
        test.execute();
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

        PassedTransactionsAbsoluteTest test = new PassedTransactionsAbsoluteTest.Builder("Test #1", 0).withDescription("Verify number of passed tests").withTransactionName("Login").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.LOGIN_1200_FAILURE);
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);

        TestData.addClientSideTestData(jmeterTransactions);
        test.execute();
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

        PassedTransactionsAbsoluteTest test = new PassedTransactionsAbsoluteTest.Builder("Test #1", 0).withDescription("Verify number of passed tests").withTransactionName("incorrect").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.LOGIN_1000_SUCCESS);
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);

        TestData.addClientSideTestData(jmeterTransactions);
        test.execute();
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

        PassedTransactionsAbsoluteTest test = new PassedTransactionsAbsoluteTest.Builder("Test #1", 0).withTransactionName("Login").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.LOGIN_1000_SUCCESS);
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);

        TestData.addClientSideTestData(jmeterTransactions);
        test.execute();
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

        PassedTransactionsAbsoluteTest test = new PassedTransactionsAbsoluteTest.Builder("Test #1", 0).withDescription("Verify number of passed tests").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.LOGIN_1000_SUCCESS);
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);

        TestData.addClientSideTestData(jmeterTransactions);
        test.execute();
        String output = test.getTestExecutionReport();
        assertThat(output, containsString(expectedOutput));
    }
}