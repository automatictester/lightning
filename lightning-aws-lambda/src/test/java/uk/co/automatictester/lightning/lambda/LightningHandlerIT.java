package uk.co.automatictester.lightning.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import io.findify.s3mock.S3Mock;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import uk.co.automatictester.lightning.core.s3client.S3Client;
import uk.co.automatictester.lightning.core.s3client.factory.S3ClientFlyweightFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import static org.testng.Assert.assertEquals;

public class LightningHandlerIT {

    private static final String REGION = "eu-west-2";
    private static final String BUCKET = "automatictester.co.uk-lightning-aws-lambda";
    private final S3Client amazonS3 = S3ClientFlyweightFactory.getInstance(REGION);
    private final S3Mock s3Mock = new S3Mock.Builder().withPort(8001).withInMemoryBackend().build();
    private Context context = null;

    @BeforeClass
    public void setupEnv() {
        if (System.getProperty("mockS3") != null) {
            startS3Mock();
            maybeCreateBucket();
        }
        amazonS3.setBucket(BUCKET);
    }

    private void startS3Mock() {
        s3Mock.start();
    }

    private void maybeCreateBucket() {
        amazonS3.createBucketIfDoesNotExist(BUCKET);
    }

    @AfterClass
    public void teardown() {
        if (System.getProperty("mockS3") != null) {
            s3Mock.stop();
        }
    }

    @Test
    public void testReport_10_0() throws IOException {
        String key = "src/test/resources/csv/jmeter/10_transactions.csv";
        amazonS3.putObjectFromFile(key);

        LightningRequest request = new LightningRequest();
        request.setMode("report");
        request.setJmeterCsv(key);
        request.setRegion(REGION);
        request.setBucket(BUCKET);

        LightningHandler handler = new LightningHandler();
        LightningResponse response = handler.handleRequest(request, context);

        int exitCode = response.getExitCode();
        assertEquals(exitCode, 0);

        String jmeterReportKey = response.getJmeterReport();
        String jmeterReportContent = amazonS3.getObjectAsString(jmeterReportKey);
        String teamCityReportKey = response.getTeamCityReport();
        String teamCityReportContent = amazonS3.getObjectAsString(teamCityReportKey);

        String actual = jmeterReportContent + teamCityReportContent;
        String expected = readResourceFileToString("src/test/resources/results/expected/report.txt");

        assertEquals(actual.trim(), expected.trim());
    }

    @Test
    public void testReport_2_1() throws IOException {
        String key = "src/test/resources/csv/jmeter/2_transactions_1_failed.csv";
        amazonS3.putObjectFromFile(key);

        LightningRequest request = new LightningRequest();
        request.setMode("report");
        request.setJmeterCsv(key);
        request.setRegion(REGION);
        request.setBucket(BUCKET);

        LightningHandler handler = new LightningHandler();
        LightningResponse response = handler.handleRequest(request, context);

        int exitCode = response.getExitCode();
        assertEquals(exitCode, 1);

        String jenkinsReportKey = response.getJenkinsReport();
        String jenkinsReportContent = amazonS3.getObjectAsString(jenkinsReportKey);

        String expected = readResourceFileToString("src/test/resources/results/expected/jenkins/2_1_txn.txt");

        assertEquals(jenkinsReportContent.trim(), expected.trim());
    }

    @Test
    public void testVerify_1_1_1() throws IOException {
        String csv = "src/test/resources/csv/jmeter/10_transactions.csv";
        amazonS3.putObjectFromFile(csv);
        String xml = "src/test/resources/xml/1_1_1.xml";
        amazonS3.putObjectFromFile(xml);

        LightningRequest request = new LightningRequest();
        request.setMode("verify");
        request.setXml(xml);
        request.setJmeterCsv(csv);
        request.setRegion(REGION);
        request.setBucket(BUCKET);

        LightningHandler handler = new LightningHandler();
        LightningResponse response = handler.handleRequest(request, context);

        int exitCode = response.getExitCode();
        assertEquals(exitCode, 1);

        String combinedReportKey = response.getCombinedTestReport();
        String jmeterReportContent = amazonS3.getObjectAsString(combinedReportKey);
        String teamCityReportKey = response.getTeamCityReport();
        String teamCityReportContent = amazonS3.getObjectAsString(teamCityReportKey);

        String actual = jmeterReportContent + teamCityReportContent;
        String expected = readResourceFileToString("src/test/resources/results/expected/1_1_1.txt");

        assertEquals(actual.trim(), expected.trim());

        String jenkinsReportKey = response.getJenkinsReport();
        String jenkinsReportContent = amazonS3.getObjectAsString(jenkinsReportKey);

        String expectedJenkinsReport = readResourceFileToString("src/test/resources/results/expected/jenkins/3_2.txt");

        assertEquals(jenkinsReportContent.trim(), expectedJenkinsReport.trim());
    }

    @Test
    public void testVerify_3_0_0() throws IOException {
        String csv = "src/test/resources/csv/jmeter/10_transactions.csv";
        amazonS3.putObjectFromFile(csv);
        String xml = "src/test/resources/xml/3_0_0.xml";
        amazonS3.putObjectFromFile(xml);

        LightningRequest request = new LightningRequest();
        request.setMode("verify");
        request.setXml(xml);
        request.setJmeterCsv(csv);
        request.setRegion(REGION);
        request.setBucket(BUCKET);

        LightningHandler handler = new LightningHandler();
        LightningResponse response = handler.handleRequest(request, context);

        int exitCode = response.getExitCode();
        assertEquals(exitCode, 0);

        String combinedReportKey = response.getCombinedTestReport();
        String jmeterReportContent = amazonS3.getObjectAsString(combinedReportKey);
        String teamCityReportKey = response.getTeamCityReport();
        String teamCityReportContent = amazonS3.getObjectAsString(teamCityReportKey);

        String actual = jmeterReportContent + teamCityReportContent;
        String expected = readResourceFileToString("src/test/resources/results/expected/3_0_0.txt");

        assertEquals(actual.trim(), expected.trim());
    }

    @Test
    public void testVerify_3_0_0_2s() throws IOException {
        String csv = "src/test/resources/csv/jmeter/10_transactions.csv";
        amazonS3.putObjectFromFile(csv);
        String xml = "src/test/resources/xml/1_client_2_server.xml";
        amazonS3.putObjectFromFile(xml);
        String perfMon = "src/test/resources/csv/perfmon/2_entries.csv";
        amazonS3.putObjectFromFile(perfMon);

        LightningRequest request = new LightningRequest();
        request.setMode("verify");
        request.setXml(xml);
        request.setJmeterCsv(csv);
        request.setPerfmonCsv(perfMon);
        request.setRegion(REGION);
        request.setBucket(BUCKET);

        LightningHandler handler = new LightningHandler();
        LightningResponse response = handler.handleRequest(request, context);

        int exitCode = response.getExitCode();
        assertEquals(exitCode, 0);

        String combinedReportKey = response.getCombinedTestReport();
        String jmeterReportContent = amazonS3.getObjectAsString(combinedReportKey);
        String teamCityReportKey = response.getTeamCityReport();
        String teamCityReportContent = amazonS3.getObjectAsString(teamCityReportKey);

        String actual = jmeterReportContent + teamCityReportContent;
        String expected = readResourceFileToString("src/test/resources/results/expected/1_client_2_server.txt");

        assertEquals(actual.trim(), expected.trim());
    }

    @Test
    public void testVerifyJunit() throws IOException {
        String csv = "src/test/resources/csv/jmeter/2_transactions.csv";
        amazonS3.putObjectFromFile(csv);
        String xml = "src/test/resources/xml/junit_report.xml";
        amazonS3.putObjectFromFile(xml);
        String perfMon = "src/test/resources/csv/perfmon/junit_report.csv";
        amazonS3.putObjectFromFile(perfMon);

        LightningRequest request = new LightningRequest();
        request.setMode("verify");
        request.setXml(xml);
        request.setJmeterCsv(csv);
        request.setPerfmonCsv(perfMon);
        request.setRegion(REGION);
        request.setBucket(BUCKET);

        LightningHandler handler = new LightningHandler();
        LightningResponse response = handler.handleRequest(request, context);

        int exitCode = response.getExitCode();
        assertEquals(exitCode, 1);

        String junitReportKey = response.getJunitReport();
        String junitReportContent = amazonS3.getObjectAsString(junitReportKey);

        String expected = readResourceFileToString("src/test/resources/results/expected/junit/junit_expected.xml");

        assertEquals(junitReportContent.trim(), expected.trim());
    }

    private String readResourceFileToString(String file) throws IOException {
        Path path = Paths.get(file);
        return Files.lines(path).collect(Collectors.joining("\n"));
    }
}
