package uk.co.automatictester.lightning;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.automatictester.lightning.data.JMeterTransactions;
import uk.co.automatictester.lightning.data.PerfMonEntries;
import uk.co.automatictester.lightning.enums.TestResult;
import uk.co.automatictester.lightning.tests.ClientSideTest;
import uk.co.automatictester.lightning.tests.LightningTest;
import uk.co.automatictester.lightning.tests.ServerSideTest;

import java.util.ArrayList;
import java.util.List;

public class TestSet {

    private List<ClientSideTest> clientSideTests = new ArrayList<>();
    private List<ServerSideTest> serverSideTests = new ArrayList<>();
    private int passCount = 0;
    private int failCount = 0;
    private int ignoreCount = 0;
    private String testExecutionReport = "";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private TestSet(List<ClientSideTest> clientSideTests) {
        this.clientSideTests = clientSideTests;
    }

    private TestSet(List<ClientSideTest> clientSideTests, List<ServerSideTest> serverSideTests) {
        this.clientSideTests = clientSideTests;
        this.serverSideTests = serverSideTests;
    }

    public static TestSet fromClientSideTest(List<ClientSideTest> clientSideTests) {
        return new TestSet(clientSideTests);
    }

    public static TestSet fromClientAndServerSideTest(List<ClientSideTest> clientSideTests, List<ServerSideTest> serverSideTests) {
        return new TestSet(clientSideTests, serverSideTests);
    }

    public void executeClientSideTests(JMeterTransactions jmeterTransactions) {
        StringBuilder output = new StringBuilder();
        for (ClientSideTest test : clientSideTests) {
            test.execute(jmeterTransactions);
            setCounts(test);
            String testExecutionReport = test.getTestExecutionReport();
            output.append(testExecutionReport).append(System.lineSeparator());
        }
        testExecutionReport += output;
    }

    public void executeServerSideTests(PerfMonEntries perfMonEntries) {
        StringBuilder output = new StringBuilder();
        for (ServerSideTest test : serverSideTests) {
            test.execute(perfMonEntries);
            setCounts(test);
            String testExecutionReport = test.getTestExecutionReport();
            output.append(testExecutionReport).append(System.lineSeparator());
        }
        testExecutionReport += output;
    }

    private void setCounts(LightningTest test) {
        TestResult testResult = test.getResult();
        switch (testResult) {
            case PASS:
                passCount++;
                break;
            case FAIL:
                failCount++;
                break;
            case ERROR:
                ignoreCount++;
                break;
        }
    }

    public void printTestExecutionReport() {
        String[] testExecutionReport = getTestExecutionReport().split(System.lineSeparator());
        for (String line : testExecutionReport) {
            logger.info(line);
        }
    }

    public String getTestExecutionReport() {
        return testExecutionReport;
    }

    public int getTestCount() {
        return getAllTests().size();
    }

    public int getPassCount() {
        return passCount;
    }

    public int getFailCount() {
        return failCount;
    }

    public int getErrorCount() {
        return ignoreCount;
    }

    public List<ClientSideTest> getClientSideTests() {
        return clientSideTests;
    }

    public List<ServerSideTest> getServerSideTests() {
        return serverSideTests;
    }

    public List<LightningTest> getAllTests() {
        List<LightningTest> tests = new ArrayList<>();
        tests.addAll(clientSideTests);
        tests.addAll(serverSideTests);
        return tests;
    }
}
