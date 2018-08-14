package uk.co.automatictester.lightning.config;

import uk.co.automatictester.lightning.tests.ClientSideTest;
import uk.co.automatictester.lightning.tests.LightningTest;
import uk.co.automatictester.lightning.tests.ServerSideTest;

import java.util.ArrayList;
import java.util.List;

public class LightningTests {
    private static List<ClientSideTest> clientSideTests = new ArrayList<>();
    private static List<ServerSideTest> serverSideTests = new ArrayList<>();

    public static void flush() {
        clientSideTests.clear();
        serverSideTests.clear();
    }

    public static void addClientSideTest(ClientSideTest test) {
        clientSideTests.add(test);
    }

    public static void addServerSideTest(ServerSideTest test) {
        serverSideTests.add(test);
    }

    public static List<ClientSideTest> getClientSideTests() {
        return clientSideTests;
    }

    public static List<ServerSideTest> getServerSideTests() {
        return serverSideTests;
    }

    public static int getTestCount() {
        List<LightningTest> alltests = new ArrayList<>();
        alltests.addAll(LightningTests.getClientSideTests());
        alltests.addAll(LightningTests.getServerSideTests());
        return alltests.size();
    }
}
