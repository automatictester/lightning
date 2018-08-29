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
import java.util.Locale;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;

public class RespTimeMedianTestUnitTest {

    private static final String[] SEARCH_121_SUCCESS = new String[]{"Search", "121", "true"};
    private static final String[] SEARCH_125_SUCCESS = new String[]{"Search", "125", "true"};
    private static final String[] SEARCH_129_SUCCESS = new String[]{"Search", "129", "true"};
    private static final String[] SEARCH_135_SUCCESS = new String[]{"Search", "135", "true"};
    private static final String[] SEARCH_143_SUCCESS = new String[]{"Search", "143", "true"};
    private static final String[] SEARCH_148_SUCCESS = new String[]{"Search", "148", "true"};
    private static final String[] SEARCH_178_SUCCESS = new String[]{"Search", "178", "true"};
    private static final String[] SEARCH_198_SUCCESS = new String[]{"Search", "198", "true"};
    private static final String[] SEARCH_221_SUCCESS = new String[]{"Search", "221", "true"};
    private static final String[] SEARCH_249_SUCCESS = new String[]{"Search", "249", "true"};
    private static final String[] LOGIN_121_SUCCESS = new String[]{"Login", "121", "true"};
    private static final String[] LOGIN_125_SUCCESS = new String[]{"Login", "125", "true"};

    @Test
    public void testExecutePass() {
        RespTimeMedianTest test = new RespTimeMedianTest.Builder("Test #1", 145).withDescription("Verify median").withTransactionName("Search").build();
        List<String[]> testData = new ArrayList<>();
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
        JMeterTransactions jmeterTransactions = JMeterTransactions.from(testData);

        TestData.addClientSideTestData(jmeterTransactions);
        test.execute();
        assertThat(test.getResult(), is(equalTo(TestResult.PASS)));
    }

    @Test
    public void testExecutePassOnNonDefaultLocale() {
        Locale.setDefault(Locale.FRENCH);

        RespTimeMedianTest test = new RespTimeMedianTest.Builder("Test #1", 145).withDescription("Verify median").withTransactionName("Search").build();
        List<String[]> testData = new ArrayList<>();
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
        JMeterTransactions jmeterTransactions = JMeterTransactions.from(testData);

        TestData.addClientSideTestData(jmeterTransactions);
        test.execute();
        assertThat(test.getResult(), is(equalTo(TestResult.PASS)));
    }

    @Test
    public void testExecuteAllTransactionsPass() {
        RespTimeMedianTest test = new RespTimeMedianTest.Builder("Test #1", 145).withDescription("Verify median").build();
        List<String[]> testData = new ArrayList<>();
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
        JMeterTransactions jmeterTransactions = JMeterTransactions.from(testData);

        TestData.addClientSideTestData(jmeterTransactions);
        test.execute();
        assertThat(test.getResult(), is(equalTo(TestResult.PASS)));
    }

    @Test
    public void testExecuteFail() {
        RespTimeMedianTest test = new RespTimeMedianTest.Builder("Test #1", 144).withDescription("Verify median").withTransactionName("Search").build();
        List<String[]> testData = new ArrayList<>();
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
        JMeterTransactions jmeterTransactions = JMeterTransactions.from(testData);

        TestData.addClientSideTestData(jmeterTransactions);
        test.execute();
        assertThat(test.getResult(), is(equalTo(TestResult.FAIL)));
    }

    @Test
    public void testExecuteAllTransactionsFail() {
        RespTimeMedianTest test = new RespTimeMedianTest.Builder("Test #1", 144).withDescription("Verify median").build();
        List<String[]> testData = new ArrayList<>();
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
        JMeterTransactions jmeterTransactions = JMeterTransactions.from(testData);

        TestData.addClientSideTestData(jmeterTransactions);
        test.execute();
        assertThat(test.getResult(), is(equalTo(TestResult.FAIL)));
    }

    @Test
    public void verifyExecuteError() {
        RespTimeMedianTest test = new RespTimeMedianTest.Builder("Test #1", 800).withDescription("Verify median").withTransactionName("nonexistent").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.SEARCH_11221_SUCCESS);
        JMeterTransactions jmeterTransactions = JMeterTransactions.from(testData);

        TestData.addClientSideTestData(jmeterTransactions);
        test.execute();
        assertThat(test.getResult(), is(equalTo(TestResult.ERROR)));
        assertThat(test.getActualResultDescription(), is(equalTo("No transactions with label equal to 'nonexistent' found in CSV file")));
    }

    @Test
    public void verifyIsEqual() {
        MatcherAssert.assertThat(LegacyTestData.RESP_TIME_MEDIAN_TEST_A, is(equalTo(LegacyTestData.RESP_TIME_MEDIAN_TEST_A)));
    }

    @Test
    public void verifyIsNotEqualOtherTestType() {
        assertThat(LegacyTestData.RESP_TIME_MEDIAN_TEST_A, is(not(equalTo((ClientSideTest) LegacyTestData.AVG_RESP_TIME_TEST_A))));
    }

    @Test
    public void verifyIsNotEqual() {
        MatcherAssert.assertThat(LegacyTestData.RESP_TIME_MEDIAN_TEST_A, is(not(equalTo(LegacyTestData.RESP_TIME_MEDIAN_TEST_B))));
    }
}