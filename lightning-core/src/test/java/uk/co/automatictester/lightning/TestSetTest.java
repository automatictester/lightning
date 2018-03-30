package uk.co.automatictester.lightning;

import org.testng.annotations.Test;
import uk.co.automatictester.lightning.shared.TestData;
import uk.co.deliverymind.lightning.data.JMeterTransactions;
import uk.co.deliverymind.lightning.data.PerfMonDataEntries;
import uk.co.deliverymind.lightning.enums.ServerSideTestType;
import uk.co.deliverymind.lightning.tests.ClientSideTest;
import uk.co.deliverymind.lightning.tests.PassedTransactionsTest;
import uk.co.deliverymind.lightning.tests.RespTimeAvgTest;
import uk.co.deliverymind.lightning.tests.ServerSideTest;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static uk.co.deliverymind.lightning.shared.TestData.*;

public class TestSetTest extends ConsoleOutputTest {

    @Test
    public void verifyExecuteServerMethod_1_1_1() {
        ServerSideTest testA = new ServerSideTest("Test #1", "serverSideTest", ServerSideTestType.LESS_THAN, "Verify CPU utilisation", "192.168.0.12 CPU", 10001);
        ServerSideTest testB = new ServerSideTest("Test #2", "serverSideTest", ServerSideTestType.GREATER_THAN, "Verify CPU utilisation", "192.168.0.12 CPU", 10001);
        ServerSideTest testC = new ServerSideTest("Test #3", "serverSideTest", ServerSideTestType.GREATER_THAN, "Verify CPU utilisation", "192.168.0.240 CPU", 10001);

        PerfMonDataEntries dataEntries = new PerfMonDataEntries();
        dataEntries.add(TestData.CPU_ENTRY_10000);
        dataEntries.add(TestData.CPU_ENTRY_10001);

        List<ServerSideTest> tests = new ArrayList<>();
        tests.add(testA);
        tests.add(testB);
        tests.add(testC);

        TestSet testSet = new TestSet(null, tests);
        configureStream();
        testSet.executeServerSideTests(dataEntries);
        revertStream();

        assertThat(testSet.getTestCount(), is(3));
        assertThat(testSet.getPassCount(), is(1));
        assertThat(testSet.getFailCount(), is(1));
        assertThat(testSet.getErrorCount(), is(1));
    }

    @Test
    public void verifyExecuteClientMethod_2_0_0() {
        PassedTransactionsTest passedTransactionsTestA = new PassedTransactionsTest("Test #1", "passedTransactionsTest", "Verify number of passed tests", "Login", 0);
        PassedTransactionsTest passedTransactionsTestB = new PassedTransactionsTest("Test #2", "passedTransactionsTest", "Verify number of passed tests", null, 0);

        JMeterTransactions transactions = new JMeterTransactions();
        transactions.add(TestData.LOGIN_3514_SUCCESS);
        transactions.add(TestData.SEARCH_11221_SUCCESS);

        List<ClientSideTest> tests = new ArrayList<>();
        tests.add(passedTransactionsTestA);
        tests.add(passedTransactionsTestB);

        TestSet testSet = new TestSet(tests, null);
        configureStream();
        testSet.executeClientSideTests(transactions);
        revertStream();

        assertThat(testSet.getTestCount(), is(2));
        assertThat(testSet.getPassCount(), is(2));
        assertThat(testSet.getFailCount(), is(0));
        assertThat(testSet.getErrorCount(), is(0));
    }

    @Test
    public void verifyExecuteClientMethod_1_1_1() {
        RespTimeAvgTest respTimeAvgTestA = new RespTimeAvgTest("Test #1", "avgRespTimeTest", "", "Login", 4000);
        RespTimeAvgTest respTimeAvgTestB = new RespTimeAvgTest("Test #2", "avgRespTimeTest", "", "Search", 5000);
        RespTimeAvgTest respTimeAvgTestC = new RespTimeAvgTest("Test #3", "avgRespTimeTest", "", "Sear", 1000);

        JMeterTransactions transactions = new JMeterTransactions();
        transactions.add(TestData.LOGIN_3514_SUCCESS);
        transactions.add(TestData.SEARCH_11221_SUCCESS);

        List<ClientSideTest> tests = new ArrayList<>();
        tests.add(respTimeAvgTestA);
        tests.add(respTimeAvgTestB);
        tests.add(respTimeAvgTestC);

        TestSet testSet = new TestSet(tests, null);
        configureStream();
        testSet.executeClientSideTests(transactions);
        revertStream();

        assertThat(testSet.getTestCount(), is(3));
        assertThat(testSet.getPassCount(), is(1));
        assertThat(testSet.getFailCount(), is(1));
        assertThat(testSet.getErrorCount(), is(1));
    }
}