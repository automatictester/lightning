package uk.co.automatictester.lightning.tests;

import org.testng.annotations.Test;
import uk.co.automatictester.lightning.data.JMeterTransactions;
import uk.co.automatictester.lightning.enums.TestResult;
import uk.co.automatictester.lightning.shared.TestData;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;

public class ThroughputTestUnitTest {

    private static final ThroughputTest THROUGHPUT_TEST_A = new ThroughputTest("Test #1", "throughputTest", "", "Login", 2);
    private static final ThroughputTest THROUGHPUT_TEST_B = new ThroughputTest("Test #1", "throughputTest", "", "Login", 3);
    private static final ThroughputTest THROUGHPUT_TEST_NO_TRANS_NAME = new ThroughputTest("Test #1", "throughputTest", "", null, 2);

    private static final String[] TRANSACTION_0 = new String[] {"Login", "1000", "true", "1434291252000"};
    private static final String[] TRANSACTION_1 = new String[] {"Login", "1000", "true", "1434291253000"};
    private static final String[] TRANSACTION_2 = new String[] {"Login", "1000", "true", "1434291254000"};
    private static final String[] TRANSACTION_3 = new String[] {"Login", "1000", "true", "1434291255000"};

    @Test
    public void testExecuteMethodPass() {
        ThroughputTest test = new ThroughputTest("Test #1", "throughputTest", "", "Login", 1);
        List<String[]> testData = new ArrayList<>();
        testData.add(TRANSACTION_0);
        testData.add(TRANSACTION_2);
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);

        test.execute(jmeterTransactions);
        assertThat(test.getResult(), is(equalTo(TestResult.PASS)));
    }

    @Test
    public void testExecuteMethodPassNonInteger() {
        ThroughputTest test = new ThroughputTest("Test #1", "throughputTest", "", "Login", 0.6);
        List<String[]> testData = new ArrayList<>();
        testData.add(TRANSACTION_0);
        testData.add(TRANSACTION_3);
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);

        test.execute(jmeterTransactions);
        assertThat(test.getResult(), is(equalTo(TestResult.PASS)));
    }

    @Test
    public void testExecuteMethodFailNonInteger() {
        ThroughputTest test = new ThroughputTest("Test #1", "throughputTest", "", "Login", 0.7);
        List<String[]> testData = new ArrayList<>();
        testData.add(TRANSACTION_0);
        testData.add(TRANSACTION_3);
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);

        test.execute(jmeterTransactions);
        assertThat(test.getResult(), is(equalTo(TestResult.FAIL)));
    }

    @Test
    public void testExecuteMethodAllTransactionsFail() {
        ThroughputTest test = new ThroughputTest("Test #1", "throughputTest", "", null, 3);
        List<String[]> testData = new ArrayList<>();
        testData.add(TRANSACTION_0);
        testData.add(TRANSACTION_1);
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);
        test.execute(jmeterTransactions);
        assertThat(test.getResult(), is(equalTo(TestResult.FAIL)));
    }

    @Test
    public void testExecuteMethodError() {
        ThroughputTest test = new ThroughputTest("Test #1", "throughputTest", "", "nonexistent", 2);
        List<String[]> testData = new ArrayList<>();
        testData.add(TRANSACTION_0);
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);

        test.execute(jmeterTransactions);
        assertThat(test.getResult(), is(equalTo(TestResult.ERROR)));
    }

    @Test
    public void testIsEqual() {
        assertThat(THROUGHPUT_TEST_A, is(equalTo(THROUGHPUT_TEST_A)));
    }

    @Test
    public void testIsEqualNoTransactionName() {
        assertThat(THROUGHPUT_TEST_NO_TRANS_NAME, is(equalTo(THROUGHPUT_TEST_NO_TRANS_NAME)));
    }

    @Test
    public void testIsNotEqual() {
        assertThat(THROUGHPUT_TEST_A, is(not(equalTo(THROUGHPUT_TEST_B))));
    }

    @Test
    public void testIsNotEqualOtherTestType() {
        assertThat(THROUGHPUT_TEST_A, is(not(equalTo((ClientSideTest) TestData.RESP_TIME_PERC_TEST_A))));
    }

    @Test
    public void testIsNotEqualNoTransactionName() {
        assertThat(THROUGHPUT_TEST_B, is(not(equalTo(THROUGHPUT_TEST_NO_TRANS_NAME))));
    }
}