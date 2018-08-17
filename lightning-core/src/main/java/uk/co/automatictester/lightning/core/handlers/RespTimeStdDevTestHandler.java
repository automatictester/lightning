package uk.co.automatictester.lightning.core.handlers;

import org.w3c.dom.Element;
import uk.co.automatictester.lightning.core.structures.LightningTests;
import uk.co.automatictester.lightning.core.tests.base.ClientSideTest;
import uk.co.automatictester.lightning.core.tests.RespTimeStdDevTest;

import static uk.co.automatictester.lightning.core.utils.LightningConfigProcessingHelper.*;

public class RespTimeStdDevTestHandler extends ElementHandler {

    protected String getExpectedElementName() {
        return "respTimeStdDevTest";
    }

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
        ClientSideTest test = builder.build();
        LightningTests.add(test);
    }
}
