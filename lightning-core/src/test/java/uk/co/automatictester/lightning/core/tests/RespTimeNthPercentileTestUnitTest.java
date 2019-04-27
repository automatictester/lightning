package uk.co.automatictester.lightning.core.tests;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import uk.co.automatictester.lightning.core.enums.TestResult;
import uk.co.automatictester.lightning.core.readers.JmeterBean;
import uk.co.automatictester.lightning.core.state.data.TestData;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;

public class RespTimeNthPercentileTestUnitTest {

    private static final JmeterBean SEARCH_121_SUCCESS = new JmeterBean("Search", "121", "true");
    private static final JmeterBean SEARCH_125_SUCCESS = new JmeterBean("Search", "125", "true");
    private static final JmeterBean SEARCH_129_SUCCESS = new JmeterBean("Search", "129", "true");
    private static final JmeterBean SEARCH_135_SUCCESS = new JmeterBean("Search", "135", "true");
    private static final JmeterBean SEARCH_143_SUCCESS = new JmeterBean("Search", "143", "true");
    private static final JmeterBean SEARCH_148_SUCCESS = new JmeterBean("Search", "148", "true");
    private static final JmeterBean SEARCH_178_SUCCESS = new JmeterBean("Search", "178", "true");
    private static final JmeterBean SEARCH_198_SUCCESS = new JmeterBean("Search", "198", "true");
    private static final JmeterBean SEARCH_221_SUCCESS = new JmeterBean("Search", "221", "true");
    private static final JmeterBean SEARCH_249_SUCCESS = new JmeterBean("Search", "249", "true");
    private static final JmeterBean LOGIN_121_SUCCESS = new JmeterBean("Login", "121", "true");
    private static final JmeterBean LOGIN_125_SUCCESS = new JmeterBean("Login", "125", "true");

    @DataProvider(name = "locale")
    private Object[][] locale() {
        return new Object[][]{
                {Locale.ENGLISH},
                {Locale.FRENCH},
        };
    }

    @Test(dataProvider = "locale")
    public void testExecutePass(Locale locale) {
        Locale.setDefault(locale);

        RespTimeNthPercentileTest test = new RespTimeNthPercentileTest.Builder("Test #1", 246, 90).withDescription("Verify 90th percentile").withTransactionName("Search").build();
        List<JmeterBean> testData = new ArrayList<>();
        testData.add(SEARCH_121_SUCCESS);
        testData.add(SEARCH_125_SUCCESS);
        testData.add(SEARCH_129_SUCCESS);
        testData.add(SEARCH_135_SUCCESS);
        testData.add(SEARCH_143_SUCCESS);
        testData.add(SEARCH_148_SUCCESS);
        testData.add(SEARCH_178_SUCCESS);
        testData.add(SEARCH_198_SUCCESS);
        testData.add(SEARCH_221_SUCCESS);
        testData.add(SEARCH_249_SUCCESS);
        TestData.getInstance().addClientSideTestData(testData);
        test.execute();
        assertThat(test.result(), is(equalTo(TestResult.PASS)));
    }

    @Test
    public void testExecuteAllTransactionsPass() {
        RespTimeNthPercentileTest test = new RespTimeNthPercentileTest.Builder("Test #1", 246, 90).withDescription("Verify 90th percentile").build();
        List<JmeterBean> testData = new ArrayList<>();
        testData.add(LOGIN_121_SUCCESS);
        testData.add(LOGIN_125_SUCCESS);
        testData.add(SEARCH_129_SUCCESS);
        testData.add(SEARCH_135_SUCCESS);
        testData.add(SEARCH_143_SUCCESS);
        testData.add(SEARCH_148_SUCCESS);
        testData.add(SEARCH_178_SUCCESS);
        testData.add(SEARCH_198_SUCCESS);
        testData.add(SEARCH_221_SUCCESS);
        testData.add(SEARCH_249_SUCCESS);
        TestData.getInstance().addClientSideTestData(testData);
        test.execute();
        assertThat(test.result(), is(equalTo(TestResult.PASS)));
    }

    @Test
    public void testExecuteFail() {
        RespTimeNthPercentileTest test = new RespTimeNthPercentileTest.Builder("Test #1", 220, 90).withDescription("Verify 90th percentile").withTransactionName("Search").build();
        List<JmeterBean> testData = new ArrayList<>();
        testData.add(SEARCH_121_SUCCESS);
        testData.add(SEARCH_125_SUCCESS);
        testData.add(SEARCH_129_SUCCESS);
        testData.add(SEARCH_135_SUCCESS);
        testData.add(SEARCH_143_SUCCESS);
        testData.add(SEARCH_148_SUCCESS);
        testData.add(SEARCH_178_SUCCESS);
        testData.add(SEARCH_198_SUCCESS);
        testData.add(SEARCH_221_SUCCESS);
        testData.add(SEARCH_249_SUCCESS);
        TestData.getInstance().addClientSideTestData(testData);
        test.execute();
        assertThat(test.result(), is(equalTo(TestResult.FAIL)));
    }

    @Test
    public void testExecuteAllTransactionsFail() {
        RespTimeNthPercentileTest test = new RespTimeNthPercentileTest.Builder("Test #1", 220, 90).withDescription("Verify 90th percentile").build();
        List<JmeterBean> testData = new ArrayList<>();
        testData.add(LOGIN_121_SUCCESS);
        testData.add(LOGIN_125_SUCCESS);
        testData.add(SEARCH_129_SUCCESS);
        testData.add(SEARCH_135_SUCCESS);
        testData.add(SEARCH_143_SUCCESS);
        testData.add(SEARCH_148_SUCCESS);
        testData.add(SEARCH_178_SUCCESS);
        testData.add(SEARCH_198_SUCCESS);
        testData.add(SEARCH_221_SUCCESS);
        testData.add(SEARCH_249_SUCCESS);
        TestData.getInstance().addClientSideTestData(testData);
        test.execute();
        assertThat(test.result(), is(equalTo(TestResult.FAIL)));
    }

    @Test
    public void testExecuteErrorPercentile() {
        RespTimeNthPercentileTest test = new RespTimeNthPercentileTest.Builder("Test #1", 9, -90).withDescription("Verify 90th percentile").withTransactionName("Search").build();
        List<JmeterBean> testData = new ArrayList<>();
        testData.add(SEARCH_143_SUCCESS);
        TestData.getInstance().addClientSideTestData(testData);
        test.execute();
        assertThat(test.result(), is(equalTo(TestResult.ERROR)));
    }

    @Test
    public void testExecuteErrorNonexistent() {
        RespTimeNthPercentileTest test = new RespTimeNthPercentileTest.Builder("Test #1", 9, -90).withDescription("Verify 90th percentile").withTransactionName("nonexistent").build();
        List<JmeterBean> testData = new ArrayList<>();
        testData.add(SEARCH_143_SUCCESS);
        TestData.getInstance().addClientSideTestData(testData);
        test.execute();
        assertThat(test.result(), is(equalTo(TestResult.ERROR)));
        assertThat(test.actualResultDescription(), is(equalTo("No transactions with label equal to 'nonexistent' found in CSV file")));
    }

    @Test
    public void verifyEquals() {
        RespTimeNthPercentileTest instanceA = new RespTimeNthPercentileTest.Builder("n", 100, 95).withDescription("d").withTransactionName("t").build();
        RespTimeNthPercentileTest instanceB = new RespTimeNthPercentileTest.Builder("n", 100, 95).withDescription("d").withTransactionName("t").build();
        RespTimeNthPercentileTest instanceC = new RespTimeNthPercentileTest.Builder("n", 100, 95).withDescription("d").withTransactionName("t").build();
        RespTimeNthPercentileTest instanceD = new RespTimeNthPercentileTest.Builder("n", 100, 95).withDescription("d").build();
        RespTimeMaxTest instanceX = new RespTimeMaxTest.Builder("n", 9).build();
        instanceB.execute();

        EqualsAndHashCodeTester<RespTimeNthPercentileTest, RespTimeMaxTest> tester = new EqualsAndHashCodeTester<>();
        tester.addEqualObjects(instanceA, instanceB, instanceC);
        tester.addNonEqualObject(instanceD);
        tester.addNotInstanceof(instanceX);
        assertThat(tester.test(), is(true));
    }

    @Test
    public void testToString() {
        RespTimeNthPercentileTest test = new RespTimeNthPercentileTest.Builder("Test #1", 100, 95).withTransactionName("t").withDescription("d").withRegexp().build();
        assertThat(test.toString(), is(equalTo("Type: nthPercRespTimeTest, name: Test #1, threshold: 100, percentile: 95, transaction: t, description: d, regexp: true")));
    }
}
