package uk.co.automatictester.lightning;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.automatictester.lightning.enums.TestResult;
import uk.co.automatictester.lightning.structures.LightningTests;
import uk.co.automatictester.lightning.tests.base.LightningTest;

import java.util.List;

public class TestSet {

    private int passCount = 0;
    private int failCount = 0;
    private int ignoreCount = 0;
    private String testExecutionReport = "";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void executeTests() {
        StringBuilder output = new StringBuilder();
        for (LightningTest test : getTests()) {
            test.execute();
            setCounts(test);
            String testExecutionReport = test.getTestExecutionReport();
            output.append(testExecutionReport).append(System.lineSeparator());
        }
        testExecutionReport += output;
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

    public void printTestExecutionReport() {
        String[] testExecutionReport = getTestExecutionReport().split(System.lineSeparator());
        for (String line : testExecutionReport) {
            logger.info(line);
        }
    }

    public String getTestExecutionReport() {
        return testExecutionReport;
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

    public List<LightningTest> getTests() {
        return LightningTests.getTests();
    }
}
