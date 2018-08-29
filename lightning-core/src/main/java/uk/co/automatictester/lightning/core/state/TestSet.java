package uk.co.automatictester.lightning.core.state;

import uk.co.automatictester.lightning.core.enums.TestResult;
import uk.co.automatictester.lightning.core.structures.LightningTests;
import uk.co.automatictester.lightning.core.tests.base.LightningTest;

import java.util.List;

public class TestSet {

    private int passCount = 0;
    private int failCount = 0;
    private int ignoreCount = 0;
    private String testExecutionReport = "";

    public void executeTests() {
        resetTestCounts();
        StringBuilder output = new StringBuilder();
        getTests().forEach(test -> {
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

    public List<LightningTest> getTests() {
        return LightningTests.getTests();
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

    private void resetTestCounts() {
        passCount = 0;
        failCount = 0;
        ignoreCount = 0;
    }

    private void setCounts(LightningTest test) {
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
}
