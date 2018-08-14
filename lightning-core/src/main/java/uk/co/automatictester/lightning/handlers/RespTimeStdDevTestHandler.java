package uk.co.automatictester.lightning.handlers;

import org.w3c.dom.Element;
import uk.co.automatictester.lightning.config.LightningTests;
import uk.co.automatictester.lightning.tests.ClientSideTest;
import uk.co.automatictester.lightning.tests.RespTimeStdDevTest;

import static uk.co.automatictester.lightning.utils.LightningConfigProcessingHelper.*;

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
        LightningTests.addClientSideTest(test);
    }
}
