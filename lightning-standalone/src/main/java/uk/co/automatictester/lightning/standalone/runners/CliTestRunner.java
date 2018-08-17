package uk.co.automatictester.lightning.standalone.runners;

import com.beust.jcommander.MissingCommandException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.automatictester.lightning.core.state.TestSet;
import uk.co.automatictester.lightning.core.ci.JUnitReporter;
import uk.co.automatictester.lightning.core.ci.JenkinsReporter;
import uk.co.automatictester.lightning.core.ci.TeamCityReporter;
import uk.co.automatictester.lightning.core.config.LightningConfig;
import uk.co.automatictester.lightning.core.data.JMeterTransactions;
import uk.co.automatictester.lightning.core.data.PerfMonEntries;
import uk.co.automatictester.lightning.core.enums.Mode;
import uk.co.automatictester.lightning.core.reporters.JMeterReporter;
import uk.co.automatictester.lightning.core.reporters.TestSetReporter;
import uk.co.automatictester.lightning.standalone.cli.CommandLineInterface;
import uk.co.automatictester.lightning.core.structures.TestData;

import java.io.File;

import static uk.co.automatictester.lightning.core.enums.Mode.valueOf;

public class CliTestRunner {

    private static int exitCode = 0;
    private static CommandLineInterface params;
    private static TestSet testSet = new TestSet();
    private static JMeterTransactions jmeterTransactions;
    private static PerfMonEntries perfMonEntries;
    private static Mode mode;

    private static final Logger log = LoggerFactory.getLogger(CliTestRunner.class);

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
            log.error("Invalid command, should be one of these: report, verify");
            setExitCode(1);
        }
    }

    private static void runTests() {
        long testSetExecStart = System.currentTimeMillis();

        File xmlFile = params.verify.getXmlFile();
        File jmeterCsvFile = params.verify.getJmeterCsvFile();

        LightningConfig lightningConfig = new LightningConfig();
        lightningConfig.readTests(xmlFile);

        jmeterTransactions = JMeterTransactions.fromFile(jmeterCsvFile);
        TestData.addClientSideTestData(jmeterTransactions);
        loadPerfMonDataIfProvided();
        testSet.executeTests();
        testSet.printTestExecutionReport();

        TestSetReporter.printTestSetExecutionSummaryReport(testSet);

        long testSetExecEnd = System.currentTimeMillis();
        long testExecTime = testSetExecEnd - testSetExecStart;
        log.info("Total execution time:    {}ms", testExecTime);

        if (testSet.getFailCount() + testSet.getErrorCount() != 0) {
            exitCode = 1;
        }
    }

    private static void loadPerfMonDataIfProvided() {
        if (params.verify.isPerfmonCsvFileProvided()) {
            File perfmonCsvFile = params.verify.getPerfmonCsvFile();
            perfMonEntries = PerfMonEntries.fromFile(perfmonCsvFile);
            TestData.addServerSideTestData(perfMonEntries);
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
                break;
        }
    }

    private static void setExitCode() {
        System.exit(exitCode);
    }

    private static void setExitCode(int code) {
        System.exit(code);
    }
}
