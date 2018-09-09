package uk.co.automatictester.lightning.core.handlers;

import org.w3c.dom.Element;
import uk.co.automatictester.lightning.core.enums.ServerSideTestType;
import uk.co.automatictester.lightning.core.state.tests.TestSet;
import uk.co.automatictester.lightning.core.tests.ServerSideBetweenTest;
import uk.co.automatictester.lightning.core.tests.ServerSideGreaterThanTest;
import uk.co.automatictester.lightning.core.tests.ServerSideLessThanTest;
import uk.co.automatictester.lightning.core.tests.AbstractServerSideTest;

import java.util.Optional;

import static uk.co.automatictester.lightning.core.utils.DomElements.*;

public class ServerSideTestHandler extends ElementHandler {

    public ServerSideTestHandler(TestSet testSet) {
        this.testSet = testSet;
    }

    @Override
    protected Optional<String> expectedElementName() {
        return Optional.of("serverSideTest");
    }

    @Override
    protected void handleHere(Element element) {
        String testName = getTestName(element);
        String description = getTestDescription(element);
        AbstractServerSideTest test = null;
        ServerSideTestType subType = getSubType(element);

        switch (subType) {
            case BETWEEN:
                int lowerThreshold = getIntegerValueFromElement(element, "metricValueA");
                int upperThreshold = getIntegerValueFromElement(element, "metricValueB");
                ServerSideBetweenTest.Builder betweenBuilder = new ServerSideBetweenTest.Builder(testName, lowerThreshold, upperThreshold);
                betweenBuilder.withDescription(description);
                if (hasHostAndMetric(element)) {
                    String hostAndMetric = getHostAndMetric(element);
                    betweenBuilder.withHostAndMetric(hostAndMetric);
                }
                test = betweenBuilder.build();
                break;
            case GREATER_THAN:
                int greatherThanThreshold = getIntegerValueFromElement(element, "metricValueA");
                ServerSideGreaterThanTest.Builder greatherThanBuilder = new ServerSideGreaterThanTest.Builder(testName, greatherThanThreshold);
                greatherThanBuilder.withDescription(description);
                if (hasHostAndMetric(element)) {
                    String hostAndMetric = getHostAndMetric(element);
                    greatherThanBuilder.withHostAndMetric(hostAndMetric);
                }
                test = greatherThanBuilder.build();
                break;
            case LESS_THAN:
                int lessThanThreshold = getIntegerValueFromElement(element, "metricValueA");
                ServerSideLessThanTest.Builder lessThanBuilder = new ServerSideLessThanTest.Builder(testName, lessThanThreshold);
                lessThanBuilder.withDescription(description);
                if (hasHostAndMetric(element)) {
                    String hostAndMetric = getHostAndMetric(element);
                    lessThanBuilder.withHostAndMetric(hostAndMetric);
                }
                test = lessThanBuilder.build();
                break;
        }

        testSet.add(test);
    }
}
