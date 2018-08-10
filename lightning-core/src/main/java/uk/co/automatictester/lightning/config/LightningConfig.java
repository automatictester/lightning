package uk.co.automatictester.lightning.config;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import uk.co.automatictester.lightning.enums.ServerSideTestType;
import uk.co.automatictester.lightning.exceptions.XMLFileException;
import uk.co.automatictester.lightning.exceptions.XMLFileNoTestsException;
import uk.co.automatictester.lightning.tests.*;
import uk.co.automatictester.lightning.utils.Percent;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static uk.co.automatictester.lightning.utils.LightningConfigProcessingHelper.*;

public class LightningConfig {

    private List<ClientSideTest> clientSideTests = new ArrayList<>();
    private List<ServerSideTest> serverSideTests = new ArrayList<>();

    public void readTests(File xmlFile) {
        Document doc = readXmlFile(xmlFile);
        loadAllTests(doc);
        throwExceptionIfNoTests();
    }

    public List<ClientSideTest> getClientSideTests() {
        return clientSideTests;
    }

    public List<ServerSideTest> getServerSideTests() {
        return serverSideTests;
    }

    protected void loadAllTests(Document doc) {
        addRespTimeAvgTests(doc);
        addRespTimeStdDevTests(doc);
        addPassedTransactionsTests(doc);
        addRespTimeNthPercTests(doc);
        addThroughputTests(doc);
        addRespTimeMaxTests(doc);
        addRespTimeMedianTests(doc);
        addServerSideTests(doc);
    }

    protected void throwExceptionIfNoTests() {
        if (getTestCount() == 0) {
            throw new XMLFileNoTestsException("No tests of expected type found in XML file");
        }
    }

    private Document readXmlFile(File xmlFile) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(xmlFile);
            doc.getDocumentElement().normalize();
            return doc;
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new XMLFileException(e);
        }
    }

    private int getTestCount() {
        List<LightningTest> alltests = new ArrayList<>();
        alltests.addAll(clientSideTests);
        alltests.addAll(serverSideTests);
        return alltests.size();
    }

    private void addPassedTransactionsTests(Document xmlDoc) {
        String testType = "passedTransactionsTest";
        NodeList passedTransactionsTestNodes = xmlDoc.getElementsByTagName(testType);
        for (int i = 0; i < passedTransactionsTestNodes.getLength(); i++) {
            Element element = (Element) passedTransactionsTestNodes.item(i);

            String name = getTestName(element);
            String description = getTestDescription(element);
            String transactionName = null;
            if (hasTransactionName(element)) {
                transactionName = getTransactionName(element);
            }
            int allowedNumberOfFailedTransactions;
            int allowedPercentOfFailedTransactions;
            PassedTransactionsTest passedTransactionsTest;

            if (isSubElementPresent(element, "allowedNumberOfFailedTransactions")) {
                allowedNumberOfFailedTransactions = getIntegerValueFromElement(element, "allowedNumberOfFailedTransactions");
                passedTransactionsTest = new PassedTransactionsTest(name, testType, description, transactionName, allowedNumberOfFailedTransactions);
            } else {
                allowedPercentOfFailedTransactions = getPercent(element, "allowedPercentOfFailedTransactions");
                passedTransactionsTest = new PassedTransactionsTest(name, testType, description, transactionName, new Percent(allowedPercentOfFailedTransactions));
            }

            passedTransactionsTest.setRegexp(isSubElementPresent(element, "regexp"));

            clientSideTests.add(passedTransactionsTest);
        }

    }

    private void addRespTimeStdDevTests(Document xmlDoc) {
        String testType = "respTimeStdDevTest";
        NodeList respTimeStdDevTestNodes = xmlDoc.getElementsByTagName(testType);
        for (int i = 0; i < respTimeStdDevTestNodes.getLength(); i++) {
            Element element = (Element) respTimeStdDevTestNodes.item(i);

            String name = getTestName(element);
            String description = getTestDescription(element);
            String transactionName = null;
            if (hasTransactionName(element)) {
                transactionName = getTransactionName(element);
            }
            int maxRespTimeStdDevTime = getIntegerValueFromElement(element, "maxRespTimeStdDev");

            RespTimeStdDevTest respTimeStdDevTest = new RespTimeStdDevTest(name, testType, description, transactionName, maxRespTimeStdDevTime);
            respTimeStdDevTest.setRegexp(isSubElementPresent(element, "regexp"));

            clientSideTests.add(respTimeStdDevTest);
        }
    }

    private void addRespTimeAvgTests(Document xmlDoc) {
        String testType = "avgRespTimeTest";
        NodeList avgRespTimeTestNodes = xmlDoc.getElementsByTagName(testType);
        for (int i = 0; i < avgRespTimeTestNodes.getLength(); i++) {
            Element element = (Element) avgRespTimeTestNodes.item(i);

            String name = getTestName(element);
            String description = getTestDescription(element);
            int maxAvgRespTime = getIntegerValueFromElement(element, "maxAvgRespTime");

            RespTimeAvgTest.Builder builder = new RespTimeAvgTest.Builder(name, maxAvgRespTime).withDescription(description);
            if (hasTransactionName(element)) {
                String transactionName = getTransactionName(element);
                builder.withTransactionName(transactionName);
            }
            if (hasRegexp(element)) {
                builder.withRegexp();
            }
            RespTimeAvgTest avgRespTimeTest = builder.build();

            clientSideTests.add(avgRespTimeTest);
        }
    }

    private void addRespTimeMaxTests(Document xmlDoc) {
        String testType = "maxRespTimeTest";
        NodeList avgRespTimeTestNodes = xmlDoc.getElementsByTagName(testType);
        for (int i = 0; i < avgRespTimeTestNodes.getLength(); i++) {
            Element element = (Element) avgRespTimeTestNodes.item(i);

            String name = getTestName(element);
            String description = getTestDescription(element);
            int maxRespTime = getIntegerValueFromElement(element, "maxAllowedRespTime");

            RespTimeMaxTest.Builder builder = new RespTimeMaxTest.Builder(name, maxRespTime).withDescription(description);
            if (hasTransactionName(element)) {
                String transactionName = getTransactionName(element);
                builder.withTransactionName(transactionName);
            }
            if (hasRegexp(element)) {
                builder.withRegexp();
            }

            RespTimeMaxTest maxRespTimeTest = builder.build();
            maxRespTimeTest.setRegexp(isSubElementPresent(element, "regexp"));

            clientSideTests.add(maxRespTimeTest);
        }
    }

    private void addRespTimeNthPercTests(Document xmlDoc) {
        String testType = "nthPercRespTimeTest";
        NodeList respTimeNthPercTestNodes = xmlDoc.getElementsByTagName(testType);
        for (int i = 0; i < respTimeNthPercTestNodes.getLength(); i++) {
            Element element = (Element) respTimeNthPercTestNodes.item(i);

            String name = getTestName(element);
            String description = getTestDescription(element);
            String transactionName = null;
            if (hasTransactionName(element)) {
                transactionName = getTransactionName(element);
            }
            int percentile = getPercentile(element, "percentile");
            int maxRespTime = getIntegerValueFromElement(element, "maxRespTime");

            RespTimeNthPercentileTest nthPercRespTimeTest = new RespTimeNthPercentileTest(name, testType, description, transactionName, percentile, maxRespTime);
            nthPercRespTimeTest.setRegexp(isSubElementPresent(element, "regexp"));

            clientSideTests.add(nthPercRespTimeTest);
        }
    }

    private void addRespTimeMedianTests(Document xmlDoc) {
        String testType = "medianRespTimeTest";
        NodeList respTimeMedianTestNodes = xmlDoc.getElementsByTagName(testType);
        for (int i = 0; i < respTimeMedianTestNodes.getLength(); i++) {
            Element element = (Element) respTimeMedianTestNodes.item(i);

            String name = getTestName(element);
            String description = getTestDescription(element);
            String transactionName = null;
            if (hasTransactionName(element)) {
                transactionName = getTransactionName(element);
            }
            int maxRespTime = getIntegerValueFromElement(element, "maxRespTime");

            RespTimeMedianTest respTimeMedianTest = new RespTimeMedianTest(name, testType, description, transactionName, maxRespTime);
            respTimeMedianTest.setRegexp(isSubElementPresent(element, "regexp"));

            clientSideTests.add(respTimeMedianTest);
        }
    }

    private void addThroughputTests(Document xmlDoc) {
        String testType = "throughputTest";
        NodeList respTimeNthPercTestNodes = xmlDoc.getElementsByTagName(testType);
        for (int i = 0; i < respTimeNthPercTestNodes.getLength(); i++) {
            Element element = (Element) respTimeNthPercTestNodes.item(i);

            String name = getTestName(element);
            String description = getTestDescription(element);
            String transactionName = null;
            if (hasTransactionName(element)) {
                transactionName = getTransactionName(element);
            }
            double minThroughput = getDoubleValueFromElement(element, "minThroughput");

            ThroughputTest throughputTest = new ThroughputTest(name, testType, description, transactionName, minThroughput);
            throughputTest.setRegexp(isSubElementPresent(element, "regexp"));

            clientSideTests.add(throughputTest);
        }
    }

    private void addServerSideTests(Document xmlDoc) {
        String testType = "serverSideTest";
        NodeList serverSideTestNodes = xmlDoc.getElementsByTagName(testType);
        for (int i = 0; i < serverSideTestNodes.getLength(); i++) {
            Element element = (Element) serverSideTestNodes.item(i);

            String name = getTestName(element);
            ServerSideTestType subType = getSubType(element);
            String description = getTestDescription(element);
            String hostAndMetric = null;
            if (hasHostAndMetric(element)) {
                hostAndMetric = getHostAndMetric(element);
            }
            int metricValueA = getIntegerValueFromElement(element, "metricValueA");

            int avgRespTimeB;
            ServerSideTest serverSideTest;

            if (subType.name().equals(ServerSideTestType.BETWEEN.name())) {
                avgRespTimeB = getIntegerValueFromElement(element, "metricValueB");
                serverSideTest = new ServerSideTest(name, testType, subType, description, hostAndMetric, metricValueA, avgRespTimeB);
            } else {
                serverSideTest = new ServerSideTest(name, testType, subType, description, hostAndMetric, metricValueA);
            }

            serverSideTests.add(serverSideTest);
        }
    }
}
