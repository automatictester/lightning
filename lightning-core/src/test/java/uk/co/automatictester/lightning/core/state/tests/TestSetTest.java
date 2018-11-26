package uk.co.automatictester.lightning.core.state.tests;

import org.testng.annotations.Test;
import uk.co.automatictester.lightning.core.AbstractConsoleOutputTest;
import uk.co.automatictester.lightning.core.state.data.TestData;
import uk.co.automatictester.lightning.core.tests.*;
import uk.co.automatictester.lightning.shared.LegacyTestData;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class TestSetTest extends AbstractConsoleOutputTest {

    @Test
    public void verifyExecuteServerMethod_2_1_1() {
        PassedTransactionsAbsoluteTest passedTransactionsAbsoluteTestA = new PassedTransactionsAbsoluteTest.Builder("Test #1", 0).withDescription("Verify number of passed tests").withTransactionName("Login").build();
        List<LightningTest> tests = new ArrayList<>();
        tests.add(passedTransactionsAbsoluteTestA);

        List<String[]> clientSideTestData = new ArrayList<>();
        clientSideTestData.add(LegacyTestData.LOGIN_3514_SUCCESS);
        clientSideTestData.add(LegacyTestData.SEARCH_11221_SUCCESS);
        TestData.getInstance().addClientSideTestData(clientSideTestData);

        ServerSideLessThanTest testA = new ServerSideLessThanTest.Builder("Test #1", 10001).withDescription("Verify CPU utilisation").withHostAndMetric("192.168.0.12 CPU").build();
        ServerSideGreaterThanTest testB = new ServerSideGreaterThanTest.Builder("Test #2", 10001).withDescription("Verify CPU utilisation").withHostAndMetric("192.168.0.12 CPU").build();
        ServerSideGreaterThanTest testC = new ServerSideGreaterThanTest.Builder("Test #3", 10001).withDescription("Verify CPU utilisation").withHostAndMetric("192.168.0.240 CPU").build();

        List<String[]> serverSideTestData = new ArrayList<>();
        serverSideTestData.add(LegacyTestData.CPU_ENTRY_10000);
        serverSideTestData.add(LegacyTestData.CPU_ENTRY_10001);
        TestData.getInstance().addServerSideTestData(serverSideTestData);

        tests.add(testA);
        tests.add(testB);
        tests.add(testC);

        TestSet testSet = new TestSet();
        testSet.addAll(tests);
        configureStream();
        testSet.executeTests();
        revertStream();

        assertThat(testSet.size(), is(4));
        assertThat(testSet.failCount(), is(1));
        assertThat(testSet.errorCount(), is(1));

        assertThat(testSet.jenkinsSummaryReport(), containsString("Tests executed: 4, failed: 2"));
    }

    @Test
    public void verifyExecuteClientMethod_2_0_0() {
        PassedTransactionsAbsoluteTest passedTransactionsAbsoluteTestA = new PassedTransactionsAbsoluteTest.Builder("Test #1", 0).withDescription("Verify number of passed tests").withTransactionName("Login").build();
        PassedTransactionsAbsoluteTest passedTransactionsAbsoluteTestB = new PassedTransactionsAbsoluteTest.Builder("Test #2", 0).withDescription("Verify number of passed tests").build();

        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.LOGIN_3514_SUCCESS);
        testData.add(LegacyTestData.SEARCH_11221_SUCCESS);
        TestData.getInstance().addClientSideTestData(testData);

        List<LightningTest> tests = new ArrayList<>();
        tests.add(passedTransactionsAbsoluteTestA);
        tests.add(passedTransactionsAbsoluteTestB);

        TestSet testSet = new TestSet();
        testSet.addAll(tests);
        configureStream();
        testSet.executeTests();
        revertStream();

        assertThat(testSet.size(), is(2));
        assertThat(testSet.failCount(), is(0));
        assertThat(testSet.errorCount(), is(0));
    }

    @Test
    public void verifyExecuteClientMethod_1_1_1() {
        RespTimeAvgTest respTimeAvgTestA = new RespTimeAvgTest.Builder("Test #1", 4000).withTransactionName("Login").build();
        RespTimeAvgTest respTimeAvgTestB = new RespTimeAvgTest.Builder("Test #2", 5000).withTransactionName("Search").build();
        RespTimeAvgTest respTimeAvgTestC = new RespTimeAvgTest.Builder("Test #3", 1000).withTransactionName("Sear").build();

        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.LOGIN_3514_SUCCESS);
        testData.add(LegacyTestData.SEARCH_11221_SUCCESS);
        TestData.getInstance().addClientSideTestData(testData);

        List<LightningTest> tests = new ArrayList<>();
        tests.add(respTimeAvgTestA);
        tests.add(respTimeAvgTestB);
        tests.add(respTimeAvgTestC);

        TestSet testSet = new TestSet();
        testSet.addAll(tests);
        configureStream();
        testSet.executeTests();
        revertStream();

        assertThat(testSet.size(), is(3));
        assertThat(testSet.failCount(), is(1));
        assertThat(testSet.errorCount(), is(1));
    }

    @Test
    public void testPrintTestSetExecutionReport() {
        PassedTransactionsAbsoluteTest passedTransactionsAbsoluteTestA = new PassedTransactionsAbsoluteTest.Builder("Test #1", 0).withDescription("Verify number of passed tests").withTransactionName("Login").build();
        PassedTransactionsAbsoluteTest passedTransactionsAbsoluteTestB = new PassedTransactionsAbsoluteTest.Builder("Test #2", 0).withDescription("Verify number of passed tests").build();

        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.LOGIN_3514_SUCCESS);
        testData.add(LegacyTestData.SEARCH_11221_SUCCESS);
        TestData.getInstance().addClientSideTestData(testData);

        List<LightningTest> tests = new ArrayList<>();
        tests.add(passedTransactionsAbsoluteTestA);
        tests.add(passedTransactionsAbsoluteTestB);

        TestSet testSet = new TestSet();
        testSet.addAll(tests);
        configureStream();
        testSet.executeTests();
        revertStream();

        String expectedResult = String.format("Test name:            Test #1%n" +
                "Test type:            passedTransactionsTest%n" +
                "Test description:     Verify number of passed tests%n" +
                "Transaction name:     Login%n" +
                "Expected result:      Number of failed transactions <= 0%n" +
                "Actual result:        Number of failed transactions = 0%n" +
                "Transaction count:    1%n" +
                "Test result:          Pass%n" +
                "%n" +
                "Test name:            Test #2%n" +
                "Test type:            passedTransactionsTest%n" +
                "Test description:     Verify number of passed tests%n" +
                "Expected result:      Number of failed transactions <= 0%n" +
                "Actual result:        Number of failed transactions = 0%n" +
                "Transaction count:    2%n" +
                "Test result:          Pass");

        String output = testSet.testExecutionReport();
        assertThat(output, containsString(expectedResult));
    }

    @Test
    public void testPrintTestSetExecutionSummaryReportForPass() {
        PassedTransactionsAbsoluteTest passedTransactionsAbsoluteTestA = new PassedTransactionsAbsoluteTest.Builder("Test #1", 0).withDescription("Verify number of passed tests").withTransactionName("Login").build();
        PassedTransactionsAbsoluteTest passedTransactionsAbsoluteTestB = new PassedTransactionsAbsoluteTest.Builder("Test #2", 0).withDescription("Verify number of passed tests").build();

        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.LOGIN_3514_SUCCESS);
        testData.add(LegacyTestData.SEARCH_11221_SUCCESS);
        TestData.getInstance().addClientSideTestData(testData);

        List<LightningTest> tests = new ArrayList<>();
        tests.add(passedTransactionsAbsoluteTestA);
        tests.add(passedTransactionsAbsoluteTestB);

        TestSet testSet = new TestSet();
        testSet.addAll(tests);
        configureStream();
        testSet.executeTests();
        revertStream();

        String expectedResult = String.format("============= EXECUTION SUMMARY =============%n" +
                "Tests executed:    2%n" +
                "Tests passed:      2%n" +
                "Tests failed:      0%n" +
                "Tests errors:      0%n" +
                "Test set status:   Pass");

        String output = testSet.testSetExecutionSummaryReport();
        assertThat(output, containsString(expectedResult));
        assertFalse(testSet.hasFailed());
    }

    @Test
    public void testPrintTestSetExecutionSummaryReportForFail() {
        RespTimeAvgTest respTimeAvgTestA = new RespTimeAvgTest.Builder("Test #1", 4000).withTransactionName("Login").build();
        RespTimeAvgTest respTimeAvgTestB = new RespTimeAvgTest.Builder("Test #2", 5000).withTransactionName("Search").build();
        RespTimeAvgTest respTimeAvgTestC = new RespTimeAvgTest.Builder("Test #3", 1000).withTransactionName("Search").build();

        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.LOGIN_3514_SUCCESS);
        testData.add(LegacyTestData.SEARCH_11221_SUCCESS);
        TestData.getInstance().addClientSideTestData(testData);

        List<LightningTest> tests = new ArrayList<>();
        tests.add(respTimeAvgTestA);
        tests.add(respTimeAvgTestB);
        tests.add(respTimeAvgTestC);

        TestSet testSet = new TestSet();
        testSet.addAll(tests);
        configureStream();
        testSet.executeTests();
        revertStream();

        String expectedResult = String.format("============= EXECUTION SUMMARY =============%n" +
                "Tests executed:    3%n" +
                "Tests passed:      1%n" +
                "Tests failed:      2%n" +
                "Tests errors:      0%n" +
                "Test set status:   FAIL");

        String output = testSet.testSetExecutionSummaryReport();
        assertThat(output, containsString(expectedResult));
        assertTrue(testSet.hasFailed());
    }

    @Test
    public void testPrintTestSetExecutionSummaryReportForIgnore() {
        RespTimeAvgTest respTimeAvgTestA = new RespTimeAvgTest.Builder("Test #1", 4000).withTransactionName("Login").build();
        RespTimeAvgTest respTimeAvgTestC = new RespTimeAvgTest.Builder("Test #3", 1000).withTransactionName("Sear").build();

        List<String[]> testData = new ArrayList<>();
        testData.add(LegacyTestData.LOGIN_3514_SUCCESS);
        testData.add(LegacyTestData.SEARCH_11221_SUCCESS);
        TestData.getInstance().addClientSideTestData(testData);

        List<LightningTest> tests = new ArrayList<>();
        tests.add(respTimeAvgTestA);
        tests.add(respTimeAvgTestC);

        TestSet testSet = new TestSet();
        testSet.addAll(tests);
        configureStream();
        testSet.executeTests();
        revertStream();

        String expectedResult = String.format("============= EXECUTION SUMMARY =============%n" +
                "Tests executed:    2%n" +
                "Tests passed:      1%n" +
                "Tests failed:      0%n" +
                "Tests errors:      1%n" +
                "Test set status:   FAIL");

        String output = testSet.testSetExecutionSummaryReport();
        assertThat(output, containsString(expectedResult));
    }

    @Test
    public void testTeamCityVerifyStatistics() {
        PassedTransactionsAbsoluteTest clientTest = new PassedTransactionsAbsoluteTest.Builder("Failed transactions", 0).build();
        ServerSideLessThanTest serverTest = new ServerSideLessThanTest.Builder("CPU utilization", 40000).withHostAndMetric("192.168.0.12 CPU").build();
        List<LightningTest> tests = new ArrayList<>();
        tests.add(clientTest);
        tests.add(serverTest);
        TestSet testSet = new TestSet();
        testSet.addAll(tests);

        List<String[]> clientSideTestData = new ArrayList<>();
        clientSideTestData.add(LegacyTestData.LOGIN_3514_SUCCESS);
        clientSideTestData.add(LegacyTestData.LOGIN_1200_FAILURE);
        TestData.getInstance().addClientSideTestData(clientSideTestData);

        List<String[]> serverSideTestData = new ArrayList<>();
        serverSideTestData.add(LegacyTestData.CPU_ENTRY_10000);
        serverSideTestData.add(LegacyTestData.CPU_ENTRY_30000);
        TestData.getInstance().addServerSideTestData(serverSideTestData);

        configureStream();
        testSet.executeTests();
        revertStream();

        String output = testSet.teamCityVerifyStatistics();
        assertThat(output, containsString("##teamcity[buildStatisticValue key='Failed transactions' value='1']"));
        assertThat(output, containsString("##teamcity[buildStatisticValue key='CPU utilization' value='20000']"));
    }
}
