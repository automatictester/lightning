package uk.co.automatictester.lightning.core.tests;

import org.testng.annotations.Test;
import uk.co.automatictester.lightning.core.enums.TestResult;
import uk.co.automatictester.lightning.core.state.data.TestData;
import uk.co.automatictester.lightning.shared.LegacyTestData;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;

public class RespTimeMaxTestUnitTest {

    @Test
    public void verifyExecutePass() {
        RespTimeMaxTest test = new RespTimeMaxTest.Builder("Test #1", 800).withDescription("Verify response times").withTransactionName("Search").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.SEARCH_800_SUCCESS);
        TestData.getInstance().addClientSideTestData(testData);
        test.execute();
        assertThat(test.result(), is(equalTo(TestResult.PASS)));
    }

    @Test
    public void verifyExecuteAllTransactionsPass() {
        RespTimeMaxTest test = new RespTimeMaxTest.Builder("Test #1", 1000).withDescription("Verify response times").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.SEARCH_800_SUCCESS);
        testData.add(LegacyTestData.LOGIN_1000_SUCCESS);
        TestData.getInstance().addClientSideTestData(testData);
        test.execute();
        assertThat(test.result(), is(equalTo(TestResult.PASS)));
    }

    @Test
    public void verifyExecuteFail() {
        RespTimeMaxTest test = new RespTimeMaxTest.Builder("Test #1", 11220).withDescription("Verify response times").withTransactionName("Search").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.SEARCH_11221_SUCCESS);
        TestData.getInstance().addClientSideTestData(testData);
        test.execute();
        assertThat(test.result(), is(equalTo(TestResult.FAIL)));
        assertThat(test.actualResult(), equalTo(11221));
    }

    @Test
    public void verifyExecuteAllTransactionsFail() {
        RespTimeMaxTest test = new RespTimeMaxTest.Builder("Test #1", 999).withDescription("Verify response times").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.SEARCH_800_SUCCESS);
        testData.add(LegacyTestData.LOGIN_1000_SUCCESS);
        TestData.getInstance().addClientSideTestData(testData);
        test.execute();
        assertThat(test.result(), is(equalTo(TestResult.FAIL)));
    }

    @Test
    public void verifyExecuteError() {
        RespTimeMaxTest test = new RespTimeMaxTest.Builder("Test #1", 800).withDescription("Verify response times").withTransactionName("nonexistent").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.SEARCH_11221_SUCCESS);
        TestData.getInstance().addClientSideTestData(testData);
        test.execute();
        assertThat(test.result(), is(equalTo(TestResult.ERROR)));
        assertThat(test.actualResultDescription(), is(equalTo("No transactions with label equal to 'nonexistent' found in CSV file")));
    }

    @Test
    public void verifyEquals() {
        RespTimeMaxTest instanceA = new RespTimeMaxTest.Builder("n", 1000).withDescription("d").withTransactionName("t").build();
        RespTimeMaxTest instanceB = new RespTimeMaxTest.Builder("n", 1000).withDescription("d").withTransactionName("t").build();
        RespTimeMaxTest instanceC = new RespTimeMaxTest.Builder("n", 1000).withDescription("d").withTransactionName("t").build();
        RespTimeMaxTest instanceD = new RespTimeMaxTest.Builder("n", 1000).withDescription("d").withTransactionName("login").build();
        RespTimeAvgTest instanceX = new RespTimeAvgTest.Builder("n", 1000).withDescription("d").withTransactionName("t").build();
        instanceB.execute();

        EqualsAndHashCodeTester<RespTimeMaxTest, RespTimeAvgTest> tester = new EqualsAndHashCodeTester<>();
        tester.addEqualObjects(instanceA, instanceB, instanceC);
        tester.addNonEqualObject(instanceD);
        tester.addNotInstanceof(instanceX);
        assertThat(tester.test(), is(true));
    }

    @Test
    public void testToString() {
        RespTimeMaxTest test = new RespTimeMaxTest.Builder("Test #1", 10).withTransactionName("t").withDescription("d").withRegexp().build();
        assertThat(test.toString(), is(equalTo("Type: maxRespTimeTest, name: Test #1, threshold: 10, transaction: t, description: d, regexp: true")));
    }
}
