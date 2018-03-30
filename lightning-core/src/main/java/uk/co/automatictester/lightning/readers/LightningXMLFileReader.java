package uk.co.automatictester.lightning.readers;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import uk.co.automatictester.lightning.exceptions.XMLFileException;
import uk.co.automatictester.lightning.exceptions.XMLFileNoTestsException;
import uk.co.deliverymind.lightning.enums.ServerSideTestType;
import uk.co.deliverymind.lightning.exceptions.XMLFileException;
import uk.co.deliverymind.lightning.exceptions.XMLFileNoTestsException;
import uk.co.deliverymind.lightning.tests.*;
import uk.co.deliverymind.lightning.utils.LightningXMLProcessingHelpers;
import uk.co.deliverymind.lightning.utils.Percent;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LightningXMLFileReader extends LightningXMLProcessingHelpers {

    private List<ClientSideTest> clientSideTests = new ArrayList<>();
    private List<ServerSideTest> serverSideTests = new ArrayList<>();

    public void readTests(File xmlFile) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(xmlFile);
            doc.getDocumentElement().normalize();

            addRespTimeAvgTests(doc);
            addRespTimeStdDevTestNodes(doc);
            addPassedTransactionsTestNodes(doc);
            addRespTimeNthPercTests(doc);
            addThroughputTests(doc);
            addRespTimeMaxTests(doc);
            addRespTimeMedianTests(doc);
            addServerSideTests(doc);

        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new XMLFileException(e);
        }

        if (getTestCount() == 0) {
            throw new XMLFileNoTestsException("No tests of expected type found in XML file");
        }
    }

    public List<ClientSideTest> getClientSideTests() {
        return clientSideTests;
    }

    public List<ServerSideTest> getServerSideTests() {
        return serverSideTests;
    }

    protected int getTestCount() {
        return
                (clientSideTests != null ? clientSideTests.size() : 0) +
                        (serverSideTests != null ? serverSideTests.size() : 0);
    }

    protected void addPassedTransactionsTestNodes(Document xmlDoc) {
        String testType = "passedTransactionsTest";
        NodeList passedTransactionsTestNodes = xmlDoc.getElementsByTagName(testType);
        for (int i = 0; i < passedTransactionsTestNodes.getLength(); i++) {
            Element passedTransactionsElement = (Element) passedTransactionsTestNodes.item(i);

            String name = getTestName(passedTransactionsElement);
            String description = getTestDescription(passedTransactionsElement);
            String transactionName = getTransactionName(passedTransactionsElement);

            int allowedNumberOfFailedTransactions;
            int allowedPercentOfFailedTransactions;
            PassedTransactionsTest passedTransactionsTest;

            if (isSubElementPresent(passedTransactionsElement, "allowedNumberOfFailedTransactions")) {
                allowedNumberOfFailedTransactions = getIntegerValueFromElement(passedTransactionsElement, "allowedNumberOfFailedTransactions");
                passedTransactionsTest = new PassedTransactionsTest(name, testType, description, transactionName, allowedNumberOfFailedTransactions);
            } else {
                allowedPercentOfFailedTransactions = getPercent(passedTransactionsElement, "allowedPercentOfFailedTransactions");
                passedTransactionsTest = new PassedTransactionsTest(name, testType, description, transactionName, new Percent(allowedPercentOfFailedTransactions));
            }

            passedTransactionsTest.setRegexp(isSubElementPresent(passedTransactionsElement, "regexp"));

            clientSideTests.add(passedTransactionsTest);
        }

    }

    protected void addRespTimeStdDevTestNodes(Document xmlDoc) {
        String testType = "respTimeStdDevTest";
        NodeList respTimeStdDevTestNodes = xmlDoc.getElementsByTagName(testType);
        for (int i = 0; i < respTimeStdDevTestNodes.getLength(); i++) {
            Element respTimeStdDevTestElement = (Element) respTimeStdDevTestNodes.item(i);

            String name = getTestName(respTimeStdDevTestElement);
            String description = getTestDescription(respTimeStdDevTestElement);
            String transactionName = getTransactionName(respTimeStdDevTestElement);
            int maxRespTimeStdDevTime = getIntegerValueFromElement(respTimeStdDevTestElement, "maxRespTimeStdDev");

            RespTimeStdDevTest respTimeStdDevTest = new RespTimeStdDevTest(name, testType, description, transactionName, maxRespTimeStdDevTime);
            respTimeStdDevTest.setRegexp(isSubElementPresent(respTimeStdDevTestElement, "regexp"));

            clientSideTests.add(respTimeStdDevTest);
        }
    }

    protected void addRespTimeAvgTests(Document xmlDoc) {
        String testType = "avgRespTimeTest";
        NodeList avgRespTimeTestNodes = xmlDoc.getElementsByTagName(testType);
        for (int i = 0; i < avgRespTimeTestNodes.getLength(); i++) {
            Element avgRespTimeTestElement = (Element) avgRespTimeTestNodes.item(i);

            String name = getTestName(avgRespTimeTestElement);
            String description = getTestDescription(avgRespTimeTestElement);
            String transactionName = getTransactionName(avgRespTimeTestElement);
            int maxAvgRespTime = getIntegerValueFromElement(avgRespTimeTestElement, "maxAvgRespTime");

            RespTimeAvgTest avgRespTimeTest = new RespTimeAvgTest(name, testType, description, transactionName, maxAvgRespTime);
            avgRespTimeTest.setRegexp(isSubElementPresent(avgRespTimeTestElement, "regexp"));

            clientSideTests.add(avgRespTimeTest);
        }
    }

    protected void addRespTimeMaxTests(Document xmlDoc) {
        String testType = "maxRespTimeTest";
        NodeList avgRespTimeTestNodes = xmlDoc.getElementsByTagName(testType);
        for (int i = 0; i < avgRespTimeTestNodes.getLength(); i++) {
            Element maxRespTimeTestElement = (Element) avgRespTimeTestNodes.item(i);

            String name = getTestName(maxRespTimeTestElement);
            String description = getTestDescription(maxRespTimeTestElement);
            String transactionName = getTransactionName(maxRespTimeTestElement);
            int maxRespTime = getIntegerValueFromElement(maxRespTimeTestElement, "maxAllowedRespTime");

            RespTimeMaxTest maxRespTimeTest = new RespTimeMaxTest(name, testType, description, transactionName, maxRespTime);
            maxRespTimeTest.setRegexp(isSubElementPresent(maxRespTimeTestElement, "regexp"));

            clientSideTests.add(maxRespTimeTest);
        }
    }

    protected void addRespTimeNthPercTests(Document xmlDoc) {
        String testType = "nthPercRespTimeTest";
        NodeList respTimeNthPercTestNodes = xmlDoc.getElementsByTagName(testType);
        for (int i = 0; i < respTimeNthPercTestNodes.getLength(); i++) {
            Element respTimeNthPercTestElement = (Element) respTimeNthPercTestNodes.item(i);

            String name = getTestName(respTimeNthPercTestElement);
            String description = getTestDescription(respTimeNthPercTestElement);
            String transactionName = getTransactionName(respTimeNthPercTestElement);
            int percentile = getPercentile(respTimeNthPercTestElement, "percentile");
            int maxRespTime = getIntegerValueFromElement(respTimeNthPercTestElement, "maxRespTime");

            RespTimeNthPercentileTest nthPercRespTimeTest = new RespTimeNthPercentileTest(name, testType, description, transactionName, percentile, maxRespTime);
            nthPercRespTimeTest.setRegexp(isSubElementPresent(respTimeNthPercTestElement, "regexp"));

            clientSideTests.add(nthPercRespTimeTest);
        }
    }

    protected void addRespTimeMedianTests(Document xmlDoc) {
        String testType = "medianRespTimeTest";
        NodeList respTimeMedianTestNodes = xmlDoc.getElementsByTagName(testType);
        for (int i = 0; i < respTimeMedianTestNodes.getLength(); i++) {
            Element respTimeMedianTestElement = (Element) respTimeMedianTestNodes.item(i);

            String name = getTestName(respTimeMedianTestElement);
            String description = getTestDescription(respTimeMedianTestElement);
            String transactionName = getTransactionName(respTimeMedianTestElement);
            int maxRespTime = getIntegerValueFromElement(respTimeMedianTestElement, "maxRespTime");

            RespTimeMedianTest respTimeMedianTest = new RespTimeMedianTest(name, testType, description, transactionName, maxRespTime);
            respTimeMedianTest.setRegexp(isSubElementPresent(respTimeMedianTestElement, "regexp"));

            clientSideTests.add(respTimeMedianTest);
        }
    }

    protected void addThroughputTests(Document xmlDoc) {
        String testType = "throughputTest";
        NodeList respTimeNthPercTestNodes = xmlDoc.getElementsByTagName(testType);
        for (int i = 0; i < respTimeNthPercTestNodes.getLength(); i++) {
            Element throughputTestElement = (Element) respTimeNthPercTestNodes.item(i);

            String name = getTestName(throughputTestElement);
            String description = getTestDescription(throughputTestElement);
            String transactionName = getTransactionName(throughputTestElement);
            double minThroughput = getDoubleValueFromElement(throughputTestElement, "minThroughput");

            ThroughputTest throughputTest = new ThroughputTest(name, testType, description, transactionName, minThroughput);
            throughputTest.setRegexp(isSubElementPresent(throughputTestElement, "regexp"));

            clientSideTests.add(throughputTest);
        }
    }

    protected void addServerSideTests(Document xmlDoc) {
        String testType = "serverSideTest";
        NodeList serverSideTestNodes = xmlDoc.getElementsByTagName(testType);
        for (int i = 0; i < serverSideTestNodes.getLength(); i++) {
            Element serverSideTestElement = (Element) serverSideTestNodes.item(i);

            String name = getTestName(serverSideTestElement);
            ServerSideTestType subType = getSubType(serverSideTestElement);
            String description = getTestDescription(serverSideTestElement);
            String hostAndMetric = getHostAndMetric(serverSideTestElement);
            int metricValueA = getIntegerValueFromElement(serverSideTestElement, "metricValueA");

            int avgRespTimeB;
            ServerSideTest serverSideTest;

            if (subType.name().equals(ServerSideTestType.BETWEEN.name())) {
                avgRespTimeB = getIntegerValueFromElement(serverSideTestElement, "metricValueB");
                serverSideTest = new ServerSideTest(name, testType, subType, description, hostAndMetric, metricValueA, avgRespTimeB);
            } else {
                serverSideTest = new ServerSideTest(name, testType, subType, description, hostAndMetric, metricValueA);
            }

            serverSideTests.add(serverSideTest);
        }
    }
}
