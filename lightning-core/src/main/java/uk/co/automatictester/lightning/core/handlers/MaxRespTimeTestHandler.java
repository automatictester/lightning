package uk.co.automatictester.lightning.core.handlers;

import org.w3c.dom.Element;
import uk.co.automatictester.lightning.core.structures.LightningTests;
import uk.co.automatictester.lightning.core.tests.base.ClientSideTest;
import uk.co.automatictester.lightning.core.tests.RespTimeMaxTest;

import static uk.co.automatictester.lightning.core.utils.LightningConfigProcessingHelper.*;
import static uk.co.automatictester.lightning.core.utils.LightningConfigProcessingHelper.getTransactionName;
import static uk.co.automatictester.lightning.core.utils.LightningConfigProcessingHelper.hasRegexp;

public class MaxRespTimeTestHandler extends ElementHandler {

    @Override
    protected String getExpectedElementName() {
        return "maxRespTimeTest";
    }

    @Override
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
