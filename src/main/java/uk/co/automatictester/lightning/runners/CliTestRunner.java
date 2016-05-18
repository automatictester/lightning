package uk.co.automatictester.lightning.runners;

import uk.co.automatictester.lightning.TestSet;
import uk.co.automatictester.lightning.ci.JUnitReporter;
import uk.co.automatictester.lightning.ci.JenkinsReporter;
import uk.co.automatictester.lightning.ci.TeamCityReporter;
import uk.co.automatictester.lightning.cli.CommandLineInterface;
import uk.co.automatictester.lightning.data.JMeterTransactions;
import uk.co.automatictester.lightning.data.PerfMonDataEntries;
import uk.co.automatictester.lightning.enums.Mode;
import uk.co.automatictester.lightning.readers.JMeterCSVFileReader;
import uk.co.automatictester.lightning.readers.LightningXMLFileReader;
import uk.co.automatictester.lightning.readers.PerfMonDataReader;
import uk.co.automatictester.lightning.reporters.JMeterReporter;
import uk.co.automatictester.lightning.reporters.TestSetReporter;
import uk.co.automatictester.lightning.tests.ClientSideTest;
import uk.co.automatictester.lightning.tests.ServerSideTest;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.util.List;

public class CliTestRunner {

    private static int exitCode = 0;
    private static CommandLineInterface params;
    private static TestSet testSet;
    private static JMeterTransactions jmeterTransactions;
    private static PerfMonDataEntries perfMonDataEntries;
    private static Mode mode;

    public static void main(String[] args) throws TransformerException, ParserConfigurationException {
        parseParams(args);

        if (params.isHelpRequested() || (params.getParsedCommand() == null)) {
            params.printHelp();
            return;
        }

        mode = Mode.valueOf(params.getParsedCommand().toLowerCase());
        if (mode.toString().equals("verify")) {
            runTests();
            saveJunitReport();
        } else if (mode.toString().equals("report")) {
            runReport();
        }
        notifyCIServer();
        setExitCode();
    }

    private static void saveJunitReport() {
        JUnitReporter jUnitReporter = new JUnitReporter();
        jUnitReporter.setTestSet(testSet);
        jUnitReporter.generateJUnitReport();
    }

    private static void parseParams(String[] args) {
        params = new CommandLineInterface(args);
    }

    private static void runTests() {
        long testSetExecStart = System.currentTimeMillis();

        File xmlFile = params.verify.getXmlFile();
        File jmeterCsvFile = params.verify.getJmeterCsvFile();
        File perfmonCsvFile = params.verify.getPerfmonCsvFile();

        LightningXMLFileReader xmlFileReader = new LightningXMLFileReader();
        xmlFileReader.readTests(xmlFile);

        List<ClientSideTest> clientSideTests = xmlFileReader.getClientSideTests();
        List<ServerSideTest> serverSideTests = xmlFileReader.getServerSideTests();

        testSet = new TestSet(clientSideTests, serverSideTests);

        jmeterTransactions = new JMeterCSVFileReader().getTransactions(jmeterCsvFile);

        if (params.verify.isPerfmonCsvFileProvided()) {
            perfMonDataEntries = new PerfMonDataReader().getDataEntires(perfmonCsvFile);
            testSet.executeServerSideTests(perfMonDataEntries);
        }

        testSet.executeClientSideTests(jmeterTransactions);

        new TestSetReporter(testSet).printTestSetExecutionSummaryReport();

        long testSetExecEnd = System.currentTimeMillis();
        long testExecTime = testSetExecEnd - testSetExecStart;
        System.out.println(String.format("Execution time:    %dms", testExecTime));

        if (testSet.getFailCount() + testSet.getErrorCount() != 0) {
            exitCode = 1;
        }
    }

    private static void runReport() {
        jmeterTransactions = new JMeterCSVFileReader().getTransactions(params.report.getJmeterCsvFile());
        JMeterReporter reporter = new JMeterReporter(jmeterTransactions);
        reporter.printJMeterReport();
        if (jmeterTransactions.getFailCount() != 0) {
            exitCode = 1;
        }
    }

    private static void notifyCIServer() {
        switch (mode) {
            case verify:
                new TeamCityReporter(testSet).printTeamCityVerifyStatistics();
                new JenkinsReporter(testSet).setJenkinsBuildName();
                break;
            case report:
                new TeamCityReporter(jmeterTransactions)
                        .printTeamCityBuildStatusText()
                        .printTeamCityReportStatistics();
                new JenkinsReporter(jmeterTransactions).setJenkinsBuildName();
        }
    }

    private static void setExitCode() {
        System.exit(exitCode);
    }
}
