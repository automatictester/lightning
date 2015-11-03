package uk.co.automatictester.lightning.ci;

import uk.co.automatictester.lightning.JMeterTransactions;
import uk.co.automatictester.lightning.TestSet;

public class TeamCityReporter extends CIReporter {

    private static final String teamCityOutput = "%nSet TeamCity build status text:%n" +
            "##teamcity[buildStatus text='%s']";

    public void setTeamCityBuildStatusText(TestSet testSet) {
        System.out.println(String.format(teamCityOutput, getVerifySummary(testSet)));
    }

    public void setTeamCityBuildStatusText(JMeterTransactions jmeterTransactions) {
        System.out.println(String.format(teamCityOutput, getReportSummary(jmeterTransactions)));
    }

	public static String getVerifySummary(TestSet testSet) {
		int executed = testSet.getTestCount();
		int failed = testSet.getFailCount();
		int ignored = testSet.getIgnoreCount();
		String testMsgHead = failed > 0 ? "Tests failed:" : "Tests passed:";
		return String.format(testMsgHead + " executed: %s, failed: %s, ignored: %s", executed, failed, ignored);
	}

}
