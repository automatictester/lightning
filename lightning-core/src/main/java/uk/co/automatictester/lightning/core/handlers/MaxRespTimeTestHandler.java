package uk.co.automatictester.lightning.core.handlers;

import org.w3c.dom.Element;
import uk.co.automatictester.lightning.core.state.tests.TestSet;
import uk.co.automatictester.lightning.core.tests.RespTimeMaxTest;
import uk.co.automatictester.lightning.core.tests.AbstractClientSideTest;

import java.util.Optional;

import static uk.co.automatictester.lightning.core.utils.DomElements.*;

public class MaxRespTimeTestHandler extends ElementHandler {

    public MaxRespTimeTestHandler(TestSet testSet) {
        this.testSet = testSet;
    }

    @Override
    protected Optional<String> expectedElementName() {
        return Optional.of("maxRespTimeTest");
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
        AbstractClientSideTest test = builder.build();
        testSet.add(test);
    }
}
