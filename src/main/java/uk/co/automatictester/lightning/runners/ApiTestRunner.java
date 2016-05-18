package uk.co.automatictester.lightning.runners;

import uk.co.automatictester.lightning.TestSet;
import uk.co.automatictester.lightning.ci.JenkinsReporter;
import uk.co.automatictester.lightning.ci.TeamCityReporter;
import uk.co.automatictester.lightning.data.JMeterTransactions;
import uk.co.automatictester.lightning.data.PerfMonDataEntries;
import uk.co.automatictester.lightning.enums.Mode;
import uk.co.automatictester.lightning.readers.JMeterCSVFileReader;
import uk.co.automatictester.lightning.readers.PerfMonDataReader;
import uk.co.automatictester.lightning.reporters.JMeterReporter;
import uk.co.automatictester.lightning.reporters.TestSetReporter;

import java.io.File;

public class ApiTestRunner {

    private int exitCode = 0;
    private TestSet testSet;
    private JMeterTransactions jmeterTransactions;
    private File jmeterCsvFile;
    private File perfmonCsvFile;
    private Mode mode;

    public ApiTestRunner(File jmeterCsvFile) {
        this.mode = Mode.REPORT;
        this.jmeterCsvFile = jmeterCsvFile;
    }

    public ApiTestRunner(File jmeterCsvFile, File perfmonCsvFile, TestSet testSet) {
        this.mode = Mode.VERIFY;
        this.jmeterCsvFile = jmeterCsvFile;
        this.perfmonCsvFile = perfmonCsvFile;
        this.testSet = testSet;
    }

    public void run() {
        switch (mode) {
            case VERIFY:
                runTests();
                break;

            case REPORT:
                runReport();
                break;
        }
        notifyCIServer();
    }

    public int getExitCode() {
        return exitCode;
    }

    private void runTests() {
        long testSetExecStart = System.currentTimeMillis();

        jmeterTransactions = new JMeterCSVFileReader().getTransactions(jmeterCsvFile);
        testSet.executeClientSideTests(jmeterTransactions);

        if (perfmonCsvFile != null) {
            PerfMonDataEntries perfMonDataEntries = new PerfMonDataReader().getDataEntires(perfmonCsvFile);
            testSet.executeServerSideTests(perfMonDataEntries);
        }

        new TestSetReporter(testSet).printTestSetExecutionSummaryReport();

        long testSetExecEnd = System.currentTimeMillis();
        long testExecTime = testSetExecEnd - testSetExecStart;
        System.out.println(String.format("Execution time:    %dms", testExecTime));

        if (testSet.getFailCount() + testSet.getErrorCount() != 0) {
            exitCode = 1;
        }
    }

    private void runReport() {
        jmeterTransactions = new JMeterCSVFileReader().getTransactions(jmeterCsvFile);
        JMeterReporter reporter = new JMeterReporter(jmeterTransactions);
        reporter.printJMeterReport();
        if (jmeterTransactions.getFailCount() != 0) {
            exitCode = 2;
        }
    }

    private void notifyCIServer() {
        switch (mode) {
            case VERIFY:
                new TeamCityReporter(testSet).printTeamCityVerifyStatistics();
                new JenkinsReporter(testSet).setJenkinsBuildName();
                break;
            case REPORT:
                new TeamCityReporter(jmeterTransactions)
                        .printTeamCityBuildStatusText()
                        .printTeamCityReportStatistics();
                new JenkinsReporter(jmeterTransactions).setJenkinsBuildName();
        }
    }
}
