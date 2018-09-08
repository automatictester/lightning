package uk.co.automatictester.lightning.core.reporters.junit.base;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import uk.co.automatictester.lightning.core.enums.TestResult;
import uk.co.automatictester.lightning.core.exceptions.JunitReportGenerationException;
import uk.co.automatictester.lightning.core.state.tests.LightningTestSet;
import uk.co.automatictester.lightning.core.state.tests.results.LightningTestSetResult;
import uk.co.automatictester.lightning.core.tests.base.AbstractTest;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;

public abstract class AbstractJunitReporter {

    protected final Document doc;

    protected AbstractJunitReporter() {
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

    protected void generateJUnitReportContent(LightningTestSetResult testSet) { // TODO: bad
        Element testsuite = getTestsuite(testSet);
        Node rootElement = doc.appendChild(testsuite);
        testSet.tests().forEach(test -> {
            Element testcase = getTestcase(test);
            rootElement.appendChild(testcase);
        });
    }

    protected Transformer transformer() {
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

    public Element getTestsuite(LightningTestSetResult testSet) {
        Element testsuite = doc.createElement("testsuite");
        String testCount = String.valueOf(testSet.testCount());
        String failCount = String.valueOf(testSet.failCount());
        String errorCount = String.valueOf(testSet.errorCount());

        testsuite.setAttribute("tests", testCount);
        testsuite.setAttribute("failures", failCount);
        testsuite.setAttribute("errors", errorCount);
        testsuite.setAttribute("time", "0");
        testsuite.setAttribute("name", "Lightning");

        return testsuite;
    }

    public Element getTestcase(AbstractTest test) {
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

    private static void setCommonFailureData(Element element, AbstractTest test) {
        String testType = test.type();
        String actualResultDescription = test.actualResultDescription();
        String testExecutionReport = test.getTestExecutionReport();

        element.setAttribute("type", testType);
        element.setAttribute("message", actualResultDescription);
        element.setTextContent(testExecutionReport);
    }
}
