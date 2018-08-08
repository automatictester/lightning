package uk.co.automatictester.lightning.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.co.automatictester.lightning.TestSet;
import uk.co.automatictester.lightning.ci.TeamCityReporter;
import uk.co.automatictester.lightning.data.JMeterTransactions;
import uk.co.automatictester.lightning.data.PerfMonEntries;
import uk.co.automatictester.lightning.lambda.ci.JUnitS3Reporter;
import uk.co.automatictester.lightning.lambda.ci.JenkinsS3Reporter;
import uk.co.automatictester.lightning.lambda.readers.LightningXMLS3ObjectReader;
import uk.co.automatictester.lightning.reporters.JMeterReporter;
import uk.co.automatictester.lightning.reporters.TestSetReporter;
import uk.co.automatictester.lightning.s3.S3Client;
import uk.co.automatictester.lightning.tests.ClientSideTest;
import uk.co.automatictester.lightning.tests.ServerSideTest;

import java.util.List;

public class LightningHandler implements RequestHandler<LightningRequest, LightningResponse> {

    private static final Logger log = LogManager.getLogger(LightningHandler.class);
    private static S3Client s3Client;

    private JMeterTransactions jmeterTransactions;
    private TestSet testSet;

    private String bucket;
    private String region;
    private String mode;
    private String xml;
    private String jmeterCsv;
    private String perfmonCsv;

    private LightningResponse response = new LightningResponse();

    public LightningResponse handleRequest(LightningRequest lightningRequest, Context context) {
        parseRequestParams(lightningRequest);
        s3Client = new S3Client(region, bucket);

        if (mode.equals("verify")) {
            runTests();

            String junitReportS3Path = saveJunitReportToS3();
            response.setJunitReport(junitReportS3Path);
        } else if (mode.equals("report")) {
            runReport();
        }
        notifyCIServer();

        return response;
    }

    private void parseRequestParams(LightningRequest request) {
        LightningRequestValidator.validate(request);

        bucket = request.getBucket();
        region = request.getRegion();
        mode = request.getMode();
        xml = request.getXml();
        jmeterCsv = request.getJmeterCsv();
        perfmonCsv = request.getPerfmonCsv();
    }

    private void notifyCIServer() {
        if (mode.equals("verify")) {
            String teamCityReport = TeamCityReporter.fromTestSet(testSet).getTeamCityVerifyStatistics();
            String teamCityReportS3Path = s3Client.putS3Object("output/teamcity.log", teamCityReport);

            log.info(teamCityReport);
            response.setTeamCityReport(teamCityReportS3Path);

            String jenkinsReportS3Path = JenkinsS3Reporter.fromTestSet(region, bucket, testSet).storeJenkinsBuildNameInS3();
            response.setJenkinsReport(jenkinsReportS3Path);

        } else if (mode.equals("report")) {
            TeamCityReporter teamCityReporter = TeamCityReporter.fromJMeterTransactions(jmeterTransactions);
            String teamCityBuildStatusText = teamCityReporter.getTeamCityBuildReportSummary();
            String teamCityReportStatistics = teamCityReporter.getTeamCityReportStatistics();
            String combinedTeamCityReport = String.format("\n%s\n%s", teamCityBuildStatusText, teamCityReportStatistics);
            String combinedTeamCityReportS3Path = s3Client.putS3Object("output/teamcity.log", combinedTeamCityReport);

            log.info(combinedTeamCityReport);
            response.setTeamCityReport(combinedTeamCityReportS3Path);

            String jenkinsReportS3Path = JenkinsS3Reporter.fromJMeterTransactions(region, bucket, jmeterTransactions).storeJenkinsBuildNameInS3();
            response.setJenkinsReport(jenkinsReportS3Path);
        }
    }

    private String saveJunitReportToS3() {
        JUnitS3Reporter junitS3Reporter = new JUnitS3Reporter(region, bucket);
        return junitS3Reporter.generateJUnitReportToS3(testSet);
    }

    private void runTests() {
        long testSetExecStart = System.currentTimeMillis();

        LightningXMLS3ObjectReader xmlFileReader = new LightningXMLS3ObjectReader(region, bucket);
        xmlFileReader.readTests(xml);

        List<ClientSideTest> clientSideTests = xmlFileReader.getClientSideTests();
        List<ServerSideTest> serverSideTests = xmlFileReader.getServerSideTests();

        testSet = TestSet.fromClientAndServerSideTest(clientSideTests, serverSideTests);

        jmeterTransactions = JMeterTransactions.fromS3Object(region, bucket, jmeterCsv);

        if (perfmonCsv != null) {
            PerfMonEntries perfMonEntries = PerfMonEntries.fromS3Object(region, bucket, perfmonCsv);
            testSet.executeServerSideTests(perfMonEntries);
        }

        testSet.executeClientSideTests(jmeterTransactions);

        String testExecutionReport = testSet.getTestExecutionReport();
        String testSetExecutionSummaryReport = TestSetReporter.getTestSetExecutionSummaryReport(testSet);

        String combinedTestReport = String.format("\n%s%s\n", testExecutionReport, testSetExecutionSummaryReport);
        String combinedTestReportS3Path = s3Client.putS3Object("output/verify.log", combinedTestReport);

        response.setCombinedTestReport(combinedTestReportS3Path);
        log.info(combinedTestReport);

        long testSetExecEnd = System.currentTimeMillis();
        long testExecTime = testSetExecEnd - testSetExecStart;
        log.info("Total verify stage execution time:    {}ms", testExecTime);

        if (testSet.getFailCount() + testSet.getErrorCount() != 0) {
            response.setExitCode(1);
        } else {
            response.setExitCode(0);
        }
    }

    private void runReport() {
        jmeterTransactions = JMeterTransactions.fromS3Object(region, bucket, jmeterCsv);
        String jmeterReport = JMeterReporter.getJMeterReport(jmeterTransactions);
        String jmeterReportS3Path = s3Client.putS3Object("output/report.log", jmeterReport);

        response.setJmeterReport(jmeterReportS3Path);
        log.info(jmeterReport);

        if (jmeterTransactions.getFailCount() != 0) {
            response.setExitCode(1);
        } else {
            response.setExitCode(0);
        }
    }
}
