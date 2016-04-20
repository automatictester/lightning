package uk.co.automatictester.lightning.tests;

import org.testng.annotations.Test;
import uk.co.automatictester.lightning.ConsoleOutputTest;
import uk.co.automatictester.lightning.data.JMeterTransactions;
import uk.co.automatictester.lightning.data.PerfMonDataEntries;
import uk.co.automatictester.lightning.enums.ServerSideTestType;
import uk.co.automatictester.lightning.enums.TestResult;

import java.util.Locale;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static uk.co.automatictester.lightning.shared.TestData.*;

public class ServerSideTestTest extends ConsoleOutputTest {

    @Test
    public void verifyExecute_LessThan_Pass() {
        ServerSideTest test = new ServerSideTest("Test #1", "serverSideTest", ServerSideTestType.LESS_THAN, "Verify CPU utilisation", "192.168.0.12 CPU", 12501);
        PerfMonDataEntries dataEntries = new PerfMonDataEntries();
        dataEntries.add(CPU_ENTRY_10000);
        dataEntries.add(CPU_ENTRY_15000);
        test.execute(dataEntries);
        assertThat(test.getResult(), is(equalTo(TestResult.PASS)));
    }

    @Test
    public void verifyExecute_LessThan_Fail() {
        ServerSideTest test = new ServerSideTest("Test #1", "serverSideTest", ServerSideTestType.LESS_THAN, "Verify CPU utilisation", "192.168.0.12 CPU", 27500);
        PerfMonDataEntries dataEntries = new PerfMonDataEntries();
        dataEntries.add(CPU_ENTRY_25000);
        dataEntries.add(CPU_ENTRY_30000);
        test.execute(dataEntries);
        assertThat(test.getResult(), is(equalTo(TestResult.FAIL)));
    }

    @Test
    public void verifyExecute_GreaterThan_Pass() {
        ServerSideTest test = new ServerSideTest("Test #1", "serverSideTest", ServerSideTestType.GREATER_THAN, "Verify CPU utilisation", "192.168.0.12 CPU", 27499);
        PerfMonDataEntries dataEntries = new PerfMonDataEntries();
        dataEntries.add(CPU_ENTRY_25000);
        dataEntries.add(CPU_ENTRY_30000);
        test.execute(dataEntries);
        assertThat(test.getResult(), is(equalTo(TestResult.PASS)));
    }

    @Test
    public void verifyExecute_GreaterThan_Fail() {
        ServerSideTest test = new ServerSideTest("Test #1", "serverSideTest", ServerSideTestType.GREATER_THAN, "Verify CPU utilisation", "192.168.0.12 CPU", 12500);
        PerfMonDataEntries dataEntries = new PerfMonDataEntries();
        dataEntries.add(CPU_ENTRY_10000);
        dataEntries.add(CPU_ENTRY_15000);
        test.execute(dataEntries);
        assertThat(test.getResult(), is(equalTo(TestResult.FAIL)));
    }

    @Test
    public void verifyExecute_Between_Pass() {
        ServerSideTest test = new ServerSideTest("Test #1", "serverSideTest", ServerSideTestType.BETWEEN, "Verify CPU utilisation", "192.168.0.12 CPU", 20000, 27501);
        PerfMonDataEntries dataEntries = new PerfMonDataEntries();
        dataEntries.add(CPU_ENTRY_25000);
        dataEntries.add(CPU_ENTRY_30000);
        test.execute(dataEntries);
        assertThat(test.getResult(), is(equalTo(TestResult.PASS)));
    }

    @Test
    public void verifyExecute_Between_Fail() {
        ServerSideTest test = new ServerSideTest("Test #1", "serverSideTest", ServerSideTestType.BETWEEN, "Verify CPU utilisation", "192.168.0.12 CPU", 10000, 12499);
        PerfMonDataEntries dataEntries = new PerfMonDataEntries();
        dataEntries.add(CPU_ENTRY_10000);
        dataEntries.add(CPU_ENTRY_15000);
        test.execute(dataEntries);
        assertThat(test.getResult(), is(equalTo(TestResult.FAIL)));
    }

    @Test
    public void verifyExecute_OneEntry_LessThan_Pass() {
        ServerSideTest test = new ServerSideTest("Test #1", "serverSideTest", ServerSideTestType.LESS_THAN, "Verify CPU utilisation", "192.168.0.12 CPU", 10001);
        PerfMonDataEntries dataEntries = new PerfMonDataEntries();
        dataEntries.add(CPU_ENTRY_10000);
        test.execute(dataEntries);
        assertThat(test.getResult(), is(equalTo(TestResult.PASS)));
    }

    @Test
    public void verifyExecute_LessThan_Pass_NonDefaultLocale() {
        Locale.setDefault(Locale.FRENCH);
        ServerSideTest test = new ServerSideTest("Test #1", "serverSideTest", ServerSideTestType.LESS_THAN, "Verify CPU utilisation", "192.168.0.12 CPU", 10001);
        PerfMonDataEntries dataEntries = new PerfMonDataEntries();
        dataEntries.add(CPU_ENTRY_10000);
        dataEntries.add(CPU_ENTRY_10001);
        test.execute(dataEntries);
        assertThat(test.getResult(), is(equalTo(TestResult.PASS)));
    }

    @Test
    public void verifyExecute_LessThan_Error() {
        ServerSideTest test = new ServerSideTest("Test #1", "serverSideTest", ServerSideTestType.LESS_THAN, "Verify CPU utilisation", "192.168.0.13 CPU", 10001);
        PerfMonDataEntries dataEntries = new PerfMonDataEntries();
        dataEntries.add(CPU_ENTRY_10000);
        dataEntries.add(CPU_ENTRY_10001);
        test.execute(dataEntries);
        assertThat(test.getResult(), is(equalTo(TestResult.IGNORED)));
    }

    @Test
    public void testPrintTestExecutionReportPass() {
        String expectedOutput = String.format("Test name:            Test #1%n" +
                "Test type:            serverSideTest%n" +
                "Test subtype:         Less than%n" +
                "Test description:     Verify CPU utilisation%n" +
                "Host and metric:      192.168.0.12 CPU%n" +
                "Expected result:      Average value < 10001%n" +
                "Actual result:        Average value = 10000.5%n" +
                "Entries count:        2%n" +
                "Test result:          Pass");

        ServerSideTest test = new ServerSideTest("Test #1", "serverSideTest", ServerSideTestType.LESS_THAN, "Verify CPU utilisation", "192.168.0.12 CPU", 10001);
        PerfMonDataEntries dataEntries = new PerfMonDataEntries();
        dataEntries.add(CPU_ENTRY_10000);
        dataEntries.add(CPU_ENTRY_10001);

        configureStream();
        test.execute(dataEntries);
        test.printTestExecutionReport();
        assertThat(out.toString(), containsString(expectedOutput));
        revertStream();
    }

    @Test
    public void verifyIsEqual() {
        ServerSideTest testA = new ServerSideTest("Test #1", "serverSideTest", ServerSideTestType.LESS_THAN, "Verify CPU utilisation", "192.168.0.13 CPU", 10000);
        ServerSideTest testA2 = new ServerSideTest("Test #1", "serverSideTest", ServerSideTestType.LESS_THAN, "Verify CPU utilisation", "192.168.0.13 CPU", 10000);
        assertThat(testA, is(equalTo(testA2)));
    }

    @Test
    public void verifyIsNotEqual() {
        ServerSideTest testA = new ServerSideTest("Test #1", "serverSideTest", ServerSideTestType.LESS_THAN, "Verify CPU utilisation", "192.168.0.13 CPU", 10000);
        ServerSideTest testB = new ServerSideTest("Test #1", "serverSideTest", ServerSideTestType.LESS_THAN, "Verify CPU utilisation", "192.168.0.13 CPU", 10001);
        assertThat(testA, is(not(equalTo(testB))));
    }

    @Test
    public void verifyIsNotEqualOtherTestType() {
        ServerSideTest testA = new ServerSideTest("Test #1", "serverSideTest", ServerSideTestType.LESS_THAN, "Verify CPU utilisation", "192.168.0.13 CPU", 10000);
        assertThat((Object) testA, is(not(equalTo((Object) RESP_TIME_PERC_TEST_A))));
    }

}