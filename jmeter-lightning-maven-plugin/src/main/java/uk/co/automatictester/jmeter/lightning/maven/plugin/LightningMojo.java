package uk.co.automatictester.jmeter.lightning.maven.plugin;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import uk.co.automatictester.lightning.TestSet;
import uk.co.automatictester.lightning.ci.JUnitReporter;
import uk.co.automatictester.lightning.ci.JenkinsReporter;
import uk.co.automatictester.lightning.ci.TeamCityReporter;
import uk.co.automatictester.lightning.data.JMeterTransactions;
import uk.co.automatictester.lightning.data.PerfMonEntries;
import uk.co.automatictester.lightning.readers.LightningXMLFileReader;
import uk.co.automatictester.lightning.reporters.JMeterReporter;
import uk.co.automatictester.lightning.reporters.TestSetReporter;
import uk.co.automatictester.lightning.tests.ClientSideTest;
import uk.co.automatictester.lightning.tests.ServerSideTest;

import java.util.Arrays;
import java.util.List;

@Mojo(name = "lightning", defaultPhase = LifecyclePhase.POST_INTEGRATION_TEST)
public class LightningMojo extends ConfigurationMojo {

    private int exitCode = 0;
    private TestSet testSet;
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

    private void setExitCode() throws MojoExecutionException {
        if (exitCode != 0) {
            throw new MojoExecutionException("");
        }
    }

    private void saveJunitReport() {
        JUnitReporter junitreporter = new JUnitReporter();
        junitreporter.generateJUnitReport(testSet);
    }

    private void runTests() {
        long testSetExecStart = System.currentTimeMillis();

        LightningXMLFileReader xmlFileReader = new LightningXMLFileReader();
        xmlFileReader.readTests(testSetXml);

        // TODO: here and in other modules - make sure we don't pass nulls
        List<ClientSideTest> clientSideTests = xmlFileReader.getClientSideTests();
        List<ServerSideTest> serverSideTests = xmlFileReader.getServerSideTests();

        testSet = TestSet.fromClientAndServerSideTest(clientSideTests, serverSideTests);

        jmeterTransactions = JMeterTransactions.fromFile(jmeterCsv);

        if (perfmonCsv != null) {
            PerfMonEntries perfMonDataEntries = PerfMonEntries.fromFile(perfmonCsv);
            testSet.executeServerSideTests(perfMonDataEntries);
        }

        testSet.executeClientSideTests(jmeterTransactions);
        log(testSet.getTestExecutionReport());

        log(TestSetReporter.getTestSetExecutionSummaryReport(testSet));

        long testSetExecEnd = System.currentTimeMillis();
        long testExecTime = testSetExecEnd - testSetExecStart;
        log(String.format("Execution time:    %dms", testExecTime));

        if (testSet.getFailCount() + testSet.getErrorCount() != 0) {
            exitCode = 1;
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

    private void log(String text) {
        for (String line : Arrays.asList(text.split(System.lineSeparator()))) {
            getLog().info(line);
        }
    }
}