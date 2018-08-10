package uk.co.automatictester.lightning;

import org.testng.annotations.Test;
import uk.co.automatictester.lightning.data.JMeterTransactions;
import uk.co.automatictester.lightning.data.PerfMonEntries;
import uk.co.automatictester.lightning.enums.ServerSideTestType;
import uk.co.automatictester.lightning.shared.TestData;
import uk.co.automatictester.lightning.tests.ClientSideTest;
import uk.co.automatictester.lightning.tests.PassedTransactionsTest;
import uk.co.automatictester.lightning.tests.RespTimeAvgTest;
import uk.co.automatictester.lightning.tests.ServerSideTest;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TestSetTest extends ConsoleOutputTest {

    @Test
    public void verifyExecuteServerMethod_1_1_1() {
        PassedTransactionsTest passedTransactionsTestA = new PassedTransactionsTest("Test #1", "passedTransactionsTest", "Verify number of passed tests", "Login", 0);
        List<ClientSideTest> clientSideTests = new ArrayList<>();
        clientSideTests.add(passedTransactionsTestA);

        List<String[]> clientSideTestData = new ArrayList<>();
        clientSideTestData.add(TestData.LOGIN_3514_SUCCESS);
        clientSideTestData.add(TestData.SEARCH_11221_SUCCESS);
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(clientSideTestData);

        ServerSideTest testA = new ServerSideTest("Test #1", "serverSideTest", ServerSideTestType.LESS_THAN, "Verify CPU utilisation", "192.168.0.12 CPU", 10001);
        ServerSideTest testB = new ServerSideTest("Test #2", "serverSideTest", ServerSideTestType.GREATER_THAN, "Verify CPU utilisation", "192.168.0.12 CPU", 10001);
        ServerSideTest testC = new ServerSideTest("Test #3", "serverSideTest", ServerSideTestType.GREATER_THAN, "Verify CPU utilisation", "192.168.0.240 CPU", 10001);

        List<String[]> serverSideTestData = new ArrayList<>();
        serverSideTestData.add(TestData.CPU_ENTRY_10000);
        serverSideTestData.add(TestData.CPU_ENTRY_10001);
        PerfMonEntries dataEntries = PerfMonEntries.fromList(serverSideTestData);

        List<ServerSideTest> serverSideTests = new ArrayList<>();
        serverSideTests.add(testA);
        serverSideTests.add(testB);
        serverSideTests.add(testC);

        TestSet testSet = TestSet.fromClientAndServerSideTest(clientSideTests, serverSideTests);
        configureStream();
        testSet.executeClientSideTests(jmeterTransactions);
        testSet.executeServerSideTests(dataEntries);
        revertStream();

        assertThat(testSet.getTestCount(), is(4));
        assertThat(testSet.getPassCount(), is(2));
        assertThat(testSet.getFailCount(), is(1));
        assertThat(testSet.getErrorCount(), is(1));
    }

    @Test
    public void verifyExecuteClientMethod_2_0_0() {
        PassedTransactionsTest passedTransactionsTestA = new PassedTransactionsTest("Test #1", "passedTransactionsTest", "Verify number of passed tests", "Login", 0);
        PassedTransactionsTest passedTransactionsTestB = new PassedTransactionsTest("Test #2", "passedTransactionsTest", "Verify number of passed tests", null, 0);

        List<String[]> testData = new ArrayList<>();
        testData.add(TestData.LOGIN_3514_SUCCESS);
        testData.add(TestData.SEARCH_11221_SUCCESS);
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);

        List<ClientSideTest> tests = new ArrayList<>();
        tests.add(passedTransactionsTestA);
        tests.add(passedTransactionsTestB);

        TestSet testSet = TestSet.fromClientSideTest(tests);
        configureStream();
        testSet.executeClientSideTests(jmeterTransactions);
        revertStream();

        assertThat(testSet.getTestCount(), is(2));
        assertThat(testSet.getPassCount(), is(2));
        assertThat(testSet.getFailCount(), is(0));
        assertThat(testSet.getErrorCount(), is(0));
    }

    @Test
    public void verifyExecuteClientMethod_1_1_1() {
        RespTimeAvgTest respTimeAvgTestA = new RespTimeAvgTest.Builder("Test #1", 4000).withTransactionName("Login").build();
        RespTimeAvgTest respTimeAvgTestB = new RespTimeAvgTest.Builder("Test #2", 5000).withTransactionName("Search").build();
        RespTimeAvgTest respTimeAvgTestC = new RespTimeAvgTest.Builder("Test #3", 1000).withTransactionName("Sear").build();

        List<String[]> testData = new ArrayList<>();
        testData.add(TestData.LOGIN_3514_SUCCESS);
        testData.add(TestData.SEARCH_11221_SUCCESS);
        JMeterTransactions jmeterTransactions = JMeterTransactions.fromList(testData);

        List<ClientSideTest> tests = new ArrayList<>();
        tests.add(respTimeAvgTestA);
        tests.add(respTimeAvgTestB);
        tests.add(respTimeAvgTestC);

        TestSet testSet = TestSet.fromClientSideTest(tests);
        configureStream();
        testSet.executeClientSideTests(jmeterTransactions);
        revertStream();

        assertThat(testSet.getTestCount(), is(3));
        assertThat(testSet.getPassCount(), is(1));
        assertThat(testSet.getFailCount(), is(1));
        assertThat(testSet.getErrorCount(), is(1));
    }
}