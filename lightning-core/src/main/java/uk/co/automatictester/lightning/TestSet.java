package uk.co.automatictester.lightning;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.automatictester.lightning.data.JMeterTransactions;
import uk.co.automatictester.lightning.data.PerfMonDataEntries;
import uk.co.automatictester.lightning.enums.TestResult;
import uk.co.automatictester.lightning.tests.ClientSideTest;
import uk.co.automatictester.lightning.tests.LightningTest;
import uk.co.automatictester.lightning.tests.ServerSideTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestSet {

    private List<ClientSideTest> clientSideTests = new ArrayList<>();
    private List<ServerSideTest> serverSideTests = new ArrayList<>();
    private int passCount = 0;
    private int failCount = 0;
    private int ignoreCount = 0;
    private String testExecutionReport = "";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public TestSet(List<ClientSideTest> clientSideTests, List<ServerSideTest> serverSideTests) {
        this.clientSideTests = clientSideTests;
        this.serverSideTests = serverSideTests;
    }

    public void executeClientSideTests(JMeterTransactions dataEntires) {
        StringBuilder output = new StringBuilder();
        for (ClientSideTest test : getClientSideTests()) {
            test.execute(dataEntires);
            setCounts(test);
            output.append(test.getTestExecutionReport()).append(System.lineSeparator());
        }
        testExecutionReport += output;
    }

    public void executeServerSideTests(PerfMonDataEntries dataEntires) {
        StringBuilder output = new StringBuilder();
        for (ServerSideTest test : getServerSideTests()) {
            test.execute(dataEntires);
            setCounts(test);
            output.append(test.getTestExecutionReport()).append(System.lineSeparator());
        }
        testExecutionReport += output;
    }

    private void setCounts(LightningTest test) {
        if (test.getResult() == TestResult.PASS) {
            passCount++;
        } else if (test.getResult() == TestResult.FAIL) {
            failCount++;
        } else if (test.getResult() == TestResult.ERROR) {
            ignoreCount++;
        }
    }

    public void printTestExecutionReport() {
        for (String line : Arrays.asList(getTestExecutionReport().split(System.lineSeparator()))) {
            logger.info(line);
        }
    }

    public String getTestExecutionReport() {
        return testExecutionReport;
    }

    public int getTestCount() {
        return
                ((clientSideTests != null) ? clientSideTests.size() : 0) +
                        ((serverSideTests != null) ? serverSideTests.size() : 0);
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

    public List<LightningTest> getTests() {
        List<LightningTest> tests = new ArrayList<>();
        tests.addAll(clientSideTests);
        tests.addAll(serverSideTests);
        return tests;
    }
}
