package uk.co.automatictester.lightning.config;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import uk.co.automatictester.lightning.exceptions.XMLFileException;
import uk.co.automatictester.lightning.exceptions.XMLFileNoTestsException;
import uk.co.automatictester.lightning.handlers.*;
import uk.co.automatictester.lightning.tests.ClientSideTest;
import uk.co.automatictester.lightning.tests.ServerSideTest;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class LightningConfig {

    public void readTests(File xmlFile) {
        LightningTests.flush();
        NodeList nodes = readXmlFile(xmlFile);
        loadAllTests(nodes);
        throwExceptionIfNoTests();
    }

    public List<ClientSideTest> getClientSideTests() {
        return LightningTests.getClientSideTests();
    }

    public List<ServerSideTest> getServerSideTests() {
        return LightningTests.getServerSideTests();
    }

    private NodeList readXmlFile(File xmlFile) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(xmlFile);
            Node node = doc.getDocumentElement();
            return node.getChildNodes();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new XMLFileException(e);
        }
    }

    protected void loadAllTests(NodeList nodes) {
        for (int i = 0; i < nodes.getLength(); i++) {
            if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) nodes.item(i);

                AvgRespTimeTestHandler avgRespTimeTestHandler = new AvgRespTimeTestHandler();
                RespTimeStdDevTestHandler respTimeStdDevTestHandler = new RespTimeStdDevTestHandler();
                PassedTransactionsTestHandler passedTransactionsTestHandler = new PassedTransactionsTestHandler();
                NthPercRespTimeTestHandler nthPercRespTimeTestHandler = new NthPercRespTimeTestHandler();
                ThroughputTestHandler throughputTestHandler = new ThroughputTestHandler();
                MaxRespTimeTestHandler maxRespTimeTestHandler = new MaxRespTimeTestHandler();
                MedianRespTimeTestHandler medianRespTimeTestHandler = new MedianRespTimeTestHandler();
                ServerSideTestHandler serverSideTestHandler = new ServerSideTestHandler();
                DefaultHandler defaultHandler = new DefaultHandler();

                avgRespTimeTestHandler.setNextHandler(respTimeStdDevTestHandler);
                respTimeStdDevTestHandler.setNextHandler(passedTransactionsTestHandler);
                passedTransactionsTestHandler.setNextHandler(nthPercRespTimeTestHandler);
                nthPercRespTimeTestHandler.setNextHandler(throughputTestHandler);
                throughputTestHandler.setNextHandler(maxRespTimeTestHandler);
                maxRespTimeTestHandler.setNextHandler(medianRespTimeTestHandler);
                medianRespTimeTestHandler.setNextHandler(serverSideTestHandler);
                serverSideTestHandler.setNextHandler(defaultHandler);

                avgRespTimeTestHandler.processHandler(element);
            }
        }
    }

    protected void throwExceptionIfNoTests() {
        if (LightningTests.getTestCount() == 0) {
            throw new XMLFileNoTestsException("No tests of expected type found in XML file");
        }
    }
}
