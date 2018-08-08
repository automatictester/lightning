package uk.co.automatictester.lightning.gradle.task;

import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import uk.co.automatictester.lightning.TestSet;
import uk.co.automatictester.lightning.ci.JUnitReporter;
import uk.co.automatictester.lightning.data.JMeterTransactions;
import uk.co.automatictester.lightning.data.PerfMonEntries;
import uk.co.automatictester.lightning.gradle.extension.LightningExtension;
import uk.co.automatictester.lightning.readers.LightningXMLFileReader;
import uk.co.automatictester.lightning.reporters.JMeterReporter;
import uk.co.automatictester.lightning.reporters.TestSetReporter;
import uk.co.automatictester.lightning.tests.ClientSideTest;
import uk.co.automatictester.lightning.tests.ServerSideTest;

import java.util.Arrays;
import java.util.List;

abstract class LightningTask extends DefaultTask {

    private int exitCode = 0;
    protected TestSet testSet;
    protected JMeterTransactions jmeterTransactions;
    protected LightningExtension extension;

    LightningTask() {
        extension = getProject().getExtensions().findByType(LightningExtension.class);
        if (extension == null) {
            extension = new LightningExtension();
        }
    }

    void setExitCode() {
        if (exitCode != 0) {
            throw new GradleException("Task failed");
        }
    }

    void saveJunitReport() {
        JUnitReporter junitreporter = new JUnitReporter();
        junitreporter.generateJUnitReport(testSet);
    }

    void runTests() {
        long testSetExecStart = System.currentTimeMillis();

        LightningXMLFileReader xmlFileReader = new LightningXMLFileReader();
        xmlFileReader.readTests(extension.getTestSetXml());

        List<ClientSideTest> clientSideTests = xmlFileReader.getClientSideTests();
        List<ServerSideTest> serverSideTests = xmlFileReader.getServerSideTests();

        testSet = TestSet.fromClientAndServerSideTest(clientSideTests, serverSideTests);

        jmeterTransactions = JMeterTransactions.fromFile(extension.getJmeterCsv());

        if (extension.getPerfmonCsv() != null) {
            PerfMonEntries perfMonDataEntries = PerfMonEntries.fromFile(extension.getPerfmonCsv());
            testSet.executeServerSideTests(perfMonDataEntries);
        }

        testSet.executeClientSideTests(jmeterTransactions);
        log(testSet.getTestExecutionReport());

        log(TestSetReporter.getTestSetExecutionSummaryReport(testSet));

        long testSetExecEnd = System.currentTimeMillis();
        long testExecTime = testSetExecEnd - testSetExecStart;
        log(String.format("Execution time:    %dms", testExecTime));

        if (testSet.getFailCount() + testSet.getErrorCount() != 0) {
            exitCode = 1;
        }
    }

    void runReport() {
        jmeterTransactions = JMeterTransactions.fromFile(extension.getJmeterCsv());
        log(JMeterReporter.getJMeterReport(jmeterTransactions));
        if (jmeterTransactions.getFailCount() != 0) {
            exitCode = 1;
        }
    }

    void log(String text) {
        for (String line : Arrays.asList(text.split(System.lineSeparator()))) {
            System.out.println(line);
        }
    }
}
