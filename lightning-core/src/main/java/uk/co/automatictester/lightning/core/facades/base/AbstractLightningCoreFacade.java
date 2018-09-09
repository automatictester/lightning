package uk.co.automatictester.lightning.core.facades.base;

import uk.co.automatictester.lightning.core.state.data.JmeterTransactions;
import uk.co.automatictester.lightning.core.state.data.TestData;
import uk.co.automatictester.lightning.core.state.tests.TestSet;

public abstract class AbstractLightningCoreFacade {

    protected JmeterTransactions jmeterTransactions;
    protected TestSet testSet;

    public String executeTests() {
        testSet.executeTests();
        return testSet.testExecutionReport();
    }

    public String runReport() {
        TestData testData = TestData.getInstance();
        JmeterTransactions jmeterTransactions = testData.clientSideTestData();
        return jmeterTransactions.summaryReport();
    }

    public String testSetExecutionSummaryReport() {
        return testSet.testSetExecutionSummaryReport();
    }

    public boolean hasExecutionFailed() {
        return testSet.hasFailed();
    }

    public boolean hasFailedTransactions() {
        TestData testData = TestData.getInstance();
        JmeterTransactions jmeterTransactions = testData.clientSideTestData();
        return jmeterTransactions.failCount() != 0;
    }

    public String teamCityVerifyStatistics() {
        return testSet.teamCityVerifyStatistics();
    }

    public String teamCityBuildReportSummary() {
        return jmeterTransactions.teamCityBuildReportSummary();
    }

    public String teamCityReportStatistics() {
        return jmeterTransactions.teamCityReportStatistics();
    }
}
