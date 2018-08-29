package uk.co.automatictester.lightning.core.tests;

import org.hamcrest.Matchers;
import org.testng.annotations.Test;
import uk.co.automatictester.lightning.core.data.JMeterTransactions;
import uk.co.automatictester.lightning.core.enums.TestResult;
import uk.co.automatictester.lightning.core.structures.TestData;
import uk.co.automatictester.lightning.core.tests.base.ClientSideTest;
import uk.co.automatictester.lightning.shared.LegacyTestData;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.number.IsCloseTo.closeTo;

public class ThroughputTestUnitTest {

    private static final ThroughputTest THROUGHPUT_TEST_A = new ThroughputTest.Builder("Test #1", 2).withTransactionName("Login").build();
    private static final ThroughputTest THROUGHPUT_TEST_B = new ThroughputTest.Builder("Test #1", 3).withTransactionName("Login").build();
    private static final ThroughputTest THROUGHPUT_TEST_NO_TRANS_NAME = new ThroughputTest.Builder("Test #1", 2).build();

    private static final String[] TRANSACTION_0 = new String[]{"Login", "1000", "true", "1434291252000"};
    private static final String[] TRANSACTION_1 = new String[]{"Login", "1000", "true", "1434291253000"};
    private static final String[] TRANSACTION_2 = new String[]{"Login", "1000", "true", "1434291254000"};
    private static final String[] TRANSACTION_3 = new String[]{"Login", "1000", "true", "1434291255000"};

    @Test
    public void testExecuteMethodPass() {
        ThroughputTest test = new ThroughputTest.Builder("Test #1", 1).withTransactionName("Login").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(TRANSACTION_0);
        testData.add(TRANSACTION_2);
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);

        TestData.getInstance().addClientSideTestData(jmeterTransactions);
        test.execute();
        assertThat(test.getResult(), is(equalTo(TestResult.PASS)));
    }

    @Test
    public void testExecuteMethodPassNonInteger() {
        ThroughputTest test = new ThroughputTest.Builder("Test #1", 0.6).withTransactionName("Login").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(TRANSACTION_0);
        testData.add(TRANSACTION_3);
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);

        TestData.getInstance().addClientSideTestData(jmeterTransactions);
        test.execute();
        assertThat(test.getResult(), is(equalTo(TestResult.PASS)));
    }

    @Test
    public void testExecuteMethodFailNonInteger() {
        ThroughputTest test = new ThroughputTest.Builder("Test #1", 0.7).withTransactionName("Login").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(TRANSACTION_0);
        testData.add(TRANSACTION_3);
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);

        TestData.getInstance().addClientSideTestData(jmeterTransactions);
        test.execute();
        assertThat(test.getResult(), is(equalTo(TestResult.FAIL)));
    }

    @Test
    public void testExecuteMethodAllTransactionsFail() {
        ThroughputTest test = new ThroughputTest.Builder("Test #1", 3).build();
        List<String[]> testData = new ArrayList<>();
        testData.add(TRANSACTION_0);
        testData.add(TRANSACTION_1);
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);
        TestData.getInstance().addClientSideTestData(jmeterTransactions);
        test.execute();
        assertThat(test.getResult(), is(equalTo(TestResult.FAIL)));
    }

    @Test
    public void testExecuteMethodError() {
        ThroughputTest test = new ThroughputTest.Builder("Test #1", 2).withTransactionName("nonexistent").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(TRANSACTION_0);
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);

        TestData.getInstance().addClientSideTestData(jmeterTransactions);
        test.execute();
        assertThat(test.getResult(), is(equalTo(TestResult.ERROR)));
        assertThat(test.getActualResultDescription(), is(equalTo("No transactions with label equal to 'nonexistent' found in CSV file")));
    }

    @Test
    public void testGetThroughputForOrderedTransactions() {
        List<String[]> testData = new ArrayList<>();
        testData.add(new String[]{"Login", "123", "true", "1434291243000"});
        testData.add(new String[]{"Login", "213", "true", "1434291244000"});
        testData.add(new String[]{"Login", "222", "true", "1434291245000"});
        testData.add(new String[]{"Login", "333", "true", "1434291246000"});
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);
        ThroughputTest test = new ThroughputTest.Builder("throughput", 1).build();
        TestData.getInstance().addClientSideTestData(jmeterTransactions);
        test.execute();

        assertThat(test.getThroughput(), Matchers.is(closeTo(1.33, 0.01)));
    }

    @Test
    public void testGetThroughputForUnorderedTransactions() {
        List<String[]> testData = new ArrayList<>();
        testData.add(new String[]{"Login", "560", "true", "1434291246000"});
        testData.add(new String[]{"Login", "650", "true", "1434291244000"});
        testData.add(new String[]{"Login", "700", "true", "1434291245000"});
        testData.add(new String[]{"Login", "400", "true", "1434291243000"});
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);
        ThroughputTest test = new ThroughputTest.Builder("throughput", 1).build();
        TestData.getInstance().addClientSideTestData(jmeterTransactions);
        test.execute();

        assertThat(test.getThroughput(), Matchers.is(closeTo(1.33, 0.01)));
    }

    @Test
    public void testGetThroughputForOneTransactionPerMillisecond() {
        List<String[]> testData = new ArrayList<>();
        testData.add(new String[]{"Login", "111", "true", "1434291240001"});
        testData.add(new String[]{"Login", "157", "true", "1434291240002"});
        testData.add(new String[]{"Login", "243", "true", "1434291240004"});
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);
        ThroughputTest test = new ThroughputTest.Builder("throughput", 2).build();
        TestData.getInstance().addClientSideTestData(jmeterTransactions);
        test.execute();

        assertThat(test.getThroughput(), Matchers.is(closeTo(1000, 0.01)));
    }

    @Test
    public void testGetThroughputForMoreThanOneTransactionPerMillisecond() {
        List<String[]> testData = new ArrayList<>();
        testData.add(new String[]{"Login", "123", "true", "1434291240001"});
        testData.add(new String[]{"Login", "142", "true", "1434291240002"});
        testData.add(new String[]{"Login", "165", "true", "1434291240003"});
        testData.add(new String[]{"Login", "109", "true", "1434291240004"});
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);
        ThroughputTest test = new ThroughputTest.Builder("throughput", 1).build();
        TestData.getInstance().addClientSideTestData(jmeterTransactions);
        test.execute();

        assertThat(test.getThroughput(), Matchers.is(closeTo(1333.33, 0.01)));
    }

    @Test
    public void testGetThroughputForLessThanOneTransactionPerSecond() {
        List<String[]> testData = new ArrayList<>();
        testData.add(new String[]{"Login", "100", "true", "1434291240000"});
        testData.add(new String[]{"Login", "124", "true", "1434291245000"});
        testData.add(new String[]{"Login", "250", "true", "1434291246000"});
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);
        ThroughputTest test = new ThroughputTest.Builder("throughput", 1).build();
        TestData.getInstance().addClientSideTestData(jmeterTransactions);
        test.execute();

        assertThat(test.getThroughput(), Matchers.is(closeTo(0.5, 0.01)));
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
        assertThat(THROUGHPUT_TEST_A, is(not(equalTo((ClientSideTest) LegacyTestData.RESP_TIME_PERC_TEST_A))));
    }

    @Test
    public void testIsNotEqualNoTransactionName() {
        assertThat(THROUGHPUT_TEST_B, is(not(equalTo(THROUGHPUT_TEST_NO_TRANS_NAME))));
    }
}