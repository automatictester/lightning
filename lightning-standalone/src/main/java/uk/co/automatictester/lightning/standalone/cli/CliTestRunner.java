package uk.co.automatictester.lightning.standalone.cli;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;
import uk.co.automatictester.lightning.core.enums.Mode;
import uk.co.automatictester.lightning.core.facades.LightningCoreLocalFacade;
import uk.co.automatictester.lightning.standalone.bean.Helper;

import java.io.File;

import static uk.co.automatictester.lightning.core.enums.Mode.valueOf;

@Slf4j
@Component
public class CliTestRunner {

    private final LightningCoreLocalFacade core;
    private final Helper helper;
    private CliParams params;
    private int exitCode = 0;

    public CliTestRunner(
            @Autowired LightningCoreLocalFacade core,
            @Autowired Helper helper) {
        this.core = core;
        this.helper = helper;
    }

    public void main(ApplicationArguments args) {
        params = new CliParams(args);

        if (params.isHelpRequested() || (!params.getParsedCommand().isPresent())) {
            helper.printHelp();
            return;
        }

        Mode mode = valueOf(params.getParsedCommand().get());
        File jmeterCsv = null;
        switch (mode) {
            case verify:
                jmeterCsv = params.getJmeterCsvFile();
                if (params.isPerfmonCsvFileProvided()) {
                    File perfmonCsv = params.getPerfmonCsvFile();
                    core.setPerfMonCsv(perfmonCsv);
                }
                break;
            case report:
                jmeterCsv = params.getJmeterCsvFile();
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

    private void runTests() {
        long testSetExecStart = System.currentTimeMillis();

        File testSetXml = params.getXmlFile();
        core.setLightningXml(testSetXml);
        core.loadConfig();

        String testExecutionReport = core.executeTests();
        log.info(testExecutionReport);

        String testSetExecutionSummaryReport = core.testSetExecutionSummaryReport();
        log.info(testSetExecutionSummaryReport);

        long testSetExecEnd = System.currentTimeMillis();
        long testExecTime = testSetExecEnd - testSetExecStart;
        log.info("Execution time:    {}ms", testExecTime);

        if (core.hasExecutionFailed()) {
            exitCode = 1;
        }
    }

    private void runReport() {
        String report = core.runReport();
        log.info(report);
        if (core.hasFailedTransactions()) {
            exitCode = 1;
        }
    }

    private void notifyCiServerForVerify() {
        String teamCityVerifyStatistics = core.teamCityVerifyStatistics();
        log.info(teamCityVerifyStatistics);
        core.setJenkinsBuildNameForVerify();
    }

    private void notifyCiServerForReport() {
        String teamCityBuildReportSummary = core.teamCityBuildReportSummary();
        log.info(teamCityBuildReportSummary);
        String teamCityReportStatistics = core.teamCityReportStatistics();
        log.info(teamCityReportStatistics);
        core.setJenkinsBuildNameForReport();
    }

    private void setExitCode() {
        if (exitCode != 0) {
            System.exit(exitCode);
        }
    }
}
