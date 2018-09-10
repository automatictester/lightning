package uk.co.automatictester.lightning.standalone.runners;

import com.beust.jcommander.MissingCommandException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.automatictester.lightning.core.enums.Mode;
import uk.co.automatictester.lightning.core.facades.LightningCoreLocalFacade;
import uk.co.automatictester.lightning.standalone.cli.CommandLineInterface;

import java.io.File;

import static uk.co.automatictester.lightning.core.enums.Mode.valueOf;

public class CliTestRunner {

    private static LightningCoreLocalFacade core = new LightningCoreLocalFacade();
    private static int exitCode = 0;
    private static CommandLineInterface params;
    private static Mode mode;
    private static Logger log = LoggerFactory.getLogger(CliTestRunner.class);

    public static void main(String[] args) {
        parseParams(args);

        if (params.isHelpRequested() || (params.getParsedCommand() == null)) {
            params.printHelp();
            return;
        }

        mode = valueOf(params.getParsedCommand().toLowerCase());
        File jmeterCsv = null;
        switch (mode) {
            case verify:
                jmeterCsv = params.verify.getJmeterCsvFile();
                if (params.verify.isPerfmonCsvFileProvided()) {
                    File perfmonCsv = params.verify.getPerfmonCsvFile();
                    core.setPerfMonCsv(perfmonCsv);
                }
                break;
            case report:
                jmeterCsv = params.report.getJmeterCsvFile();
                break;
        }
        core.setJmeterCsv(jmeterCsv);
        core.loadTestData();

        switch (mode) {
            case verify:
                runTests();
                core.saveJunitReport();
                notifyCiServerForVerify();
                break;
            case report:
                runReport();
                notifyCiServerForReport();
                break;
        }

        setExitCode();
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

        File testSetXml = params.verify.getXmlFile();
        core.setLightningXml(testSetXml);
        core.loadConfig();

        String testExecutionReport = core.executeTests();
        log(testExecutionReport);

        String testSetExecutionSummaryReport = core.testSetExecutionSummaryReport();
        log(testSetExecutionSummaryReport);

        long testSetExecEnd = System.currentTimeMillis();
        long testExecTime = testSetExecEnd - testSetExecStart;
        String message = String.format("Execution time:    %dms", testExecTime);
        log(message);

        if (core.hasExecutionFailed()) {
            exitCode = 1;
        }
    }

    private static void runReport() {
        String report = core.runReport();
        log(report);
        if (core.hasFailedTransactions()) {
            exitCode = 1;
        }
    }

    private static void notifyCiServerForVerify() {
        String teamCityVerifyStatistics = core.teamCityVerifyStatistics();
        log(teamCityVerifyStatistics);
        core.setJenkinsBuildNameForVerify();
    }

    private static void notifyCiServerForReport() {
        String teamCityBuildReportSummary = core.teamCityBuildReportSummary();
        log(teamCityBuildReportSummary);
        String teamCityReportStatistics = core.teamCityReportStatistics();
        log(teamCityReportStatistics);
        core.setJenkinsBuildNameForReport();
    }

    private static void setExitCode() {
        System.exit(exitCode);
    }

    private static void setExitCode(int code) {
        System.exit(code);
    }

    private static void log(String text) {
        for (String line : text.split(System.lineSeparator())) {
            log.info(line);
        }
    }
}
