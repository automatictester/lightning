package uk.co.automatictester.lightning.core.handlers;

import org.w3c.dom.Element;
import uk.co.automatictester.lightning.core.state.tests.LightningTestSet;
import uk.co.automatictester.lightning.core.tests.base.AbstractClientSideTest;
import uk.co.automatictester.lightning.core.tests.RespTimeNthPercentileTest;

import java.util.Optional;

import static uk.co.automatictester.lightning.core.utils.DomElements.*;

public class NthPercRespTimeTestHandler extends ElementHandler {

    @Override
    protected Optional<String> expectedElementName() {
        return Optional.of("nthPercRespTimeTest");
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
        AbstractClientSideTest test = builder.build();
        LightningTestSet.getInstance().add(test);
    }
}
