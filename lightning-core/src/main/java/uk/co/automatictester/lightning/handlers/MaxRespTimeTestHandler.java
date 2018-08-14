package uk.co.automatictester.lightning.handlers;

import org.w3c.dom.Element;
import uk.co.automatictester.lightning.structures.LightningTests;
import uk.co.automatictester.lightning.tests.ClientSideTest;
import uk.co.automatictester.lightning.tests.RespTimeMaxTest;

import static uk.co.automatictester.lightning.utils.LightningConfigProcessingHelper.*;
import static uk.co.automatictester.lightning.utils.LightningConfigProcessingHelper.getTransactionName;
import static uk.co.automatictester.lightning.utils.LightningConfigProcessingHelper.hasRegexp;

public class MaxRespTimeTestHandler extends ElementHandler {

    protected String getExpectedElementName() {
        return "maxRespTimeTest";
    }

    protected void handleHere(Element element) {
        String testName = getTestName(element);
        String description = getTestDescription(element);
        int maxRespTime = getIntegerValueFromElement(element, "maxAllowedRespTime");
        RespTimeMaxTest.Builder builder = new RespTimeMaxTest.Builder(testName, maxRespTime).withDescription(description);
        if (hasTransactionName(element)) {
            String transactionName = getTransactionName(element);
            builder.withTransactionName(transactionName);
            if (hasRegexp(element)) {
                builder.withRegexp();
            }
        }
        ClientSideTest maxRespTimeTest = builder.build();
        LightningTests.add(maxRespTimeTest);
    }
}
