package uk.co.automatictester.lightning.core.facade;

import org.apache.commons.io.FileUtils;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class LightningCoreS3FacadeIT extends FileAndOutputComparisonIT {

    private LightningCoreS3Facade core;

    private String region = "eu-west-2";
    private String bucket = "automatictester.co.uk-lightning-aws-lambda";

    private String mode;
    private String lightningXml;
    private String jmeterCsv;
    private String perfMonCsv;

    private int expectedExitCode;
    private String expectedLogEntries;
    private String expectedJunitReport;

    private int responseExitCode;
    private String responseVerifyLogKey;
    private String responseReportLogKey;
    private String responseTeamCityLogKey;
    private String responseJenkinsReportKey;
    private String responseJunitReportKey;

    private TestS3Client s3Client = new TestS3Client(region, bucket);

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
                {"report", null, "csv/jmeter/10_transactions.csv", null, 0, null, "/results/expected/report.txt"},
                {"report", null, "csv/jmeter/2_transactions.csv", null, 0, null, null},
                {"report", null, "csv/jmeter/2_transactions_1_failed.csv", null, 1, null, null},
                {"verify", "xml/1_1_1.xml", "csv/jmeter/10_transactions.csv", null, 1, null, "/results/expected/1_1_1.txt"},
                {"verify", "xml/3_0_0.xml", "csv/jmeter/10_transactions.csv", null, 0, null, "/results/expected/3_0_0.txt"},
                {"verify", "xml/1_client_2_server.xml", "csv/jmeter/10_transactions.csv", "csv/perfmon/2_entries.csv", 0, null, "/results/expected/1_client_2_server.txt"},
                {"verify", "xml/junit_report.xml", "csv/jmeter/2_transactions.csv", "csv/perfmon/junit_report.csv", 1, "/results/expected/junit/junit_expected.xml", null}
        };
    }

    @Test(dataProvider = "testData")
    public void s3facadeIT(String mode, String lightningXml, String jmeterCsv, String perfMonCsv, int expectedExitCode, String expectedJunitReport, String expectedLogEntries) throws IOException {
        this.responseExitCode = 0;
        this.mode = mode;
        this.lightningXml = lightningXml;
        this.jmeterCsv = jmeterCsv;
        this.perfMonCsv = perfMonCsv;
        this.expectedExitCode = expectedExitCode;
        this.expectedJunitReport = expectedJunitReport;
        this.expectedLogEntries = expectedLogEntries;

        saveTestDataToS3();
        run();
        assertExitCode();
        assertLogEntries();
        assertJunitReport();
        assertJenkinsReport();
    }

    private void saveTestDataToS3() throws IOException {
        if (lightningXml != null) {
            s3Client.putS3Object(lightningXml);
        }
        if (jmeterCsv != null) {
            s3Client.putS3Object(jmeterCsv);
        }
        if (perfMonCsv != null) {
            s3Client.putS3Object(perfMonCsv);
        }
    }

    private void run() {
        core = new LightningCoreS3Facade();

        core.setRegionAndBucket(region, bucket);
        core.setLightningXml(lightningXml);
        core.setJmeterCsv(jmeterCsv);
        core.setPerfMonCsv(perfMonCsv);

        core.loadTestDataFromS3();

        if (mode.equals("verify")) {
            runTests();
            responseJunitReportKey = core.saveJunitReportToS3();
        } else if (mode.equals("report")) {
            runReport();
        }
        notifyCIServer();
    }

    private void runTests() {
        long testSetExecStart = System.currentTimeMillis();

        core.setLightningXml(lightningXml);
        core.loadConfigFromS3();

        String testExecutionReport = core.executeTests();
        String testSetExecutionSummaryReport = core.getTestSetExecutionSummaryReport();

        String combinedTestReport = String.format("\n%s%s\n", testExecutionReport, testSetExecutionSummaryReport);
        log(combinedTestReport);
        responseVerifyLogKey = core.putS3Object("output/verify.log", combinedTestReport);

        long testSetExecEnd = System.currentTimeMillis();
        long testExecTime = testSetExecEnd - testSetExecStart;
        String message = String.format("Execution time:    %dms", testExecTime);
        log(message);

        if (core.hasExecutionFailed()) {
            responseExitCode = 1;
        }
    }

    private void runReport() {
        String report = core.runReport();
        log(report);

        responseReportLogKey = core.putS3Object("output/report.log", report);

        if (core.hasFailedTransactions()) {
            responseExitCode = 1;
        }
    }

    private void notifyCIServer() {
        if (mode.equals("verify")) {
            String teamCityReport = core.getTeamCityVerifyStatistics();
            log(teamCityReport);
            responseTeamCityLogKey = core.putS3Object("output/teamcity.log", teamCityReport);

            responseJenkinsReportKey = core.storeJenkinsBuildNameForVerifyInS3();

        } else if (mode.equals("report")) {
            String teamCityBuildStatusText = core.getTeamCityBuildReportSummary();
            String teamCityReportStatistics = core.getTeamCityReportStatistics();
            String combinedTeamCityReport = String.format("\n%s\n%s", teamCityBuildStatusText, teamCityReportStatistics);
            log(combinedTeamCityReport);
            responseTeamCityLogKey = core.putS3Object("output/teamcity.log", combinedTeamCityReport);

            responseJenkinsReportKey = core.storeJenkinsBuildNameForReportInS3();
        }
    }

    private void log(String text) {
        for (String line : text.split(System.lineSeparator())) {
            System.out.println(line);
        }
    }

    private void assertExitCode() {
        assertThat(responseExitCode, is(equalTo((expectedExitCode))));
    }

    private void assertLogEntries() throws IOException {
        if (expectedLogEntries != null) {
            String combinedS3Objects = "";
            if (mode.equals("report")) {
                combinedS3Objects += s3Client.getS3ObjectContent(responseReportLogKey);
            } else {
                combinedS3Objects += s3Client.getS3ObjectContent(responseVerifyLogKey);
            }
            combinedS3Objects += s3Client.getS3ObjectContent(responseTeamCityLogKey);

            assertThat(combinedS3Objects, is(equalToIgnoringWhiteSpace(readFileToString(expectedLogEntries))));
        }
    }

    private void assertJunitReport() throws IOException {
        if (expectedJunitReport != null) {
            assertThat(readFileToString(expectedJunitReport), is(equalTo(readS3ObjectToString(responseJunitReportKey))));
        }
    }

    private void assertJenkinsReport() {
        assertThat(responseJenkinsReportKey, notNullValue());
    }

    private String readFileToString(String filePath) throws IOException {
        File file = new File(this.getClass().getResource(filePath).getFile());
        return FileUtils.readFileToString(file);
    }

    private String readS3ObjectToString(String key) {
        return s3Client.getS3ObjectContent(key);
    }
}
