package uk.co.automatictester.lightning;

import uk.co.automatictester.lightning.data.JMeterTransactions;
import uk.co.automatictester.lightning.data.PerfMonDataEntries;
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

    public TestSet(List<ClientSideTest> clientSideTests, List<ServerSideTest> serverSideTests) {
        this.clientSideTests = clientSideTests;
        this.serverSideTests = serverSideTests;
    }

    public void executeClientSideTests(JMeterTransactions dataEntires) {
        for (ClientSideTest test : getClientSideTests()) {
            test.execute(dataEntires);
            if (test.getResult() == TestResult.PASS) {
                passCount++;
            } else if (test.getResult() == TestResult.FAIL) {
                failCount++;
            } else if (test.getResult() == TestResult.IGNORED) {
                ignoreCount++;
            }
            test.printTestExecutionReport();
        }
    }

    public void executeServerSideTests(PerfMonDataEntries dataEntires) {
        for (ServerSideTest test : getServerSideTests()) {
            test.execute(dataEntires);
            if (test.getResult() == TestResult.PASS) {
                passCount++;
            } else if (test.getResult() == TestResult.FAIL) {
                failCount++;
            } else if (test.getResult() == TestResult.IGNORED) {
                ignoreCount++;
            }
            test.printTestExecutionReport();
        }
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

    public int getIgnoreCount() {
        return ignoreCount;
    }

    private List<ClientSideTest> getClientSideTests() {
        return clientSideTests;
    }

    private List<ServerSideTest> getServerSideTests() {
        return serverSideTests;
    }

}
