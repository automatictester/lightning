package uk.co.automatictester.lightning.core.handlers;

import org.w3c.dom.Element;
import uk.co.automatictester.lightning.core.structures.LightningTests;
import uk.co.automatictester.lightning.core.tests.base.ClientSideTest;
import uk.co.automatictester.lightning.core.tests.ThroughputTest;

import static uk.co.automatictester.lightning.core.utils.LightningConfigProcessingHelper.*;
import static uk.co.automatictester.lightning.core.utils.LightningConfigProcessingHelper.getTransactionName;
import static uk.co.automatictester.lightning.core.utils.LightningConfigProcessingHelper.hasRegexp;

public class ThroughputTestHandler extends ElementHandler {

    @Override
    protected String getExpectedElementName() {
        return "throughputTest";
    }

    @Override
    protected void handleHere(Element element) {
        String testName = getTestName(element);
        String description = getTestDescription(element);
        double minThroughput = getDoubleValueFromElement(element, "minThroughput");
        ThroughputTest.Builder builder = new ThroughputTest.Builder(testName, minThroughput).withDescription(description);
        if (hasTransactionName(element)) {
            String transactionName = getTransactionName(element);
            builder.withTransactionName(transactionName);
            if (hasRegexp(element)) {
                builder.withRegexp();
            }
        }
        ClientSideTest throughputTest = builder.build();
        LightningTests.add(throughputTest);
    }
}
