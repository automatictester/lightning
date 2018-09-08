package uk.co.automatictester.lightning.core.handlers;

import org.w3c.dom.Element;
import uk.co.automatictester.lightning.core.state.tests.LightningTestSet;
import uk.co.automatictester.lightning.core.tests.RespTimeMedianTest;
import uk.co.automatictester.lightning.core.tests.base.AbstractClientSideTest;

import java.util.Optional;

import static uk.co.automatictester.lightning.core.utils.DomElements.*;

public class MedianRespTimeTestHandler extends ElementHandler {

    @Override
    protected Optional<String> expectedElementName() {
        return Optional.of("medianRespTimeTest");
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
        AbstractClientSideTest test = builder.build();
        LightningTestSet.getInstance().add(test);
    }
}
