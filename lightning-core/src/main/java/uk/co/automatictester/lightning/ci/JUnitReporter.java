package uk.co.automatictester.lightning.ci;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import uk.co.automatictester.lightning.TestSet;
import uk.co.automatictester.lightning.enums.TestResult;
import uk.co.automatictester.lightning.exceptions.JunitReportGenerationException;
import uk.co.automatictester.lightning.tests.base.LightningTest;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class JUnitReporter {

    protected Document doc;

    public JUnitReporter() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db;
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new JunitReportGenerationException(e);
        }
        doc = db.newDocument();
        doc.setXmlStandalone(true);
    }

    public void generateJUnitReport(TestSet testSet) {
        generateJUnitReportContent(testSet);
        saveReportToDisk();
    }

    public Element getTestsuite(TestSet testSet) {
        Element testsuite = doc.createElement("testsuite");
        String testCount = String.valueOf(testSet.getTestCount());
        String failCount = String.valueOf(testSet.getFailCount());
        String errorCount = String.valueOf(testSet.getErrorCount());

        testsuite.setAttribute("tests", testCount);
        testsuite.setAttribute("failures", failCount);
        testsuite.setAttribute("errors", errorCount);
        testsuite.setAttribute("time", "0");
        testsuite.setAttribute("name", "Lightning");

        return testsuite;
    }

    public Element getTestcase(LightningTest test) {
        Element testcase = doc.createElement("testcase");
        testcase.setAttribute("time", "0");
        String testName = test.getName();
        testcase.setAttribute("name", testName);

        TestResult testResult = test.getResult();
        Element resultElement = null;
        switch (testResult) {
            case FAIL:
                resultElement = doc.createElement("failure");
                break;
            case ERROR:
                resultElement = doc.createElement("error");
                break;
        }
        if (resultElement != null) {
            setCommonFailureData(resultElement, test);
            testcase.appendChild(resultElement);
        }

        return testcase;
    }

    protected void generateJUnitReportContent(TestSet testSet) {
        Element testsuite = getTestsuite(testSet);
        Node rootElement = doc.appendChild(testsuite);
        for (LightningTest test : testSet.getTests()) {
            Element testcase = getTestcase(test);
            rootElement.appendChild(testcase);
        }
    }

    protected Transformer getTransformer() {
        Transformer transformer;
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformer = transformerFactory.newTransformer();
        } catch (TransformerConfigurationException e) {
            throw new JunitReportGenerationException(e);
        }
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        return transformer;
    }

    private static void setCommonFailureData(Element element, LightningTest test) {
        String testType = test.getType();
        String actualResultDescription = test.getActualResultDescription();
        String testExecutionReport = test.getTestExecutionReport();

        element.setAttribute("type", testType);
        element.setAttribute("message", actualResultDescription);
        element.setTextContent(testExecutionReport);
    }

    private void saveReportToDisk() {
        Transformer transformer = getTransformer();
        DOMSource source = new DOMSource(doc);

        File junitReport = new File("junit.xml");
        StreamResult file = new StreamResult(junitReport);
        try {
            transformer.transform(source, file);
        } catch (TransformerException e) {
            throw new JunitReportGenerationException(e);
        }
    }
}
