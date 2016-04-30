package uk.co.automatictester.lightning.reporters;

import org.testng.annotations.Test;
import uk.co.automatictester.lightning.ConsoleOutputTest;
import uk.co.automatictester.lightning.TestSet;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestSetReporterTest extends ConsoleOutputTest {

    @Test
    public void testPrintTestSetExecutionSummaryReportForPass() {
        String expectedResult = String.format("============= EXECUTION SUMMARY =============%n" +
                "Tests executed:    15%n" +
                "Tests passed:      15%n" +
                "Tests failed:      0%n" +
                "Tests errors:      0%n" +
                "Test set status:   Pass%n");

        TestSet testSet = mock(TestSet.class);
        when(testSet.getTestCount()).thenReturn(15);
        when(testSet.getPassCount()).thenReturn(15);
        when(testSet.getFailCount()).thenReturn(0);
        when(testSet.getErrorCount()).thenReturn(0);

        configureStream();
        new TestSetReporter(testSet).printTestSetExecutionSummaryReport();
        revertStream();

        assertThat(out.toString(), containsString(expectedResult));
    }

    @Test
    public void testPrintTestSetExecutionSummaryReportForFail() {
        String expectedResult = String.format("============= EXECUTION SUMMARY =============%n" +
                "Tests executed:    15%n" +
                "Tests passed:      14%n" +
                "Tests failed:      1%n" +
                "Tests errors:      0%n" +
                "Test set status:   FAIL%n");

        TestSet testSet = mock(TestSet.class);
        when(testSet.getTestCount()).thenReturn(15);
        when(testSet.getPassCount()).thenReturn(14);
        when(testSet.getFailCount()).thenReturn(1);
        when(testSet.getErrorCount()).thenReturn(0);

        configureStream();
        new TestSetReporter(testSet).printTestSetExecutionSummaryReport();
        revertStream();

        assertThat(out.toString(), containsString(expectedResult));
    }

    @Test
    public void testPrintTestSetExecutionSummaryReportForIgnore() {
        String expectedResult = String.format("============= EXECUTION SUMMARY =============%n" +
                "Tests executed:    15%n" +
                "Tests passed:      14%n" +
                "Tests failed:      0%n" +
                "Tests errors:      1%n" +
                "Test set status:   FAIL%n");

        TestSet testSet = mock(TestSet.class);
        when(testSet.getTestCount()).thenReturn(15);
        when(testSet.getPassCount()).thenReturn(14);
        when(testSet.getFailCount()).thenReturn(0);
        when(testSet.getErrorCount()).thenReturn(1);

        configureStream();
        new TestSetReporter(testSet).printTestSetExecutionSummaryReport();
        revertStream();

        assertThat(out.toString(), containsString(expectedResult));
    }


}