package uk.co.automatictester.lightning.core.utils;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import uk.co.automatictester.lightning.core.enums.ServerSideTestType;
import uk.co.automatictester.lightning.core.exceptions.XMLFileMissingElementException;
import uk.co.automatictester.lightning.core.exceptions.XMLFileMissingElementValueException;
import uk.co.automatictester.lightning.core.exceptions.XMLFileNoValidSubTypeException;
import uk.co.automatictester.lightning.core.exceptions.XMLFilePercentileException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class DomElementsTest {

    @Test
    public void testGetTestName() {
        String xmlSnippet = "<avgRespTimeTest><testName>tn</testName></avgRespTimeTest>";
        Element element = getElementFromString(xmlSnippet);
        String testName = DomElements.getTestName(element);
        assertThat(testName, is(equalTo("tn")));
    }

    @Test(expectedExceptions = XMLFileMissingElementValueException.class)
    public void testMissingElementValueException() {
        String xmlSnippet = "<avgRespTimeTest><testName></testName></avgRespTimeTest>";
        Element element = getElementFromString(xmlSnippet);
        DomElements.getTestName(element);
    }

    @Test(expectedExceptions = XMLFileMissingElementException.class)
    public void testMissingElementException() {
        String xmlSnippet = "<avgRespTimeTest></avgRespTimeTest>";
        Element element = getElementFromString(xmlSnippet);
        DomElements.getTestName(element);
    }

    @DataProvider(name = "serverSideTestTypes")
    private Object[][] serverSideTestTypes() {
        return new String[][]{
                {"LESS_THAN"},
                {"BETWEEN"},
                {"GREATER_THAN"}
        };
    }

    @Test(dataProvider = "serverSideTestTypes")
    public void testGetSubType(String type) {
        String xmlSnippet = String.format("<serverSideTest><subType>%s</subType></serverSideTest>", type);
        Element element = getElementFromString(xmlSnippet);
        ServerSideTestType serverSideTestType = DomElements.getSubType(element);
        assertThat(serverSideTestType.toString(), is(equalTo(type)));
    }

    @Test(expectedExceptions = XMLFileNoValidSubTypeException.class)
    public void testGetSubTypeException() {
        String xmlSnippet = "<serverSideTest><subType>WRONG</subType></serverSideTest>";
        Element element = getElementFromString(xmlSnippet);
        DomElements.getSubType(element);
    }

    @Test
    public void testHasRegexp() {
        String xmlSnippet = "<throughputTest><regexp/></throughputTest>";
        Element element = getElementFromString(xmlSnippet);
        boolean hasRegexp = DomElements.hasRegexp(element);
        assertThat(hasRegexp, is(true));
    }

    @Test
    public void testHasTransactionName() {
        String xmlSnippet = "<throughputTest><transactionName>tn</transactionName></throughputTest>";
        Element element = getElementFromString(xmlSnippet);
        boolean hasTransactionName = DomElements.hasTransactionName(element);
        assertThat(hasTransactionName, is(true));
    }

    @Test
    public void testHasHostAndMetric() {
        String xmlSnippet = "<serverSideTest><hostAndMetric>192.168.0.1 CPU</hostAndMetric></serverSideTest>";
        Element element = getElementFromString(xmlSnippet);
        boolean hasHostAndMetric = DomElements.hasHostAndMetric(element);
        assertThat(hasHostAndMetric, is(true));
    }

    @Test
    public void testIsSubElementPresent() {
        String xmlSnippet = "<passedTransactionsTest><allowedNumberOfFailedTransactions>10</allowedNumberOfFailedTransactions></passedTransactionsTest>";
        Element element = getElementFromString(xmlSnippet);
        boolean isSubElementPresent = DomElements.isSubElementPresent(element, "allowedNumberOfFailedTransactions");
        assertThat(isSubElementPresent, is(true));
    }

    @Test
    public void testGetTransactionName() {
        String xmlSnippet = "<avgRespTimeTest><transactionName>tn</transactionName></avgRespTimeTest>";
        Element element = getElementFromString(xmlSnippet);
        String transactionName = DomElements.getTransactionName(element);
        assertThat(transactionName, is(equalTo("tn")));
    }

    @Test
    public void testGetHostAndMetric() {
        String xmlSnippet = "<serverSideTest><hostAndMetric>192.168.0.1 CPU</hostAndMetric></serverSideTest>";
        Element element = getElementFromString(xmlSnippet);
        String hostAndMetric = DomElements.getHostAndMetric(element);
        assertThat(hostAndMetric, is(equalTo("192.168.0.1 CPU")));
    }

    @Test
    public void testGetTestDescription() {
        String xmlSnippet = "<avgRespTimeTest><description>d</description></avgRespTimeTest>";
        Element element = getElementFromString(xmlSnippet);
        String description = DomElements.getTestDescription(element);
        assertThat(description, is(equalTo("d")));
    }

    @Test
    public void testGetIntegerValueFromElement() {
        String xmlSnippet = "<nthPercRespTimeTest><maxRespTime>200</maxRespTime></nthPercRespTimeTest>";
        Element element = getElementFromString(xmlSnippet);
        int value = DomElements.getIntegerValueFromElement(element, "maxRespTime");
        assertThat(value, is(equalTo(200)));
    }

    @Test(expectedExceptions = NumberFormatException.class)
    public void testGetIntegerValueFromElementException() {
        String xmlSnippet = "<nthPercRespTimeTest><maxRespTime>x</maxRespTime></nthPercRespTimeTest>";
        Element element = getElementFromString(xmlSnippet);
        DomElements.getIntegerValueFromElement(element, "maxRespTime");
    }

    @Test
    public void testGetDoubleValueFromElement() {
        String xmlSnippet = "<throughputTest><minThroughput>2.2</minThroughput></throughputTest>";
        Element element = getElementFromString(xmlSnippet);
        double value = DomElements.getDoubleValueFromElement(element, "minThroughput");
        assertThat(value, is(equalTo(2.2)));
    }

    @Test(expectedExceptions = NumberFormatException.class)
    public void testGetDoubleValueFromElementException() {
        String xmlSnippet = "<throughputTest><minThroughput>x</minThroughput></throughputTest>";
        Element element = getElementFromString(xmlSnippet);
        DomElements.getDoubleValueFromElement(element, "minThroughput");
    }

    @DataProvider(name = "percentileTestData")
    private Object[][] percentileTestData() {
        return new Integer[][]{
                {1},
                {100}
        };
    }

    @Test(dataProvider = "percentileTestData")
    public void testGetPercentile(int percentile) {
        String xmlSnippet = String.format("<nthPercRespTimeTest><percentile>%d</percentile></nthPercRespTimeTest>", percentile);
        Element element = getElementFromString(xmlSnippet);
        int value = DomElements.getPercentile(element, "percentile");
        assertThat(value, is(equalTo(percentile)));
    }

    @DataProvider(name = "negativePercentileTestData")
    private Object[][] negativePercentileTestData() {
        return new Integer[][]{
                {-1},
                {0},
                {101}
        };
    }

    @Test(dataProvider = "negativePercentileTestData", expectedExceptions = XMLFilePercentileException.class)
    public void testGetPercentileException(int percentile) {
        String xmlSnippet = String.format("<nthPercRespTimeTest><percentile>%d</percentile></nthPercRespTimeTest>", percentile);
        Element element = getElementFromString(xmlSnippet);
        DomElements.getPercentile(element, "percentile");
    }

    @Test
    public void testGetPercentAsInt() {
        String xmlSnippet = "<passedTransactionsTest><allowedPercentOfFailedTransactions>5</allowedPercentOfFailedTransactions></passedTransactionsTest>";
        Element element = getElementFromString(xmlSnippet);
        int value = DomElements.getPercentAsInt(element, "allowedPercentOfFailedTransactions");
        assertThat(value, is(equalTo(5)));
    }

    private Element getElementFromString(String xmlSnippet) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xmlSnippet));
            Document doc = db.parse(is);
            return doc.getDocumentElement();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new RuntimeException("Things went wrong processing XML snippet");
        }
    }
}
