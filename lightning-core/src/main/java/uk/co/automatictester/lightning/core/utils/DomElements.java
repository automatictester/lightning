package uk.co.automatictester.lightning.core.utils;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import uk.co.automatictester.lightning.core.enums.ServerSideTestType;
import uk.co.automatictester.lightning.core.exceptions.XMLFileMissingElementException;
import uk.co.automatictester.lightning.core.exceptions.XMLFileMissingElementValueException;
import uk.co.automatictester.lightning.core.exceptions.XMLFileNoValidSubTypeException;
import uk.co.automatictester.lightning.core.exceptions.XMLFilePercentileException;

public class DomElements {

    private static final String EXCEPTION_MESSAGE = "Incorrect %s value for %s: %s";

    private DomElements() {
    }

    public static String getTestName(Element element) {
        return getSubElementValueByTagName(element, "testName");
    }

    public static ServerSideTestType getSubType(Element element) {
        NodeList list = element.getElementsByTagName("subType");
        Node descriptionElement = list.item(0);
        String subType = getTextContentOrEmptyString(descriptionElement);
        ServerSideTestType testSubType;
        if (subType.equalsIgnoreCase(ServerSideTestType.LESS_THAN.name())) {
            testSubType = ServerSideTestType.LESS_THAN;
        } else if (subType.equalsIgnoreCase(ServerSideTestType.GREATER_THAN.name())) {
            testSubType = ServerSideTestType.GREATER_THAN;
        } else if (subType.equalsIgnoreCase(ServerSideTestType.BETWEEN.name())) {
            testSubType = ServerSideTestType.BETWEEN;
        } else {
            throw new XMLFileNoValidSubTypeException("Value of subType element is not in set (LESS_THAN, BETWEEN, GREATER_THAN)");
        }
        return testSubType;
    }

    public static boolean hasRegexp(Element element) {
        return isSubElementPresent(element, "regexp");
    }

    public static boolean hasTransactionName(Element element) {
        return isSubElementPresent(element, "transactionName");
    }

    public static boolean hasHostAndMetric(Element element) {
        return isSubElementPresent(element, "hostAndMetric");
    }

    public static boolean isSubElementPresent(Element element, String subElement) {
        NodeList list = element.getElementsByTagName(subElement);
        return list.getLength() > 0;
    }

    public static String getTransactionName(Element element) {
        return getSubElementTextContent(element, "transactionName");
    }

    public static String getHostAndMetric(Element element) {
        return getSubElementTextContent(element, "hostAndMetric");
    }

    public static String getTestDescription(Element element) {
        NodeList list = element.getElementsByTagName("description");
        Node descriptionElement = list.item(0);
        return getTextContentOrEmptyString(descriptionElement);
    }

    public static int getIntegerValueFromElement(Element element, String subElement) {
        String elementValue = getSubElementValueByTagName(element, subElement);
        try {
            return Integer.parseInt(elementValue);
        } catch (NumberFormatException e) {
            String parentNodeName = element.getNodeName();
            String errorMessage = String.format(EXCEPTION_MESSAGE, subElement, parentNodeName, elementValue);
            throw new NumberFormatException(errorMessage);
        }
    }

    public static double getDoubleValueFromElement(Element element, String subElement) {
        String elementValue = getSubElementValueByTagName(element, subElement);
        try {
            return Double.parseDouble(elementValue);
        } catch (NumberFormatException e) {
            String parentNodeName = element.getNodeName();
            String errorMessage = String.format(EXCEPTION_MESSAGE, subElement, parentNodeName, elementValue);
            throw new NumberFormatException(errorMessage);
        }
    }

    public static int getPercentile(Element element, String subElement) {
        int elementValue = getIntegerValueFromElement(element, subElement);
        if (isPercentile(elementValue)) {
            return elementValue;
        } else {
            String parentNodeName = element.getNodeName();
            String errorMessage = String.format(EXCEPTION_MESSAGE, subElement, parentNodeName, elementValue);
            throw new XMLFilePercentileException(errorMessage);
        }
    }

    public static int getPercentAsInt(Element element, String subElement) {
        int elementValue = getIntegerValueFromElement(element, subElement);
        return Percent.from(elementValue).value();
    }

    private static String getTextContentOrEmptyString(Node node) {
        return (node == null) ? "" : node.getTextContent();
    }

    private static String getSubElementTextContent(Element element, String tagName) {
        NodeList list = element.getElementsByTagName(tagName);
        return list.item(0).getTextContent();
    }

    private static String getSubElementValueByTagName(Element element, String subElement) {
        String elementValue = getNodeByTagName(element, subElement).getTextContent();
        if (elementValue.length() == 0) {
            String parentNodeName = element.getNodeName();
            String errorMessage = String.format("Missing %s value for %s", subElement, parentNodeName);
            throw new XMLFileMissingElementValueException(errorMessage);
        } else {
            return elementValue;
        }
    }

    private static Node getNodeByTagName(Element element, String subElement) {
        NodeList list = element.getElementsByTagName(subElement);
        Node node = list.item(0);
        if (node == null) {
            String errorMessage = String.format("Missing element %s for %s", subElement, element.getNodeName());
            throw new XMLFileMissingElementException(errorMessage);
        } else {
            return node;
        }
    }

    private static boolean isPercentile(int percentile) {
        return (percentile > 0) && (percentile <= 100);
    }
}
