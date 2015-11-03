package uk.co.automatictester.lightning.ci;

import org.testng.annotations.Test;
import uk.co.automatictester.lightning.ConsoleOutputTest;
import uk.co.automatictester.lightning.JMeterTransactions;
import uk.co.automatictester.lightning.TestSet;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TeamCityReporterTest extends ConsoleOutputTest {

    @Test
    public void testSetTeamCityBuildStatusTextFailedTests_verify() {
        String expectedOutput = String.format("%nSet TeamCity build status text:%n" +
                "##teamcity[buildStatus text='Tests failed: executed: 6, failed: 2, ignored: 1']%n");

        TestSet testSet = mock(TestSet.class);
        when(testSet.getTestCount()).thenReturn(6);
        when(testSet.getFailCount()).thenReturn(2);
        when(testSet.getIgnoreCount()).thenReturn(1);

        configureStream();
        new TeamCityReporter().setTeamCityBuildStatusText(testSet);
        assertThat(out.toString(), containsString(expectedOutput));
        revertStream();
    }

	@Test
	public void testSetTeamCityBuildStatusTextZeroFailedTests_verify() {
		String expectedOutput = String.format("%nSet TeamCity build status text:%n" +
				"##teamcity[buildStatus text='Tests passed: executed: 6, failed: 0, ignored: 1']%n");

		TestSet testSet = mock(TestSet.class);
		when(testSet.getTestCount()).thenReturn(6);
		when(testSet.getFailCount()).thenReturn(0);
		when(testSet.getIgnoreCount()).thenReturn(1);

		configureStream();
		new TeamCityReporter().setTeamCityBuildStatusText(testSet);
		assertThat(out.toString(), containsString(expectedOutput));
		revertStream();
	}

    @Test
    public void testSetTeamCityBuildStatusText_report() {
        String expectedOutput = String.format("%nSet TeamCity build status text:%n" +
                "##teamcity[buildStatus text='Transactions executed: 10, failed: 2']%n");

        JMeterTransactions jmeterTransactions = mock(JMeterTransactions.class);
        when(jmeterTransactions.getTransactionCount()).thenReturn(10);
        when(jmeterTransactions.getFailCount()).thenReturn(2);

        configureStream();
        new TeamCityReporter().setTeamCityBuildStatusText(jmeterTransactions);
        assertThat(out.toString(), containsString(expectedOutput));
        revertStream();
    }
}