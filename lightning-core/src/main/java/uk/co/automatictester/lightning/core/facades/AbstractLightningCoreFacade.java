package uk.co.automatictester.lightning.core.facades;

import uk.co.automatictester.lightning.core.state.data.JmeterTransactions;
import uk.co.automatictester.lightning.core.state.data.TestData;
import uk.co.automatictester.lightning.core.state.tests.TestSet;

public abstract class AbstractLightningCoreFacade {

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
        TestData testData = TestData.getInstance();
        JmeterTransactions jmeterTransactions = testData.clientSideTestData();
        return jmeterTransactions.teamCityBuildReportSummary();
    }

    public String teamCityReportStatistics() {
        TestData testData = TestData.getInstance();
        JmeterTransactions jmeterTransactions = testData.clientSideTestData();
        return jmeterTransactions.teamCityReportStatistics();
    }
}
