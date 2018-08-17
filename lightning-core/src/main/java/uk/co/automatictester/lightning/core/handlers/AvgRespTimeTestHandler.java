package uk.co.automatictester.lightning.core.handlers;

import org.w3c.dom.Element;
import uk.co.automatictester.lightning.core.structures.LightningTests;
import uk.co.automatictester.lightning.core.tests.base.ClientSideTest;
import uk.co.automatictester.lightning.core.tests.RespTimeAvgTest;

import static uk.co.automatictester.lightning.core.utils.LightningConfigProcessingHelper.*;

public class AvgRespTimeTestHandler extends ElementHandler {

    protected String getExpectedElementName() {
        return "avgRespTimeTest";
    }

    protected void handleHere(Element element) {
        String testName = getTestName(element);
        String description = getTestDescription(element);
        int maxAvgRespTime = getIntegerValueFromElement(element, "maxAvgRespTime");
        RespTimeAvgTest.Builder builder = new RespTimeAvgTest.Builder(testName, maxAvgRespTime).withDescription(description);
        if (hasTransactionName(element)) {
            String transactionName = getTransactionName(element);
            builder.withTransactionName(transactionName);
            if (hasRegexp(element)) {
                builder.withRegexp();
            }
        }
        ClientSideTest test = builder.build();
        LightningTests.add(test);
    }
}
