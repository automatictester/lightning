package uk.co.automatictester.lightning.core.reporters.junit;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import uk.co.automatictester.lightning.core.enums.TestResult;
import uk.co.automatictester.lightning.core.exceptions.JunitReportGenerationException;
import uk.co.automatictester.lightning.core.state.tests.TestSet;
import uk.co.automatictester.lightning.core.tests.LightningTest;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;

public class JunitReportGenerator {

    private final Document doc;
    private final TestSet testSet;

    public JunitReportGenerator(TestSet testSet) {
        this.testSet = testSet;
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

    public String generate() {
        generateXmlDocument();
        return transformXmlDocumentToString();
    }

    Element getTestsuite() {
        Element testsuite = doc.createElement("testsuite");
        String testCount = String.valueOf(testSet.size());
        String failCount = String.valueOf(testSet.failCount());
        String errorCount = String.valueOf(testSet.errorCount());

        testsuite.setAttribute("tests", testCount);
        testsuite.setAttribute("failures", failCount);
        testsuite.setAttribute("errors", errorCount);
        testsuite.setAttribute("time", "0");
        testsuite.setAttribute("name", "Lightning");

        return testsuite;
    }

    Element getTestcase(LightningTest test) {
        Element testcase = doc.createElement("testcase");
        testcase.setAttribute("time", "0");
        String testName = test.name();
        testcase.setAttribute("name", testName);

        TestResult testResult = test.result();
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

    private static void setCommonFailureData(Element element, LightningTest test) {
        String testType = test.type();
        String actualResultDescription = test.actualResultDescription();
        String testExecutionReport = test.getTestExecutionReport();

        element.setAttribute("type", testType);
        element.setAttribute("message", actualResultDescription);
        element.setTextContent(testExecutionReport);
    }

    private void generateXmlDocument() {
        Element testsuite = getTestsuite();
        Node rootElement = doc.appendChild(testsuite);
        testSet.get().forEach(test -> {
            Element testcase = getTestcase(test);
            rootElement.appendChild(testcase);
        });
    }

    private String transformXmlDocumentToString() {
        Transformer transformer = transformer();
        DOMSource source = new DOMSource(doc);
        StringWriter stringWriter = new StringWriter();
        try {
            transformer.transform(source, new StreamResult(stringWriter));
        } catch (TransformerException e) {
            throw new JunitReportGenerationException(e);
        }
        return stringWriter.toString();
    }

    private Transformer transformer() {
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
}
