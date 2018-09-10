package uk.co.automatictester.lightning.core.facades;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import uk.co.automatictester.lightning.core.enums.Mode;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class LightningCoreLocalFacadeIT extends FileAndOutputComparisonIT {

    private LightningCoreLocalFacade core = new LightningCoreLocalFacade();
    private Mode mode;
    private File perfMonCsv;
    private File jmeterCsv;
    private File lightningXml;
    private int exitCode;
    private int expectedExitCode;
    private String expectedJunitReport;
    private String expectedConsoleOutput;
    private String expectedJenkinsReport;

    @BeforeMethod
    public void setup() {
        configureStream();
    }

    @AfterMethod
    public void teardown() {
        revertStream();
    }

    @DataProvider(name = "testData")
    private Object[][] testData() {
        return new Object[][]{
                {Mode.report, null, new File("src/test/resources/csv/jmeter/10_transactions.csv"), null, 0, null, "/results/expected/report.txt", "/results/expected/jenkins/10_0_txn.txt"},
                {Mode.report, null, new File("src/test/resources/csv/jmeter/2_transactions.csv"), null, 0, null, null, null},
                {Mode.report, null, new File("src/test/resources/csv/jmeter/2_transactions_1_failed.csv"), null, 1, null, null, "/results/expected/jenkins/2_1_txn.txt"},
                {Mode.verify, new File("src/test/resources/xml/1_1_1.xml"), new File("src/test/resources/csv/jmeter/10_transactions.csv"), null, 1, null, "/results/expected/1_1_1.txt", "/results/expected/jenkins/3_2.txt"},
                {Mode.verify, new File("src/test/resources/xml/3_0_0.xml"), new File("src/test/resources/csv/jmeter/10_transactions.csv"), null, 0, null, "/results/expected/3_0_0.txt", "/results/expected/jenkins/3_0.txt"},
                {Mode.verify, new File("src/test/resources/xml/1_client_2_server.xml"), new File("src/test/resources/csv/jmeter/10_transactions.csv"), new File("src/test/resources/csv/perfmon/2_entries.csv"), 0, null, "/results/expected/1_client_2_server.txt", null},
                {Mode.verify, new File("src/test/resources/xml/junit_report.xml"), new File("src/test/resources/csv/jmeter/2_transactions.csv"), new File("src/test/resources/csv/perfmon/junit_report.csv"), 1, "/results/expected/junit/junit_expected.xml", null, null}
        };
    }

    @Test(dataProvider = "testData")
    public void localFacadeIT(Mode mode, File lightningXml, File jmeterCsv, File perfMonCsv, int expectedExitCode, String expectedJunitReport, String expectedConsoleOutput, String expectedJenkinsReport) throws IOException {
        this.exitCode = 0;
        this.mode = mode;
        this.lightningXml = lightningXml;
        this.jmeterCsv = jmeterCsv;
        this.perfMonCsv = perfMonCsv;
        this.expectedExitCode = expectedExitCode;
        this.expectedJunitReport = expectedJunitReport;
        this.expectedConsoleOutput = expectedConsoleOutput;
        this.expectedJenkinsReport = expectedJenkinsReport;

        run();
        assertExitCode();
        assertConsoleOutput();
        assertJunitReport();
        assertJenkinsReport();
    }

    private void run() {
        core.setJmeterCsv(jmeterCsv);
        core.setPerfMonCsv(perfMonCsv);
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
    }

    private void runTests() {
        long testSetExecStart = System.currentTimeMillis();

        core.setLightningXml(lightningXml);
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

    private void log(String text) {
        for (String line : text.split(System.lineSeparator())) {
            System.out.println(line);
        }
    }

    private void assertExitCode() {
        assertThat(exitCode, is(equalTo((expectedExitCode))));
    }

    private void assertConsoleOutput() throws IOException {
        if (expectedConsoleOutput != null) {
            assertThat(consoleOutputContainsFileContent(expectedConsoleOutput), is(true));
        }
    }

    private void assertJunitReport() throws IOException {
        if (expectedJunitReport != null) {
            assertThat(fileContentIsEqual(expectedJunitReport, "junit.xml"), is(true));
        }
    }

    private void assertJenkinsReport() throws IOException {
        if (expectedJenkinsReport != null) {
            assertThat(fileContentContainsResourceFile(expectedJenkinsReport, "lightning-jenkins.properties"), is(true));
        }
    }

    private boolean fileContentIsEqual(String resourceFilePath, String file) throws IOException {
        String resourceFileContent = readResourceFileToString(resourceFilePath);
        String outputFile = readFileToString(file);
        return resourceFileContent.equals(outputFile);
    }

    private boolean fileContentContainsResourceFile(String resourceFilePath, String file) throws IOException {
        String resourceFileContent = readResourceFileToString(resourceFilePath);
        String outputFile = readFileToString(file);
        return outputFile.contains(resourceFileContent);
    }

    private boolean consoleOutputContainsFileContent(String resourceFilePath) throws IOException {
        String resourceFileContent = readResourceFileToString(resourceFilePath);
        String filteredBuildOutput = out.toString().replaceAll("Execution time:\\s*\\d*[ms]*\\s", "");
        return filteredBuildOutput.contains(resourceFileContent);
    }

    private String readResourceFileToString(String file) throws IOException {
        return readFileToString("src/test/resources/" + file);
    }

    private String readFileToString(String file) throws IOException {
        Path path = Paths.get(file);
        return Files.lines(path).collect(Collectors.joining("\n"));
    }
}
