package uk.co.automatictester;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import uk.co.automatictester.lightning.TestSet;
import uk.co.automatictester.lightning.ci.JUnitReporter;
import uk.co.automatictester.lightning.ci.JenkinsReporter;
import uk.co.automatictester.lightning.ci.TeamCityReporter;
import uk.co.automatictester.lightning.data.JMeterTransactions;
import uk.co.automatictester.lightning.data.PerfMonDataEntries;
import uk.co.automatictester.lightning.readers.JMeterCSVFileReader;
import uk.co.automatictester.lightning.readers.LightningXMLFileReader;
import uk.co.automatictester.lightning.readers.PerfMonDataReader;
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
        JUnitReporter jUnitReporter = new JUnitReporter();
        jUnitReporter.setTestSet(testSet);
        jUnitReporter.generateJUnitReport();
    }

    private void runTests() {
        long testSetExecStart = System.currentTimeMillis();

        LightningXMLFileReader xmlFileReader = new LightningXMLFileReader();
        xmlFileReader.readTests(testSetXml);

        List<ClientSideTest> clientSideTests = xmlFileReader.getClientSideTests();
        List<ServerSideTest> serverSideTests = xmlFileReader.getServerSideTests();

        testSet = new TestSet(clientSideTests, serverSideTests);

        jmeterTransactions = new JMeterCSVFileReader().getTransactions(jmeterCsv);

        if (perfmonCsv != null) {
            PerfMonDataEntries perfMonDataEntries = new PerfMonDataReader().getDataEntires(perfmonCsv);
            testSet.executeServerSideTests(perfMonDataEntries);
        }

        testSet.executeClientSideTests(jmeterTransactions);
        log(testSet.getTestExecutionReport());

        log(new TestSetReporter(testSet).getTestSetExecutionSummaryReport());

        long testSetExecEnd = System.currentTimeMillis();
        long testExecTime = testSetExecEnd - testSetExecStart;
        log(String.format("Execution time:    %dms", testExecTime));

        if (testSet.getFailCount() + testSet.getErrorCount() != 0) {
            exitCode = 1;
        }
    }

    private void runReport() {
        jmeterTransactions = new JMeterCSVFileReader().getTransactions(jmeterCsv);
        JMeterReporter reporter = new JMeterReporter(jmeterTransactions);
        log(reporter.getJMeterReport());
        if (jmeterTransactions.getFailCount() != 0) {
            exitCode = 1;
        }
    }

    private void notifyCIServer() {
        switch (mode) {
            case verify:
                log(new TeamCityReporter(testSet).getTeamCityVerifyStatistics());
                new JenkinsReporter(testSet).setJenkinsBuildName();
                break;
            case report:
                TeamCityReporter teamCityReporter = new TeamCityReporter(jmeterTransactions);
                log(teamCityReporter.getTeamCityBuildStatusText());
                log(teamCityReporter.getTeamCityReportStatistics());
                new JenkinsReporter(jmeterTransactions).setJenkinsBuildName();
                break;
        }
    }

    private void log(String text) {
        for (String line : Arrays.asList(text.split(System.lineSeparator()))) {
            getLog().info(line);
        }
    }
}