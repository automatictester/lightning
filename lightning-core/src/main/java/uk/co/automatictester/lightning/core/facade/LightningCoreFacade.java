package uk.co.automatictester.lightning.core.facade;

import uk.co.automatictester.lightning.core.ci.TeamCityReporter;
import uk.co.automatictester.lightning.core.data.JMeterTransactions;
import uk.co.automatictester.lightning.core.reporters.JMeterReporter;
import uk.co.automatictester.lightning.core.reporters.TestSetReporter;
import uk.co.automatictester.lightning.core.state.TestSet;

public abstract class LightningCoreFacade {

    protected TestSet testSet = new TestSet();
    protected JMeterTransactions jmeterTransactions;
    private TeamCityReporter teamCityReporter;

    public String executeTests() {
        testSet.executeTests();
        return testSet.getTestExecutionReport();
    }

    public String runReport() {
        return JMeterReporter.getJMeterReport(jmeterTransactions);
    }

    public String getTestSetExecutionSummaryReport() {
        return TestSetReporter.getTestSetExecutionSummaryReport(testSet);
    }

    public boolean hasExecutionFailed() {
        return testSet.getFailCount() + testSet.getErrorCount() != 0;
    }

    public boolean hasFailedTransactions() {
        return jmeterTransactions.getFailCount() != 0;
    }

    public String getTeamCityVerifyStatistics() {
        return TeamCityReporter.fromTestSet(testSet).getTeamCityVerifyStatistics();
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
            teamCityReporter = TeamCityReporter.fromJMeterTransactions(jmeterTransactions);
        }
    }
}
