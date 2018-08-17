package uk.co.automatictester.lightning.core.reporters;

import uk.co.automatictester.lightning.core.state.TestSet;

public class TestSetReporter {

    public static String getTestSetExecutionSummaryReport(TestSet testSet) {
        int testCount = testSet.getTestCount();
        int passedTestCount = testSet.getPassCount();
        int failedTestCount = testSet.getFailCount();
        int errorTestCount = testSet.getErrorCount();
        String testSetStatus = getTestSetStatus(testSet);

        return String.format("%n============= EXECUTION SUMMARY =============%n"
                        + "Tests executed:    %s%n"
                        + "Tests passed:      %s%n"
                        + "Tests failed:      %s%n"
                        + "Tests errors:      %s%n"
                        + "Test set status:   %s",
                testCount,
                passedTestCount,
                failedTestCount,
                errorTestCount,
                testSetStatus);
    }

    private static String getTestSetStatus(TestSet testSet) {
        return hasFailed(testSet) ? "FAIL" : "Pass";
    }

    private static boolean hasFailed(TestSet testSet) {
        return testSet.getFailCount() != 0 || testSet.getErrorCount() != 0;
    }

}
