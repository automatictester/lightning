package uk.co.automatictester.jmeter.lightning.maven.plugin;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import uk.co.automatictester.lightning.core.state.TestSet;
import uk.co.automatictester.lightning.core.ci.JUnitReporter;
import uk.co.automatictester.lightning.core.ci.JenkinsReporter;
import uk.co.automatictester.lightning.core.ci.TeamCityReporter;
import uk.co.automatictester.lightning.core.data.JMeterTransactions;
import uk.co.automatictester.lightning.core.data.PerfMonEntries;
import uk.co.automatictester.lightning.core.config.LightningConfig;
import uk.co.automatictester.lightning.core.reporters.JMeterReporter;
import uk.co.automatictester.lightning.core.reporters.TestSetReporter;
import uk.co.automatictester.lightning.core.structures.TestData;

@Mojo(name = "lightning", defaultPhase = LifecyclePhase.POST_INTEGRATION_TEST)
public class LightningMojo extends ConfigurationMojo {

    private int exitCode = 0;
    private TestSet testSet = new TestSet();
    private JMeterTransactions jmeterTransactions;

    @Override
    public void execute() throws MojoExecutionException {
        switch (mode) {
            case verify:
                runTests();
                saveJunitReport();
                break;
            case report:
                runReport();
                break;
        }
        notifyCIServer();
        setExitCode();
    }

    private void saveJunitReport() {
        JUnitReporter junitreporter = new JUnitReporter();
        junitreporter.generateJUnitReport(testSet);
    }

    private void runTests() {
        long testSetExecStart = System.currentTimeMillis();

        LightningConfig lightningConfig = new LightningConfig();
        lightningConfig.readTests(testSetXml);

        jmeterTransactions = JMeterTransactions.fromFile(jmeterCsv);
        TestData.addClientSideTestData(jmeterTransactions);
        loadPerfMonDataIfProvided();
        testSet.executeTests();
        log(testSet.getTestExecutionReport());

        log(TestSetReporter.getTestSetExecutionSummaryReport(testSet));

        long testSetExecEnd = System.currentTimeMillis();
        long testExecTime = testSetExecEnd - testSetExecStart;
        log(String.format("Execution time:    %dms", testExecTime));

        if (testSet.getFailCount() + testSet.getErrorCount() != 0) {
            exitCode = 1;
        }
    }

    private void loadPerfMonDataIfProvided() {
        if (perfmonCsv != null) {
            PerfMonEntries perfMonDataEntries = PerfMonEntries.fromFile(perfmonCsv);
            TestData.addServerSideTestData(perfMonDataEntries);
        }
    }

    private void runReport() {
        jmeterTransactions = JMeterTransactions.fromFile(jmeterCsv);
        log(JMeterReporter.getJMeterReport(jmeterTransactions));
        if (jmeterTransactions.getFailCount() != 0) {
            exitCode = 1;
        }
    }

    private void notifyCIServer() {
        switch (mode) {
            case verify:
                log(TeamCityReporter.fromTestSet(testSet).getTeamCityVerifyStatistics());
                JenkinsReporter.fromTestSet(testSet).setJenkinsBuildName();
                break;
            case report:
                TeamCityReporter teamCityReporter = TeamCityReporter.fromJMeterTransactions(jmeterTransactions);
                log(teamCityReporter.getTeamCityBuildReportSummary());
                log(teamCityReporter.getTeamCityReportStatistics());
                JenkinsReporter.fromJMeterTransactions(jmeterTransactions).setJenkinsBuildName();
                break;
        }
    }

    private void setExitCode() throws MojoExecutionException {
        if (exitCode != 0) {
            throw new MojoExecutionException("");
        }
    }

    private void log(String text) {
        for (String line : text.split(System.lineSeparator())) {
            getLog().info(line);
        }
    }
}