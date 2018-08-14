package uk.co.automatictester.lightning.handlers;

import org.w3c.dom.Element;
import uk.co.automatictester.lightning.config.LightningTests;
import uk.co.automatictester.lightning.enums.ServerSideTestType;
import uk.co.automatictester.lightning.tests.ServerSideTest;

import static uk.co.automatictester.lightning.utils.LightningConfigProcessingHelper.*;

public class ServerSideTestHandler extends ElementHandler {

    protected String getExpectedElementName() {
        return "serverSideTest";
    }

    protected void handleHere(Element element) {
        String name = getTestName(element);
        ServerSideTestType subType = getSubType(element);
        int metricValueA = getIntegerValueFromElement(element, "metricValueA");
        ServerSideTest.Builder builder;
        if (subType.name().equals(ServerSideTestType.BETWEEN.name())) {
            int avgRespTimeB = getIntegerValueFromElement(element, "metricValueB");
            builder = new ServerSideTest.Builder(name, subType, metricValueA, avgRespTimeB);
        } else {
            builder = new ServerSideTest.Builder(name, subType, metricValueA);
        }
        if (hasHostAndMetric(element)) {
            String hostAndMetric = getHostAndMetric(element);
            builder.withHostAndMetric(hostAndMetric);
        }
        String description = getTestDescription(element);
        builder.withDescription(description);
        ServerSideTest serverSideTest = builder.build();
        LightningTests.addServerSideTest(serverSideTest);
    }
}
