package uk.co.automatictester.lightning.core.tests;

import org.testng.annotations.Test;
import uk.co.automatictester.lightning.core.enums.TestResult;
import uk.co.automatictester.lightning.core.state.data.JmeterTransactions;
import uk.co.automatictester.lightning.core.state.data.TestData;
import uk.co.automatictester.lightning.shared.LegacyTestData;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;

public class PassedTransactionsAbsoluteTestUnitTest {

    @Test
    public void verifyExecuteMethodPass() {
        PassedTransactionsAbsoluteTest test = new PassedTransactionsAbsoluteTest.Builder("Test #1", 0).withDescription("Verify number of passed tests").withTransactionName("Login").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.LOGIN_1000_SUCCESS);
        TestData.getInstance().addClientSideTestData(testData);
        test.execute();
        assertThat(test.result(), is(equalTo(TestResult.PASS)));
        assertThat(test.actualResultDescription(), containsString("Number of failed transactions = 0"));
    }

    @Test
    public void verifyExecuteMethodRegexpPass() {
        PassedTransactionsAbsoluteTest test = new PassedTransactionsAbsoluteTest.Builder("Test #1", 0).withDescription("Verify number of passed tests").withTransactionName("Log[a-z]{2,3}").withRegexp().build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.LOGIN_1000_SUCCESS);
        testData.add(LegacyTestData.LOGOUT_1000_SUCCESS);
        TestData.getInstance().addClientSideTestData(testData);
        test.execute();
        assertThat(test.result(), is(equalTo(TestResult.PASS)));
        assertThat(test.actualResultDescription(), containsString("Number of failed transactions = 0"));
    }

    @Test
    public void verifyExecuteMethodRegexpFail() {
        PassedTransactionsAbsoluteTest test = new PassedTransactionsAbsoluteTest.Builder("Test #1", 0).withDescription("Verify number of passed tests").withTransactionName("Log[a-z]ut").withRegexp().build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.LOGOUT_1000_SUCCESS);
        testData.add(LegacyTestData.LOGOUT_1000_FAILURE);
        TestData.getInstance().addClientSideTestData(testData);
        test.execute();
        assertThat(test.result(), is(equalTo(TestResult.FAIL)));
        assertThat(test.actualResultDescription(), containsString("Number of failed transactions = 1"));
    }

    @Test
    public void verifyExecuteMethodAllTransactionsPass() {
        PassedTransactionsAbsoluteTest test = new PassedTransactionsAbsoluteTest.Builder("Test #1", 0).withDescription("Verify number of passed tests").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.LOGIN_1000_SUCCESS);
        testData.add(LegacyTestData.SEARCH_800_SUCCESS);
        TestData.getInstance().addClientSideTestData(testData);
        test.execute();
        assertThat(test.result(), is(equalTo(TestResult.PASS)));
        assertThat(test.actualResultDescription(), containsString("Number of failed transactions = 0"));
    }

    @Test
    public void verifyExecuteMethodFail() {
        PassedTransactionsAbsoluteTest test = new PassedTransactionsAbsoluteTest.Builder("Test #1", 0).withDescription("Verify number of passed tests").withTransactionName("Login").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.LOGIN_1200_FAILURE);
        TestData.getInstance().addClientSideTestData(testData);
        test.execute();
        assertThat(test.result(), is(equalTo(TestResult.FAIL)));
        assertThat(test.actualResultDescription(), containsString("Number of failed transactions = 1"));
    }

    @Test
    public void verifyExecuteMethodAllTransactionsFail() {
        PassedTransactionsAbsoluteTest test = new PassedTransactionsAbsoluteTest.Builder("Test #1", 0).withDescription("Verify number of passed tests").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.LOGIN_1200_SUCCESS);
        testData.add(LegacyTestData.SEARCH_800_FAILURE);
        TestData.getInstance().addClientSideTestData(testData);
        test.execute();
        assertThat(test.result(), is(equalTo(TestResult.FAIL)));
        assertThat(test.actualResultDescription(), containsString("Number of failed transactions = 1"));
    }

    @Test
    public void verifyExecuteMethodError() {
        PassedTransactionsAbsoluteTest test = new PassedTransactionsAbsoluteTest.Builder("Test #1", 0).withDescription("Verify number of passed tests").withTransactionName("nonexistent").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.LOGIN_1200_FAILURE);
        TestData.getInstance().addClientSideTestData(testData);
        test.execute();
        assertThat(test.result(), is(equalTo(TestResult.ERROR)));
    }

    @Test
    public void verifyEquals() {
        PassedTransactionsAbsoluteTest instanceA = new PassedTransactionsAbsoluteTest.Builder("n", 1).withDescription("d").withTransactionName("t").build();
        PassedTransactionsAbsoluteTest instanceB = new PassedTransactionsAbsoluteTest.Builder("n", 1).withDescription("d").withTransactionName("t").build();
        PassedTransactionsAbsoluteTest instanceC = new PassedTransactionsAbsoluteTest.Builder("n", 1).withDescription("d").withTransactionName("t").build();
        PassedTransactionsAbsoluteTest instanceD = new PassedTransactionsAbsoluteTest.Builder("n", 1).build();
        RespTimeNthPercentileTest instanceX = new RespTimeNthPercentileTest.Builder("n", 9, 9).withDescription("d").withTransactionName("t").build();
        instanceB.execute();

        EqualsAndHashCodeTester<PassedTransactionsAbsoluteTest, RespTimeNthPercentileTest> tester = new EqualsAndHashCodeTester<>();
        tester.addEqualObjects(instanceA, instanceB, instanceC);
        tester.addNonEqualObject(instanceD);
        tester.addNotInstanceof(instanceX);
        assertThat(tester.test(), is(true));
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
        TestData.getInstance().addClientSideTestData(testData);
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
        TestData.getInstance().addClientSideTestData(testData);
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
        TestData.getInstance().addClientSideTestData(testData);
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
        TestData.getInstance().addClientSideTestData(testData);
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
        TestData.getInstance().addClientSideTestData(testData);
        test.execute();
        String output = test.getTestExecutionReport();
        assertThat(output, containsString(expectedOutput));
    }

    @Test
    public void testToString() {
        PassedTransactionsAbsoluteTest test = new PassedTransactionsAbsoluteTest.Builder("Test #1", 0).withDescription("d").withTransactionName("t").withRegexp().build();
        assertThat(test.toString(), is(equalTo("Type: passedTransactionsTest, name: Test #1, threshold: 0, transaction: t, description: d, regexp: true")));
    }
}
