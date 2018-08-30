package uk.co.automatictester.lightning.core.handlers;

import org.w3c.dom.Element;
import uk.co.automatictester.lightning.core.structures.LightningTests;
import uk.co.automatictester.lightning.core.tests.base.ClientSideTest;
import uk.co.automatictester.lightning.core.tests.RespTimeMedianTest;

import static uk.co.automatictester.lightning.core.utils.LightningConfigProcessingHelper.*;
import static uk.co.automatictester.lightning.core.utils.LightningConfigProcessingHelper.getTransactionName;
import static uk.co.automatictester.lightning.core.utils.LightningConfigProcessingHelper.hasRegexp;

public class MedianRespTimeTestHandler extends ElementHandler {

    @Override
    protected String getExpectedElementName() {
        return "medianRespTimeTest";
    }

    @Override
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
        ClientSideTest test = builder.build();
        LightningTests.getInstance().add(test);
    }
}
