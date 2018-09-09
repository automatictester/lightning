package uk.co.automatictester.lightning.core.tests;

import org.hamcrest.Matchers;
import org.testng.annotations.Test;
import uk.co.automatictester.lightning.core.enums.TestResult;
import uk.co.automatictester.lightning.core.state.data.JmeterTransactions;
import uk.co.automatictester.lightning.core.state.data.TestData;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.number.IsCloseTo.closeTo;

public class ThroughputTestUnitTest {

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
        TestData.getInstance().addClientSideTestData(testData);
        test.execute();
        assertThat(test.result(), is(equalTo(TestResult.PASS)));
    }

    @Test
    public void testExecuteMethodPassNonInteger() {
        ThroughputTest test = new ThroughputTest.Builder("Test #1", 0.6).withTransactionName("Login").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(TRANSACTION_0);
        testData.add(TRANSACTION_3);
        TestData.getInstance().addClientSideTestData(testData);
        test.execute();
        assertThat(test.result(), is(equalTo(TestResult.PASS)));
    }

    @Test
    public void testExecuteMethodFailNonInteger() {
        ThroughputTest test = new ThroughputTest.Builder("Test #1", 0.7).withTransactionName("Login").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(TRANSACTION_0);
        testData.add(TRANSACTION_3);
        TestData.getInstance().addClientSideTestData(testData);
        test.execute();
        assertThat(test.result(), is(equalTo(TestResult.FAIL)));
    }

    @Test
    public void testExecuteMethodAllTransactionsFail() {
        ThroughputTest test = new ThroughputTest.Builder("Test #1", 3).build();
        List<String[]> testData = new ArrayList<>();
        testData.add(TRANSACTION_0);
        testData.add(TRANSACTION_1);
        TestData.getInstance().addClientSideTestData(testData);
        test.execute();
        assertThat(test.result(), is(equalTo(TestResult.FAIL)));
    }

    @Test
    public void testExecuteMethodError() {
        ThroughputTest test = new ThroughputTest.Builder("Test #1", 2).withTransactionName("nonexistent").build();
        List<String[]> testData = new ArrayList<>();
        testData.add(TRANSACTION_0);
        TestData.getInstance().addClientSideTestData(testData);
        test.execute();
        assertThat(test.result(), is(equalTo(TestResult.ERROR)));
        assertThat(test.actualResultDescription(), is(equalTo("No transactions with label equal to 'nonexistent' found in CSV file")));
    }

    @Test
    public void testGetThroughputForOrderedTransactions() {
        List<String[]> testData = new ArrayList<>();
        testData.add(new String[]{"Login", "123", "true", "1434291243000"});
        testData.add(new String[]{"Login", "213", "true", "1434291244000"});
        testData.add(new String[]{"Login", "222", "true", "1434291245000"});
        testData.add(new String[]{"Login", "333", "true", "1434291246000"});
        TestData.getInstance().addClientSideTestData(testData);
        ThroughputTest test = new ThroughputTest.Builder("throughput", 1).build();
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
        TestData.getInstance().addClientSideTestData(testData);
        ThroughputTest test = new ThroughputTest.Builder("throughput", 1).build();
        test.execute();

        assertThat(test.getThroughput(), Matchers.is(closeTo(1.33, 0.01)));
    }

    @Test
    public void testGetThroughputForOneTransactionPerMillisecond() {
        List<String[]> testData = new ArrayList<>();
        testData.add(new String[]{"Login", "111", "true", "1434291240001"});
        testData.add(new String[]{"Login", "157", "true", "1434291240002"});
        testData.add(new String[]{"Login", "243", "true", "1434291240004"});
        TestData.getInstance().addClientSideTestData(testData);
        ThroughputTest test = new ThroughputTest.Builder("throughput", 2).build();
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
        TestData.getInstance().addClientSideTestData(testData);
        ThroughputTest test = new ThroughputTest.Builder("throughput", 1).build();
        test.execute();

        assertThat(test.getThroughput(), Matchers.is(closeTo(1333.33, 0.01)));
    }

    @Test
    public void testGetThroughputForLessThanOneTransactionPerSecond() {
        List<String[]> testData = new ArrayList<>();
        testData.add(new String[]{"Login", "100", "true", "1434291240000"});
        testData.add(new String[]{"Login", "124", "true", "1434291245000"});
        testData.add(new String[]{"Login", "250", "true", "1434291246000"});
        TestData.getInstance().addClientSideTestData(testData);
        ThroughputTest test = new ThroughputTest.Builder("throughput", 1).build();
        test.execute();

        assertThat(test.getThroughput(), Matchers.is(closeTo(0.5, 0.01)));
    }

    @Test
    public void verifyEquals() {
        ThroughputTest instanceA = new ThroughputTest.Builder("n", 100).withTransactionName("t").build();
        ThroughputTest instanceB = new ThroughputTest.Builder("n", 100).withTransactionName("t").build();
        ThroughputTest instanceC = new ThroughputTest.Builder("n", 100).withTransactionName("t").build();
        ThroughputTest instanceD = new ThroughputTest.Builder("n", 100).build();
        RespTimeMaxTest instanceX = new RespTimeMaxTest.Builder("n", 9).build();
        instanceB.execute();

        EqualsAndHashCodeTester<ThroughputTest, RespTimeMaxTest> tester = new EqualsAndHashCodeTester<>();
        tester.addEqualObjects(instanceA, instanceB, instanceC);
        tester.addNonEqualObject(instanceD);
        tester.addNotInstanceof(instanceX);
        assertThat(tester.test(), is(true));
    }

    @Test
    public void testToString() {
        ThroughputTest test = new ThroughputTest.Builder("Test #1", 10).withTransactionName("t").withDescription("d").withRegexp().build();
        assertThat(test.toString(), is(equalTo("Type: throughputTest, name: Test #1, threshold: 10.00, transaction: t, description: d, regexp: true")));
    }
}
