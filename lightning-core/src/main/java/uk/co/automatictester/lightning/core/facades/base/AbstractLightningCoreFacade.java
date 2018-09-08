package uk.co.automatictester.lightning.core.facades.base;

import uk.co.automatictester.lightning.core.reporters.ci.TeamCityReporter;
import uk.co.automatictester.lightning.core.state.data.JmeterTransactions;
import uk.co.automatictester.lightning.core.state.data.TestData;
import uk.co.automatictester.lightning.core.state.tests.TestSet;

public abstract class AbstractLightningCoreFacade {

    protected JmeterTransactions jmeterTransactions;
    protected TestSet testSet;
    private TeamCityReporter teamCityReporter;

    public String executeTests() {
        testSet.executeTests();
        return testSet.testExecutionReport();
    }

    public String runReport() {
        TestData testData = TestData.getInstance();
        JmeterTransactions jmeterTransactions = testData.clientSideTestData();
        return jmeterTransactions.getJmeterReport();
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
        return TeamCityReporter.fromTestSet(testSet).teamCityVerifyStatistics();
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
