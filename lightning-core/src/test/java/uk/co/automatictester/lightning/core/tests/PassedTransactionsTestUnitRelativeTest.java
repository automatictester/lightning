package uk.co.automatictester.lightning.core.tests;

import org.hamcrest.MatcherAssert;
import org.testng.annotations.Test;
import uk.co.automatictester.lightning.core.data.JMeterTransactions;
import uk.co.automatictester.lightning.core.enums.TestResult;
import uk.co.automatictester.lightning.core.structures.TestData;
import uk.co.automatictester.lightning.core.tests.base.ClientSideTest;
import uk.co.automatictester.lightning.shared.LegacyTestData;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

public class PassedTransactionsTestUnitRelativeTest {

    @Test
    public void verifyExecuteMethodPass() {
        PassedTransactionsRelativeTest test = new PassedTransactionsRelativeTest.Builder("Test #1", 10).withDescription("Verify percent of passed tests").withTransactionName("Search").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.SEARCH_1_SUCCESS);
        testData.add(LegacyTestData.SEARCH_2_SUCCESS);
        testData.add(LegacyTestData.SEARCH_3_SUCCESS);
        testData.add(LegacyTestData.SEARCH_4_SUCCESS);
        testData.add(LegacyTestData.SEARCH_5_SUCCESS);
        testData.add(LegacyTestData.SEARCH_6_SUCCESS);
        testData.add(LegacyTestData.SEARCH_7_SUCCESS);
        testData.add(LegacyTestData.SEARCH_8_SUCCESS);
        testData.add(LegacyTestData.SEARCH_9_SUCCESS);
        testData.add(LegacyTestData.SEARCH_800_FAILURE);
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);

        TestData.getInstance().addClientSideTestData(jmeterTransactions);
        test.execute();
        assertThat(test.getResult(), is(equalTo(TestResult.PASS)));
        assertThat(test.getActualResultDescription(), containsString("Percent of failed transactions = 10"));
    }

    @Test
    public void verifyExecuteMethodRegexpPass() {
        PassedTransactionsRelativeTest test = new PassedTransactionsRelativeTest.Builder("Test #1", 0).withDescription("Verify percent of passed tests").withTransactionName("Log[a-z]{2,3}").withRegexp().build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.LOGIN_1000_SUCCESS);
        testData.add(LegacyTestData.LOGOUT_1000_SUCCESS);
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);

        TestData.getInstance().addClientSideTestData(jmeterTransactions);
        test.execute();
        assertThat(test.getResult(), is(equalTo(TestResult.PASS)));
        assertThat(test.getActualResultDescription(), containsString("Percent of failed transactions = 0"));
    }

    @Test
    public void verifyExecuteMethodRegexpFail() {
        PassedTransactionsRelativeTest test = new PassedTransactionsRelativeTest.Builder("Test #1", 0).withDescription("Verify percent of passed tests").withTransactionName("Log[a-z]ut").withRegexp().build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.LOGOUT_1000_SUCCESS);
        testData.add(LegacyTestData.LOGOUT_1000_FAILURE);
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);

        TestData.getInstance().addClientSideTestData(jmeterTransactions);
        test.execute();
        assertThat(test.getResult(), is(equalTo(TestResult.FAIL)));
        assertThat(test.getActualResultDescription(), containsString("Percent of failed transactions = 50"));
    }

    @Test
    public void verifyExecuteMethodAllTransactionsPass() {
        PassedTransactionsRelativeTest test = new PassedTransactionsRelativeTest.Builder("Test #1", 0).withDescription("Verify percent of passed tests").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.LOGIN_1000_SUCCESS);
        testData.add(LegacyTestData.SEARCH_800_SUCCESS);
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);

        TestData.getInstance().addClientSideTestData(jmeterTransactions);
        test.execute();
        assertThat(test.getResult(), is(equalTo(TestResult.PASS)));
        assertThat(test.getActualResultDescription(), containsString("Percent of failed transactions = 0"));
    }

    @Test
    public void verifyExecuteMethodPercentPass() {
    }

    @Test
    public void verifyExecuteMethodFail() {
        PassedTransactionsRelativeTest test = new PassedTransactionsRelativeTest.Builder("Test #1", 9).withDescription("Verify percent of passed tests").withTransactionName("Search").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.SEARCH_1_SUCCESS);
        testData.add(LegacyTestData.SEARCH_2_SUCCESS);
        testData.add(LegacyTestData.SEARCH_3_SUCCESS);
        testData.add(LegacyTestData.SEARCH_4_SUCCESS);
        testData.add(LegacyTestData.SEARCH_5_SUCCESS);
        testData.add(LegacyTestData.SEARCH_6_SUCCESS);
        testData.add(LegacyTestData.SEARCH_7_SUCCESS);
        testData.add(LegacyTestData.SEARCH_8_SUCCESS);
        testData.add(LegacyTestData.SEARCH_9_SUCCESS);
        testData.add(LegacyTestData.SEARCH_800_FAILURE);
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);

        TestData.getInstance().addClientSideTestData(jmeterTransactions);
        test.execute();
        assertThat(test.getResult(), is(equalTo(TestResult.FAIL)));
        assertThat(test.getActualResultDescription(), containsString("Percent of failed transactions = 10"));
    }

    @Test
    public void verifyExecuteMethodAllTransactionsFail() {
        PassedTransactionsRelativeTest test = new PassedTransactionsRelativeTest.Builder("Test #1", 0).withDescription("Verify percent of passed tests").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.LOGIN_1200_SUCCESS);
        testData.add(LegacyTestData.SEARCH_800_FAILURE);
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);

        TestData.getInstance().addClientSideTestData(jmeterTransactions);
        test.execute();
        assertThat(test.getResult(), is(equalTo(TestResult.FAIL)));
        assertThat(test.getActualResultDescription(), containsString("Percent of failed transactions = 50"));
    }

    @Test
    public void verifyExecuteMethodError() {
        PassedTransactionsRelativeTest test = new PassedTransactionsRelativeTest.Builder("Test #1", 0).withDescription("Verify percent of passed tests").withTransactionName("nonexistent").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.LOGIN_1200_FAILURE);
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);

        TestData.getInstance().addClientSideTestData(jmeterTransactions);
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
                "Test description:     Verify percent of passed tests%n" +
                "Transaction name:     Login%n" +
                "Expected result:      Percent of failed transactions <= 0%n" +
                "Actual result:        Percent of failed transactions = 0%n" +
                "Transaction count:    1%n" +
                "Test result:          Pass");

        PassedTransactionsRelativeTest test = new PassedTransactionsRelativeTest.Builder("Test #1", 0).withDescription("Verify percent of passed tests").withTransactionName("Login").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.LOGIN_1000_SUCCESS);
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);

        TestData.getInstance().addClientSideTestData(jmeterTransactions);
        test.execute();
        String output = test.getTestExecutionReport();
        assertThat(output, containsString(expectedOutput));
    }

    @Test
    public void testPrintTestExecutionReportFail() {
        String expectedOutput = String.format("Test name:            Test #1%n" +
                "Test type:            passedTransactionsTest%n" +
                "Test description:     Verify percent of passed tests%n" +
                "Transaction name:     Login%n" +
                "Expected result:      Percent of failed transactions <= 0%n" +
                "Actual result:        Percent of failed transactions = 100%n" +
                "Transaction count:    1%n" +
                "Test result:          FAIL");

        PassedTransactionsRelativeTest test = new PassedTransactionsRelativeTest.Builder("Test #1", 0).withDescription("Verify percent of passed tests").withTransactionName("Login").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.LOGIN_1200_FAILURE);
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);

        TestData.getInstance().addClientSideTestData(jmeterTransactions);
        test.execute();
        String output = test.getTestExecutionReport();
        assertThat(output, containsString(expectedOutput));
    }

    @Test
    public void testPrintTestExecutionReportIgnored() {
        String expectedOutput = String.format("Test name:            Test #1%n" +
                "Test type:            passedTransactionsTest%n" +
                "Test description:     Verify percent of passed tests%n" +
                "Transaction name:     incorrect%n" +
                "Expected result:      Percent of failed transactions <= 0%n" +
                "Actual result:        No transactions with label equal to 'incorrect' found in CSV file%n" +
                "Transaction count:    0%n" +
                "Test result:          ERROR");

        PassedTransactionsRelativeTest test = new PassedTransactionsRelativeTest.Builder("Test #1", 0).withDescription("Verify percent of passed tests").withTransactionName("incorrect").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.LOGIN_1000_SUCCESS);
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);

        TestData.getInstance().addClientSideTestData(jmeterTransactions);
        test.execute();
        String output = test.getTestExecutionReport();
        assertThat(output, containsString(expectedOutput));
    }

    @Test
    public void testPrintTestExecutionReportPassNoDescription() {
        String expectedOutput = String.format("Test name:            Test #1%n" +
                "Test type:            passedTransactionsTest%n" +
                "Transaction name:     Login%n" +
                "Expected result:      Percent of failed transactions <= 0%n" +
                "Actual result:        Percent of failed transactions = 0%n" +
                "Transaction count:    1%n" +
                "Test result:          Pass");

        PassedTransactionsRelativeTest test = new PassedTransactionsRelativeTest.Builder("Test #1", 0).withTransactionName("Login").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.LOGIN_1000_SUCCESS);
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);

        TestData.getInstance().addClientSideTestData(jmeterTransactions);
        test.execute();
        String output = test.getTestExecutionReport();
        assertThat(output, containsString(expectedOutput));
    }

    @Test
    public void testPrintTestExecutionReportPassNoTransactionName() {
        String expectedOutput = String.format("Test name:            Test #1%n" +
                "Test type:            passedTransactionsTest%n" +
                "Test description:     Verify percent of passed tests%n" +
                "Expected result:      Percent of failed transactions <= 0%n" +
                "Actual result:        Percent of failed transactions = 0%n" +
                "Transaction count:    1%n" +
                "Test result:          Pass");

        PassedTransactionsRelativeTest test = new PassedTransactionsRelativeTest.Builder("Test #1", 0).withDescription("Verify percent of passed tests").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.LOGIN_1000_SUCCESS);
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);

        TestData.getInstance().addClientSideTestData(jmeterTransactions);
        test.execute();
        String output = test.getTestExecutionReport();
        assertThat(output, containsString(expectedOutput));
    }
}