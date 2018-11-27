package uk.co.automatictester.lightning.core.tests;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import uk.co.automatictester.lightning.core.enums.TestResult;
import uk.co.automatictester.lightning.core.state.data.TestData;
import uk.co.automatictester.lightning.shared.LegacyTestData;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;

public class RespTimeAvgTestUnitTest {

    @DataProvider(name = "locale")
    private Object[][] locale() {
        return new Object[][]{
                {Locale.ENGLISH},
                {Locale.FRENCH},
        };
    }

    @Test(dataProvider = "locale")
    public void verifyExecutePass(Locale locale) {
        Locale.setDefault(locale);

        RespTimeAvgTest test = new RespTimeAvgTest.Builder("Test #1", 6010).withDescription("Verify response times").withTransactionName("Search").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.SEARCH_800_SUCCESS);
        testData.add(LegacyTestData.SEARCH_11221_SUCCESS);
        TestData.getInstance().addClientSideTestData(testData);
        test.execute();
        assertThat(test.result(), is(equalTo(TestResult.PASS)));
    }

    @Test
    public void verifyExecuteAllTransactionsPass() {
        RespTimeAvgTest test = new RespTimeAvgTest.Builder("Test #1", 900).withDescription("Verify response times").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.SEARCH_800_SUCCESS);
        testData.add(LegacyTestData.LOGIN_1000_SUCCESS);
        TestData.getInstance().addClientSideTestData(testData);
        test.execute();
        assertThat(test.result(), is(equalTo(TestResult.PASS)));
    }

    @Test
    public void verifyExecuteFail() {
        RespTimeAvgTest test = new RespTimeAvgTest.Builder("Test #1", 11220).withDescription("Verify response times").withTransactionName("Search").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.SEARCH_11221_SUCCESS);
        TestData.getInstance().addClientSideTestData(testData);
        test.execute();
        assertThat(test.result(), is(equalTo(TestResult.FAIL)));
    }

    @Test
    public void verifyExecuteAllTransactionsFail() {
        RespTimeAvgTest test = new RespTimeAvgTest.Builder("Test #1", 899).withDescription("Verify response times").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.SEARCH_800_SUCCESS);
        testData.add(LegacyTestData.LOGIN_1000_SUCCESS);
        TestData.getInstance().addClientSideTestData(testData);
        test.execute();
        assertThat(test.result(), is(equalTo(TestResult.FAIL)));
    }

    @Test
    public void verifyExecuteError() {
        RespTimeAvgTest test = new RespTimeAvgTest.Builder("Test #1", 800).withDescription("Verify response times").withTransactionName("nonexistent").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.SEARCH_11221_SUCCESS);
        TestData.getInstance().addClientSideTestData(testData);
        test.execute();
        assertThat(test.result(), is(equalTo(TestResult.ERROR)));
        assertThat(test.actualResultDescription(), is(equalTo("No transactions with label equal to 'nonexistent' found in CSV file")));
    }

    @Test
    public void verifyEquals() {
        RespTimeAvgTest instanceA = new RespTimeAvgTest.Builder("n", 1000).withDescription("d").withTransactionName("t").build();
        RespTimeAvgTest instanceB = new RespTimeAvgTest.Builder("n", 1000).withDescription("d").withTransactionName("t").build();
        RespTimeAvgTest instanceC = new RespTimeAvgTest.Builder("n", 1000).withDescription("d").withTransactionName("t").build();
        RespTimeAvgTest instanceD = new RespTimeAvgTest.Builder("n", 200).withDescription("d").withTransactionName("t").build();
        PassedTransactionsAbsoluteTest instanceX = new PassedTransactionsAbsoluteTest.Builder("n", 1000).withDescription("d").withTransactionName("t").build();
        instanceB.execute();

        EqualsAndHashCodeTester<RespTimeAvgTest, PassedTransactionsAbsoluteTest> tester = new EqualsAndHashCodeTester<>();
        tester.addEqualObjects(instanceA, instanceB, instanceC);
        tester.addNonEqualObject(instanceD);
        tester.addNotInstanceof(instanceX);
        assertThat(tester.test(), is(true));
    }

    @Test
    public void testPrintTestExecutionReport() {
        RespTimeAvgTest test = new RespTimeAvgTest.Builder("my name", 800).withDescription("my description").withTransactionName("Search").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.SEARCH_800_SUCCESS);

        String expectedOutput = String.format("Test name:            my name%n" +
                "Test type:            avgRespTimeTest%n" +
                "Test description:     my description%n" +
                "Transaction name:     Search%n" +
                "Expected result:      Average response time <= 800%n" +
                "Actual result:        Average response time = 800%n" +
                "Transaction count:    1%n" +
                "Longest transactions: [800]%n" +
                "Test result:          Pass");

        TestData.getInstance().addClientSideTestData(testData);
        test.execute();
        String output = test.getTestExecutionReport();
        assertThat(output, containsString(expectedOutput));
    }

    @Test
    public void testToString() {
        RespTimeAvgTest test = new RespTimeAvgTest.Builder("Test #1", 0).withTransactionName("t").withDescription("d").withRegexp().build();
        assertThat(test.toString(), is(equalTo("Type: avgRespTimeTest, name: Test #1, threshold: 0, transaction: t, description: d, regexp: true")));
    }
}
