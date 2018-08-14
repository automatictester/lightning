package uk.co.automatictester.lightning.standalone.runners;

import com.beust.jcommander.MissingCommandException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.automatictester.lightning.TestSet;
import uk.co.automatictester.lightning.ci.JUnitReporter;
import uk.co.automatictester.lightning.ci.JenkinsReporter;
import uk.co.automatictester.lightning.ci.TeamCityReporter;
import uk.co.automatictester.lightning.config.LightningConfig;
import uk.co.automatictester.lightning.data.JMeterTransactions;
import uk.co.automatictester.lightning.data.PerfMonEntries;
import uk.co.automatictester.lightning.enums.Mode;
import uk.co.automatictester.lightning.reporters.JMeterReporter;
import uk.co.automatictester.lightning.reporters.TestSetReporter;
import uk.co.automatictester.lightning.standalone.cli.CommandLineInterface;
import uk.co.automatictester.lightning.tests.ClientSideTest;
import uk.co.automatictester.lightning.tests.ServerSideTest;

import java.io.File;
import java.util.List;

import static uk.co.automatictester.lightning.enums.Mode.valueOf;

public class CliTestRunner {

    private static int exitCode = 0;
    private static CommandLineInterface params;
    private static TestSet testSet;
    private static JMeterTransactions jmeterTransactions;
    private static PerfMonEntries perfMonEntries;
    private static Mode mode;

    private static final Logger logger = LoggerFactory.getLogger(CliTestRunner.class);

    public static void main(String[] args) {
        parseParams(args);

        if (params.isHelpRequested() || (params.getParsedCommand() == null)) {
            params.printHelp();
            return;
        }

        mode = valueOf(params.getParsedCommand().toLowerCase());
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

    private static void saveJunitReport() {
        JUnitReporter junitreporter = new JUnitReporter();
        junitreporter.generateJUnitReport(testSet);
    }

    private static void parseParams(String[] args) {
        try {
            params = new CommandLineInterface(args);
        } catch (MissingCommandException e) {
            logger.error("Invalid command, should be one of these: report, verify");
            setExitCode(1);
        }
    }

    private static void runTests() {
        long testSetExecStart = System.currentTimeMillis();

        File xmlFile = params.verify.getXmlFile();
        File jmeterCsvFile = params.verify.getJmeterCsvFile();

        LightningConfig lightningConfig = new LightningConfig();
        lightningConfig.readTests(xmlFile);
        populateTestSet(lightningConfig);

        jmeterTransactions = JMeterTransactions.fromFile(jmeterCsvFile);
        executeServerSideTestsIfPerfMonDataProvided();
        testSet.executeClientSideTests(jmeterTransactions);
        testSet.printTestExecutionReport();

        TestSetReporter.printTestSetExecutionSummaryReport(testSet);

        long testSetExecEnd = System.currentTimeMillis();
        long testExecTime = testSetExecEnd - testSetExecStart;
        logger.info("Total execution time:    {}ms", testExecTime);

        if (testSet.getFailCount() + testSet.getErrorCount() != 0) {
            exitCode = 1;
        }
    }

    private static void populateTestSet(LightningConfig lightningConfig) {
        List<ClientSideTest> clientSideTests = lightningConfig.getClientSideTests();
        List<ServerSideTest> serverSideTests = lightningConfig.getServerSideTests();
        if (serverSideTests.size() == 0) {
            testSet = TestSet.fromClientSideTest(clientSideTests);
        } else {
            testSet = TestSet.fromClientAndServerSideTest(clientSideTests, serverSideTests);
        }
    }

    private static void executeServerSideTestsIfPerfMonDataProvided() {
        if (params.verify.isPerfmonCsvFileProvided()) {
            File perfmonCsvFile = params.verify.getPerfmonCsvFile();
            perfMonEntries = PerfMonEntries.fromFile(perfmonCsvFile);
            testSet.executeServerSideTests(perfMonEntries);
        }
    }

    private static void runReport() {
        jmeterTransactions = JMeterTransactions.fromFile(params.report.getJmeterCsvFile());
        JMeterReporter.printJMeterReport(jmeterTransactions);
        if (jmeterTransactions.getFailCount() != 0) {
            exitCode = 1;
        }
    }

    private static void notifyCIServer() {
        switch (mode) {
            case verify:
                TeamCityReporter.fromTestSet(testSet).printTeamCityVerifyStatistics();
                JenkinsReporter.fromTestSet(testSet).setJenkinsBuildName();
                break;
            case report:
                TeamCityReporter.fromJMeterTransactions(jmeterTransactions)
                        .printTeamCityBuildReportSummary()
                        .printTeamCityReportStatistics();
                JenkinsReporter.fromJMeterTransactions(jmeterTransactions).setJenkinsBuildName();
        }
    }

    private static void setExitCode() {
        System.exit(exitCode);
    }

    private static void setExitCode(int code) {
        System.exit(code);
    }
}
