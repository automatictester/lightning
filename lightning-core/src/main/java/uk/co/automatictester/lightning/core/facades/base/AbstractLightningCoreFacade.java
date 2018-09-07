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
        return testSetResult.testExecutionReport();
    }

    public String runReport() {
        TestData testData = TestData.getInstance();
        JmeterTransactions jmeterTransactions = testData.clientSideTestData();
        return jmeterTransactions.getJmeterReport();
    }

    public String testSetExecutionSummaryReport() {
        return testSetResult.testSetExecutionSummaryReport();
    }

    public boolean hasExecutionFailed() {
        return testSetResult.failCount() + testSetResult.errorCount() != 0;
    }

    public boolean hasFailedTransactions() {
        TestData testData = TestData.getInstance();
        JmeterTransactions jmeterTransactions = testData.clientSideTestData();
        return jmeterTransactions.failCount() != 0;
    }

    public String teamCityVerifyStatistics() {
        return TeamCityReporter.fromTestSet(testSetResult).teamCityVerifyStatistics();
    }

    public String teamCityBuildReportSummary() {
        setTeamCityReporter();
        return teamCityReporter.teamCityBuildReportSummary();
    }

    public String teamCityReportStatistics() {
        setTeamCityReporter();
        return teamCityReporter.teamCityReportStatistics();
    }

    private void setTeamCityReporter() {
        if (teamCityReporter == null) {
            teamCityReporter = TeamCityReporter.fromJmeterTransactions(jmeterTransactions);
        }
    }
}
