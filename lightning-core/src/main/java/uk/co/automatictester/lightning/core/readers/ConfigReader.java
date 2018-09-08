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

    protected ConfigReader() {
    }

    public static void readTests(File xmlFile) {
        LightningTestSet.getInstance().flush();
        NodeList nodes = readXmlFile(xmlFile);
        loadAllTests(nodes);
        throwExceptionIfNoTests();
    }

    private static NodeList readXmlFile(File xmlFile) {
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

    protected static void loadAllTests(NodeList nodes) {
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

    protected static void throwExceptionIfNoTests() {
        if (LightningTestSet.getInstance().size() == 0) {
            throw new IllegalStateException("No tests of expected type found in XML file");
        }
    }
}
