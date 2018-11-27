package uk.co.automatictester.lightning.core.tests;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import uk.co.automatictester.lightning.core.enums.TestResult;
import uk.co.automatictester.lightning.core.state.data.TestData;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;

public class RespTimeStdDevTestUnitTest {

    private static final String[] LOGIN_198_SUCCESS = new String[]{"Search", "198", "true"};
    private static final String[] LOGIN_221_SUCCESS = new String[]{"Search", "221", "true"};
    private static final String[] SEARCH_198_SUCCESS = new String[]{"Search", "198", "true"};
    private static final String[] SEARCH_221_SUCCESS = new String[]{"Search", "221", "true"};
    private static final String[] SEARCH_249_SUCCESS = new String[]{"Search", "249", "true"};

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

        RespTimeStdDevTest test = new RespTimeStdDevTest.Builder("Test #1", 25).withDescription("Verify standard deviance").withTransactionName("Search").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(SEARCH_198_SUCCESS);
        testData.add(SEARCH_221_SUCCESS);
        testData.add(SEARCH_249_SUCCESS);
        TestData.getInstance().addClientSideTestData(testData);
        test.execute();
        assertThat(test.result(), is(equalTo(TestResult.PASS)));
    }

    @Test
    public void verifyExecuteAllTransactionsPass() {
        RespTimeStdDevTest test = new RespTimeStdDevTest.Builder("Test #1", 25).withDescription("Verify standard deviance").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LOGIN_198_SUCCESS);
        testData.add(LOGIN_221_SUCCESS);
        testData.add(SEARCH_249_SUCCESS);
        TestData.getInstance().addClientSideTestData(testData);
        test.execute();
        assertThat(test.result(), is(equalTo(TestResult.PASS)));
    }

    @Test
    public void verifyExecuteFail() {
        RespTimeStdDevTest test = new RespTimeStdDevTest.Builder("Test #1", 24).withDescription("Verify standard deviance").withTransactionName("Search").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(SEARCH_198_SUCCESS);
        testData.add(SEARCH_221_SUCCESS);
        testData.add(SEARCH_249_SUCCESS);
        TestData.getInstance().addClientSideTestData(testData);
        test.execute();
        assertThat(test.result(), is(equalTo(TestResult.FAIL)));
    }

    @Test
    public void verifyExecuteAllTransactionsFail() {
        RespTimeStdDevTest test = new RespTimeStdDevTest.Builder("Test #1", 24).withDescription("Verify standard deviance").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LOGIN_198_SUCCESS);
        testData.add(LOGIN_221_SUCCESS);
        testData.add(SEARCH_249_SUCCESS);
        TestData.getInstance().addClientSideTestData(testData);
        test.execute();
        assertThat(test.result(), is(equalTo(TestResult.FAIL)));
    }

    @Test
    public void verifyExecuteError() {
        RespTimeStdDevTest test = new RespTimeStdDevTest.Builder("Test #1", 8).withDescription("Verify standard deviance").withTransactionName("nonexistent").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LOGIN_198_SUCCESS);
        TestData.getInstance().addClientSideTestData(testData);
        test.execute();
        assertThat(test.result(), is(equalTo(TestResult.ERROR)));
        assertThat(test.actualResultDescription(), is(equalTo("No transactions with label equal to 'nonexistent' found in CSV file")));
    }

    @Test
    public void verifyEquals() {
        RespTimeStdDevTest instanceA = new RespTimeStdDevTest.Builder("n", 50).withTransactionName("t").build();
        RespTimeStdDevTest instanceB = new RespTimeStdDevTest.Builder("n", 50).withTransactionName("t").build();
        RespTimeStdDevTest instanceC = new RespTimeStdDevTest.Builder("n", 50).withTransactionName("t").build();
        RespTimeStdDevTest instanceD = new RespTimeStdDevTest.Builder("n", 100).withTransactionName("t").build();
        RespTimeMaxTest instanceX = new RespTimeMaxTest.Builder("n", 9).build();
        instanceB.execute();

        EqualsAndHashCodeTester<RespTimeStdDevTest, RespTimeMaxTest> tester = new EqualsAndHashCodeTester<>();
        tester.addEqualObjects(instanceA, instanceB, instanceC);
        tester.addNonEqualObject(instanceD);
        tester.addNotInstanceof(instanceX);
        assertThat(tester.test(), is(true));
    }

    @Test
    public void testToString() {
        RespTimeStdDevTest test = new RespTimeStdDevTest.Builder("Test #1", 10).withTransactionName("t").withDescription("d").withRegexp().build();
        assertThat(test.toString(), is(equalTo("Type: respTimeStdDevTest, name: Test #1, threshold: 10, transaction: t, description: d, regexp: true")));
    }
}
