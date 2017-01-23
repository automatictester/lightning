package uk.co.deliverymind.lightning.gradle.task;

import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import uk.co.deliverymind.lightning.TestSet;
import uk.co.deliverymind.lightning.ci.JUnitReporter;
import uk.co.deliverymind.lightning.data.JMeterTransactions;
import uk.co.deliverymind.lightning.data.PerfMonDataEntries;
import uk.co.deliverymind.lightning.gradle.extension.LightningExtension;
import uk.co.deliverymind.lightning.readers.JMeterCSVFileReader;
import uk.co.deliverymind.lightning.readers.LightningXMLFileReader;
import uk.co.deliverymind.lightning.readers.PerfMonDataReader;
import uk.co.deliverymind.lightning.reporters.JMeterReporter;
import uk.co.deliverymind.lightning.reporters.TestSetReporter;
import uk.co.deliverymind.lightning.tests.ClientSideTest;
import uk.co.deliverymind.lightning.tests.ServerSideTest;

import java.util.Arrays;
import java.util.List;

abstract class LightningTask extends DefaultTask {

    private int exitCode = 0;
    private TestSet testSet;
    private JMeterTransactions jmeterTransactions;
    private LightningExtension extension;

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
        JUnitReporter jUnitReporter = new JUnitReporter();
        jUnitReporter.setTestSet(testSet);
        jUnitReporter.generateJUnitReport();
    }

    void runTests() {
        long testSetExecStart = System.currentTimeMillis();

        LightningXMLFileReader xmlFileReader = new LightningXMLFileReader();
        xmlFileReader.readTests(getExtension().getTestSetXml());

        List<ClientSideTest> clientSideTests = xmlFileReader.getClientSideTests();
        List<ServerSideTest> serverSideTests = xmlFileReader.getServerSideTests();

        testSet = new TestSet(clientSideTests, serverSideTests);

        jmeterTransactions = new JMeterCSVFileReader().getTransactions(getExtension().getJmeterCsv());

        if (getExtension().getPerfmonCsv() != null) {
            PerfMonDataEntries perfMonDataEntries = new PerfMonDataReader().getDataEntires(getExtension().getPerfmonCsv());
            testSet.executeServerSideTests(perfMonDataEntries);
        }

        testSet.executeClientSideTests(jmeterTransactions);
        log(testSet.getTestExecutionReport());

        log(new TestSetReporter(testSet).getTestSetExecutionSummaryReport());

        long testSetExecEnd = System.currentTimeMillis();
        long testExecTime = testSetExecEnd - testSetExecStart;
        log(String.format("Execution time:    %dms", testExecTime));

        if (testSet.getFailCount() + testSet.getErrorCount() != 0) {
            exitCode = 1;
        }
    }

    void runReport() {
        jmeterTransactions = new JMeterCSVFileReader().getTransactions(getExtension().getJmeterCsv());
        JMeterReporter reporter = new JMeterReporter(jmeterTransactions);
        log(reporter.getJMeterReport());
        if (jmeterTransactions.getFailCount() != 0) {
            exitCode = 1;
        }
    }

    void log(String text) {
        for (String line : Arrays.asList(text.split(System.lineSeparator()))) {
            System.out.println(line);
        }
    }

    LightningExtension getExtension() {
        return extension;
    }

    TestSet getTestSet() {
        return testSet;
    }

    JMeterTransactions getJmeterTransactions() {
        return jmeterTransactions;
    }
}
