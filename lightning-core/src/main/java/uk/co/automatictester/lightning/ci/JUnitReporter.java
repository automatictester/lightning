package uk.co.automatictester.lightning.ci;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import uk.co.automatictester.lightning.TestSet;
import uk.co.automatictester.lightning.enums.TestResult;
import uk.co.automatictester.lightning.tests.LightningTest;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class JUnitReporter {

    private TestSet testSet;
    private Document doc;

    public JUnitReporter() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db;
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
        doc = db.newDocument();
        doc.setXmlStandalone(true);
    }

    public void setTestSet(TestSet testSet) {
        this.testSet = testSet;
    }

    public void generateJUnitReport() {
        Node rootElement = doc.appendChild(getTestsuite());
        for (LightningTest test : testSet.getTests()) {
            rootElement.appendChild(getTestcase(test));
        }
        saveReportToDisk();
    }

    public Element getTestsuite() {
        Element testsuite = doc.createElement("testsuite");
        testsuite.setAttribute("tests", String.valueOf(testSet.getTestCount()));
        testsuite.setAttribute("failures", String.valueOf(testSet.getFailCount()));
        testsuite.setAttribute("errors", String.valueOf(testSet.getErrorCount()));
        testsuite.setAttribute("time", "0");
        testsuite.setAttribute("name", "Lightning");
        return testsuite;
    }

    public Element getTestcase(LightningTest test) {
        Element testcase = doc.createElement("testcase");
        testcase.setAttribute("time", "0");
        testcase.setAttribute("name", test.getName());

        if (test.getResult().equals(TestResult.FAIL)) {
            Element failure = doc.createElement("failure");
            setCommonFailureData(failure, test);
            testcase.appendChild(failure);

        } else if (test.getResult().equals(TestResult.ERROR)) {
            Element error = doc.createElement("error");
            setCommonFailureData(error, test);
            testcase.appendChild(error);
        }
        return testcase;
    }

    private void setCommonFailureData(Element e, LightningTest test) {
        e.setAttribute("type", test.getType());
        e.setAttribute("message", test.getActualResultDescription());
        e.setTextContent(test.getTestExecutionReport());
    }

    private void saveReportToDisk() {
        Transformer transformer;
        try {
            transformer = TransformerFactory.newInstance().newTransformer();
        } catch (TransformerConfigurationException e) {
            throw new RuntimeException(e);
        }
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        DOMSource source = new DOMSource(doc);

        File junitReport = new File("junit.xml");
        StreamResult file = new StreamResult(junitReport);
        try {
            transformer.transform(source, file);
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
    }
}
