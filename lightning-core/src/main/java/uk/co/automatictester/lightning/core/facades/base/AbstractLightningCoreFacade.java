package uk.co.automatictester.lightning.core.facades.base;

import uk.co.automatictester.lightning.core.reporters.ci.TeamCityReporter;
import uk.co.automatictester.lightning.core.state.data.JmeterTransactions;
import uk.co.automatictester.lightning.core.state.data.TestData;
import uk.co.automatictester.lightning.core.state.tests.results.LightningTestSetResult;

public abstract class AbstractLightningCoreFacade {

    protected LightningTestSetResult testSetResult = new LightningTestSetResult();
    protected JmeterTransactions jmeterTransactions;
    private TeamCityReporter teamCityReporter;

    public String executeTests() {
        testSetResult.executeTests();
        return testSetResult.getTestExecutionReport();
    }

    public String runReport() {
        TestData testData = TestData.getInstance();
        JmeterTransactions jmeterTransactions = testData.getClientSideTestData();
        return jmeterTransactions.getJmeterReport();
    }

    public String getTestSetExecutionSummaryReport() {
        return testSetResult.getTestSetExecutionSummaryReport();
    }

    public boolean hasExecutionFailed() {
        return testSetResult.getFailCount() + testSetResult.getErrorCount() != 0;
    }

    public boolean hasFailedTransactions() {
        TestData testData = TestData.getInstance();
        JmeterTransactions jmeterTransactions = testData.getClientSideTestData();
        return jmeterTransactions.getFailCount() != 0;
    }

    public String getTeamCityVerifyStatistics() {
        return TeamCityReporter.fromTestSet(testSetResult).getTeamCityVerifyStatistics();
    }

    public String getTeamCityBuildReportSummary() {
        setTeamCityReporter();
        return teamCityReporter.getTeamCityBuildReportSummary();
    }

    public String getTeamCityReportStatistics() {
        setTeamCityReporter();
        return teamCityReporter.getTeamCityReportStatistics();
    }

    private void setTeamCityReporter() {
        if (teamCityReporter == null) {
            teamCityReporter = TeamCityReporter.fromJmeterTransactions(jmeterTransactions);
        }
    }
}
