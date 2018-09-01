package uk.co.automatictester.lightning.core.handlers;

import org.w3c.dom.Element;
import uk.co.automatictester.lightning.core.state.tests.LightningTestSet;
import uk.co.automatictester.lightning.core.tests.base.AbstractClientSideTest;
import uk.co.automatictester.lightning.core.tests.ThroughputTest;

import static uk.co.automatictester.lightning.core.utils.DomElementProcessor.*;
import static uk.co.automatictester.lightning.core.utils.DomElementProcessor.getTransactionName;
import static uk.co.automatictester.lightning.core.utils.DomElementProcessor.hasRegexp;

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
        AbstractClientSideTest test = builder.build();
        LightningTestSet.getInstance().add(test);
    }
}
