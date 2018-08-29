package uk.co.automatictester.lightning.core.handlers;

import org.w3c.dom.Element;
import uk.co.automatictester.lightning.core.structures.LightningTests;
import uk.co.automatictester.lightning.core.tests.base.ClientSideTest;
import uk.co.automatictester.lightning.core.tests.RespTimeNthPercentileTest;

import static uk.co.automatictester.lightning.core.utils.LightningConfigProcessingHelper.*;

public class NthPercRespTimeTestHandler extends ElementHandler {

    @Override
    protected String getExpectedElementName() {
        return "nthPercRespTimeTest";
    }

    @Override
    protected void handleHere(Element element) {
        String testName = getTestName(element);
        int maxRespTime = getIntegerValueFromElement(element, "maxRespTime");
        int percentile = getPercentile(element, "percentile");
        String description = getTestDescription(element);
        RespTimeNthPercentileTest.Builder builder = new RespTimeNthPercentileTest.Builder(testName, maxRespTime, percentile).withDescription(description);
        if (hasTransactionName(element)) {
            String transactionName = getTransactionName(element);
            builder.withTransactionName(transactionName);
            if (hasRegexp(element)) {
                builder.withRegexp();
            }
        }
        ClientSideTest nthPercRespTimeTest = builder.build();
        LightningTests.add(nthPercRespTimeTest);
    }
}
