package uk.co.automatictester.jmeter.lightning.maven.plugin;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import uk.co.automatictester.lightning.core.facades.LightningCoreLocalFacade;

@Mojo(name = "lightning", defaultPhase = LifecyclePhase.POST_INTEGRATION_TEST)
public class LightningMojo extends ConfigurationMojo {

    private LightningCoreLocalFacade core = new LightningCoreLocalFacade();
    private int exitCode = 0;

    @Override
    public void execute() throws MojoExecutionException {
        core.setJmeterCsv(jmeterCsv);
        core.setPerfMonCsv(perfmonCsv);
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

    private void runReport() {
        String report = core.runReport();
        log(report);
        if (core.hasFailedTransactions()) {
            exitCode = 1;
        }
    }

    private void notifyCiServerForVerify() {
        String teamCityVerifyStatistics = core.teamCityVerifyStatistics();
        log(teamCityVerifyStatistics);
        core.setJenkinsBuildNameForVerify();
    }

    private void notifyCiServerForReport() {
        String teamCityBuildReportSummary = core.teamCityBuildReportSummary();
        log(teamCityBuildReportSummary);
        String teamCityReportStatistics = core.teamCityReportStatistics();
        log(teamCityReportStatistics);
        core.setJenkinsBuildNameForReport();
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
