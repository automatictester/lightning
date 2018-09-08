package uk.co.automatictester.lightning.core.readers;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import uk.co.automatictester.lightning.core.exceptions.XMLFileException;
import uk.co.automatictester.lightning.core.handlers.*;
import uk.co.automatictester.lightning.core.state.tests.LightningTestSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class ConfigReader {

    protected LightningTestSet testSet = new LightningTestSet();

    public LightningTestSet readTests(File xmlFile) {
        testSet.flush(); // TODO - do we really need it ?
        NodeList nodes = readXmlFile(xmlFile);
        loadAllTests(nodes);
        throwExceptionIfNoTests();
        return testSet;
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

                AvgRespTimeTestHandler avgRespTimeTestHandler = new AvgRespTimeTestHandler(testSet);
                RespTimeStdDevTestHandler respTimeStdDevTestHandler = new RespTimeStdDevTestHandler(testSet);
                PassedTransactionsTestHandler passedTransactionsTestHandler = new PassedTransactionsTestHandler(testSet);
                NthPercRespTimeTestHandler nthPercRespTimeTestHandler = new NthPercRespTimeTestHandler(testSet);
                ThroughputTestHandler throughputTestHandler = new ThroughputTestHandler(testSet);
                MaxRespTimeTestHandler maxRespTimeTestHandler = new MaxRespTimeTestHandler(testSet);
                MedianRespTimeTestHandler medianRespTimeTestHandler = new MedianRespTimeTestHandler(testSet);
                ServerSideTestHandler serverSideTestHandler = new ServerSideTestHandler(testSet);
                DefaultHandler defaultHandler = new DefaultHandler(testSet);

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
        if (testSet.size() == 0) {
            throw new IllegalStateException("No tests of expected type found in XML file");
        }
    }
}
