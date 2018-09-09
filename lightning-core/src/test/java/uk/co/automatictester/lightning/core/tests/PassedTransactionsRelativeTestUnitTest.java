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

public class PassedTransactionsRelativeTestUnitTest {

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
        TestData.getInstance().addClientSideTestData(testData);
        test.execute();
        assertThat(test.result(), is(equalTo(TestResult.PASS)));
        assertThat(test.actualResultDescription(), containsString("Percent of failed transactions = 10"));
    }

    @Test
    public void verifyExecuteMethodRegexpPass() {
        PassedTransactionsRelativeTest test = new PassedTransactionsRelativeTest.Builder("Test #1", 0).withDescription("Verify percent of passed tests").withTransactionName("Log[a-z]{2,3}").withRegexp().build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.LOGIN_1000_SUCCESS);
        testData.add(LegacyTestData.LOGOUT_1000_SUCCESS);
        TestData.getInstance().addClientSideTestData(testData);
        test.execute();
        assertThat(test.result(), is(equalTo(TestResult.PASS)));
        assertThat(test.actualResultDescription(), containsString("Percent of failed transactions = 0"));
    }

    @Test
    public void verifyExecuteMethodRegexpFail() {
        PassedTransactionsRelativeTest test = new PassedTransactionsRelativeTest.Builder("Test #1", 0).withDescription("Verify percent of passed tests").withTransactionName("Log[a-z]ut").withRegexp().build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.LOGOUT_1000_SUCCESS);
        testData.add(LegacyTestData.LOGOUT_1000_FAILURE);
        TestData.getInstance().addClientSideTestData(testData);
        test.execute();
        assertThat(test.result(), is(equalTo(TestResult.FAIL)));
        assertThat(test.actualResultDescription(), containsString("Percent of failed transactions = 50"));
    }

    @Test
    public void verifyExecuteMethodAllTransactionsPass() {
        PassedTransactionsRelativeTest test = new PassedTransactionsRelativeTest.Builder("Test #1", 0).withDescription("Verify percent of passed tests").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.LOGIN_1000_SUCCESS);
        testData.add(LegacyTestData.SEARCH_800_SUCCESS);
        TestData.getInstance().addClientSideTestData(testData);
        test.execute();
        assertThat(test.result(), is(equalTo(TestResult.PASS)));
        assertThat(test.actualResultDescription(), containsString("Percent of failed transactions = 0"));
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
        TestData.getInstance().addClientSideTestData(testData);
        test.execute();
        assertThat(test.result(), is(equalTo(TestResult.FAIL)));
        assertThat(test.actualResultDescription(), containsString("Percent of failed transactions = 10"));
    }

    @Test
    public void verifyExecuteMethodAllTransactionsFail() {
        PassedTransactionsRelativeTest test = new PassedTransactionsRelativeTest.Builder("Test #1", 0).withDescription("Verify percent of passed tests").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.LOGIN_1200_SUCCESS);
        testData.add(LegacyTestData.SEARCH_800_FAILURE);
        TestData.getInstance().addClientSideTestData(testData);
        test.execute();
        assertThat(test.result(), is(equalTo(TestResult.FAIL)));
        assertThat(test.actualResultDescription(), containsString("Percent of failed transactions = 50"));
    }

    @Test
    public void verifyExecuteMethodError() {
        PassedTransactionsRelativeTest test = new PassedTransactionsRelativeTest.Builder("Test #1", 0).withDescription("Verify percent of passed tests").withTransactionName("nonexistent").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.LOGIN_1200_FAILURE);
        TestData.getInstance().addClientSideTestData(testData);
        test.execute();
        assertThat(test.result(), is(equalTo(TestResult.ERROR)));
    }

    @Test
    public void verifyEquals() {
        PassedTransactionsRelativeTest instanceA = new PassedTransactionsRelativeTest.Builder("n", 2).withDescription("d").withTransactionName("t").build();
        PassedTransactionsRelativeTest instanceB = new PassedTransactionsRelativeTest.Builder("n", 2).withDescription("d").withTransactionName("t").build();
        PassedTransactionsRelativeTest instanceC = new PassedTransactionsRelativeTest.Builder("n", 2).withDescription("d").withTransactionName("t").build();
        PassedTransactionsRelativeTest instanceD = new PassedTransactionsRelativeTest.Builder("n", 5).withDescription("d").withTransactionName("t").build();
        PassedTransactionsAbsoluteTest instanceX = new PassedTransactionsAbsoluteTest.Builder("n", 2).withDescription("d").withTransactionName("t").build();
        instanceB.execute();

        EqualsAndHashCodeTester<PassedTransactionsRelativeTest, PassedTransactionsAbsoluteTest> tester = new EqualsAndHashCodeTester<>();
        tester.addEqualObjects(instanceA, instanceB, instanceC);
        tester.addNonEqualObject(instanceD);
        tester.addNotInstanceof(instanceX);
        assertThat(tester.test(), is(true));
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
        TestData.getInstance().addClientSideTestData(testData);
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
        TestData.getInstance().addClientSideTestData(testData);
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
                "Expected result:      Percent of failed transactions <= 0%n" +
                "Actual result:        Percent of failed transactions = 0%n" +
                "Transaction count:    1%n" +
                "Test result:          Pass");

        PassedTransactionsRelativeTest test = new PassedTransactionsRelativeTest.Builder("Test #1", 0).withTransactionName("Login").build();
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
                "Test description:     Verify percent of passed tests%n" +
                "Expected result:      Percent of failed transactions <= 0%n" +
                "Actual result:        Percent of failed transactions = 0%n" +
                "Transaction count:    1%n" +
                "Test result:          Pass");

        PassedTransactionsRelativeTest test = new PassedTransactionsRelativeTest.Builder("Test #1", 0).withDescription("Verify percent of passed tests").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.LOGIN_1000_SUCCESS);
        TestData.getInstance().addClientSideTestData(testData);
        test.execute();
        String output = test.getTestExecutionReport();
        assertThat(output, containsString(expectedOutput));
    }

    @Test
    public void testToString() {
        PassedTransactionsRelativeTest test = new PassedTransactionsRelativeTest.Builder("Test #1", 0).withDescription("d").withTransactionName("t").withRegexp().build();
        assertThat(test.toString(), is(equalTo("Type: passedTransactionsTest, name: Test #1, threshold: 0, transaction: t, description: d, regexp: true")));
    }
}
