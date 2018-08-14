package uk.co.automatictester.lightning.handlers;

import org.w3c.dom.Element;
import uk.co.automatictester.lightning.structures.LightningTests;
import uk.co.automatictester.lightning.tests.ClientSideTest;
import uk.co.automatictester.lightning.tests.RespTimeMedianTest;

import static uk.co.automatictester.lightning.utils.LightningConfigProcessingHelper.*;
import static uk.co.automatictester.lightning.utils.LightningConfigProcessingHelper.getTransactionName;
import static uk.co.automatictester.lightning.utils.LightningConfigProcessingHelper.hasRegexp;

public class MedianRespTimeTestHandler extends ElementHandler {

    protected String getExpectedElementName() {
        return "medianRespTimeTest";
    }

    protected void handleHere(Element element) {
        String testName = getTestName(element);
        String description = getTestDescription(element);
        int maxRespTime = getIntegerValueFromElement(element, "maxRespTime");
        RespTimeMedianTest.Builder builder = new RespTimeMedianTest.Builder(testName, maxRespTime).withDescription(description);
        if (hasTransactionName(element)) {
            String transactionName = getTransactionName(element);
            builder.withTransactionName(transactionName);
            if (hasRegexp(element)) {
                builder.withRegexp();
            }
        }
        ClientSideTest respTimeMedianTest = builder.build();
        LightningTests.add(respTimeMedianTest);
    }
}
