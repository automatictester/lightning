package uk.co.automatictester.lightning.core.handlers;

import org.w3c.dom.Element;
import uk.co.automatictester.lightning.core.state.tests.TestSet;
import uk.co.automatictester.lightning.core.tests.RespTimeStdDevTest;
import uk.co.automatictester.lightning.core.tests.AbstractClientSideTest;

import java.util.Optional;

import static uk.co.automatictester.lightning.core.utils.DomElements.*;

public class RespTimeStdDevTestHandler extends ElementHandler {

    public RespTimeStdDevTestHandler(TestSet testSet) {
        this.testSet = testSet;
    }

    @Override
    protected Optional<String> expectedElementName() {
        return Optional.of("respTimeStdDevTest");
    }

    @Override
    protected void handleHere(Element element) {
        String testName = getTestName(element);
        String description = getTestDescription(element);
        int maxRespTimeStdDevTime = getIntegerValueFromElement(element, "maxRespTimeStdDev");
        RespTimeStdDevTest.Builder builder = new RespTimeStdDevTest.Builder(testName, maxRespTimeStdDevTime).withDescription(description);
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
