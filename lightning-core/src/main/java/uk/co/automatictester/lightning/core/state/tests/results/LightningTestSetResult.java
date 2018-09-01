package uk.co.automatictester.lightning.core.state.tests.results;

import uk.co.automatictester.lightning.core.enums.TestResult;
import uk.co.automatictester.lightning.core.state.tests.LightningTestSet;
import uk.co.automatictester.lightning.core.tests.base.AbstractTest;

import java.util.List;

public class LightningTestSetResult {

    private int passCount = 0;
    private int failCount = 0;
    private int ignoreCount = 0;
    private String testExecutionReport = "";

    public void executeTests() {
        resetTestCounts();
        StringBuilder output = new StringBuilder();
        LightningTestSet tests = LightningTestSet.getInstance();
        tests.get().forEach(test -> {
            test.execute();
            setCounts(test);
            String testExecutionReport = test.getTestExecutionReport();
            output.append(testExecutionReport).append(System.lineSeparator());
        });
        testExecutionReport = output.toString();
    }

    public String getTestExecutionReport() {
        return testExecutionReport;
    }

    public List<AbstractTest> getTests() {
        return LightningTestSet.getInstance().get();
    }

    public int getTestCount() {
        return passCount + failCount + ignoreCount;
    }

    public int getPassCount() {
        return passCount;
    }

    public int getFailCount() {
        return failCount;
    }

    public int getErrorCount() {
        return ignoreCount;
    }

    public String getTestSetExecutionSummaryReport() {
        return String.format("%n============= EXECUTION SUMMARY =============%n"
                        + "Tests executed:    %s%n"
                        + "Tests passed:      %s%n"
                        + "Tests failed:      %s%n"
                        + "Tests errors:      %s%n"
                        + "Test set status:   %s",
                getTestCount(),
                getPassCount(),
                getFailCount(),
                getErrorCount(),
                getTestSetStatus());
    }

    private String getTestSetStatus() {
        return hasFailed() ? "FAIL" : "Pass";
    }

    private boolean hasFailed() {
        return getFailCount() != 0 || getErrorCount() != 0;
    }

    private void resetTestCounts() {
        passCount = 0;
        failCount = 0;
        ignoreCount = 0;
    }

    private void setCounts(AbstractTest test) {
        TestResult testResult = test.getResult();
        switch (testResult) {
            case PASS:
                passCount++;
                break;
            case FAIL:
                failCount++;
                break;
            case ERROR:
                ignoreCount++;
                break;
        }
    }

    @Override
    public String toString() {
        return String.format("Tests: %d, passed: %d, failed: %d, ignored: %d", getTestCount(), passCount, failCount, ignoreCount);
    }
}
