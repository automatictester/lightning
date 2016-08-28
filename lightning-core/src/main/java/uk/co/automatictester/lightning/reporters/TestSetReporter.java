package uk.co.automatictester.lightning.reporters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.automatictester.lightning.TestSet;

public class TestSetReporter {

    private TestSet testSet;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public TestSetReporter(TestSet testSet) {
        this.testSet = testSet;
    }

    public void printTestSetExecutionSummaryReport() {
        logger.info(getTestSetExecutionSummaryReport());
    }

    public String getTestSetExecutionSummaryReport() {
        return String.format("%n============= EXECUTION SUMMARY =============%n"
                + "Tests executed:    %s%n"
                + "Tests passed:      %s%n"
                + "Tests failed:      %s%n"
                + "Tests errors:      %s%n"
                + "Test set status:   %s", testSet.getTestCount(), testSet.getPassCount(), testSet.getFailCount(), testSet.getErrorCount(), getTestSetStatus());
    }

    private String getTestSetStatus() {
        return ((testSet.getFailCount() != 0) || (testSet.getErrorCount() != 0)) ? "FAIL" : "Pass";
    }

}
