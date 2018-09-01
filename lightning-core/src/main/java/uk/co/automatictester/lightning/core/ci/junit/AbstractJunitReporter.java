package uk.co.automatictester.lightning.core.ci.junit;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import uk.co.automatictester.lightning.core.enums.TestResult;
import uk.co.automatictester.lightning.core.exceptions.JunitReportGenerationException;
import uk.co.automatictester.lightning.core.state.TestSet;
import uk.co.automatictester.lightning.core.structures.LightningTests;
import uk.co.automatictester.lightning.core.tests.base.LightningTest;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;

public abstract class AbstractJunitReporter {

    protected Document doc;

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

    protected void generateJUnitReportContent(TestSet testSet) {
        Element testsuite = getTestsuite(testSet);
        Node rootElement = doc.appendChild(testsuite);
        LightningTests tests = LightningTests.getInstance();
        tests.get().forEach(test -> {
            Element testcase = getTestcase(test);
            rootElement.appendChild(testcase);
        });
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

    Element getTestsuite(TestSet testSet) {
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

    Element getTestcase(LightningTest test) {
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

    private static void setCommonFailureData(Element element, LightningTest test) {
        String testType = test.getType();
        String actualResultDescription = test.getActualResultDescription();
        String testExecutionReport = test.getTestExecutionReport();

        element.setAttribute("type", testType);
        element.setAttribute("message", actualResultDescription);
        element.setTextContent(testExecutionReport);
    }
}