package uk.co.automatictester.lightning.handlers;

import org.w3c.dom.Element;
import uk.co.automatictester.lightning.config.LightningTests;
import uk.co.automatictester.lightning.tests.ClientSideTest;
import uk.co.automatictester.lightning.tests.PassedTransactionsTest;
import uk.co.automatictester.lightning.utils.Percent;

import static uk.co.automatictester.lightning.utils.LightningConfigProcessingHelper.*;
import static uk.co.automatictester.lightning.utils.LightningConfigProcessingHelper.hasRegexp;

public class PassedTransactionsTestHandler extends ElementHandler {

    protected String getExpectedElementName() {
        return "passedTransactionsTest";
    }

    protected void handleHere(Element element) {
        String testName = getTestName(element);
        String description = getTestDescription(element);
        PassedTransactionsTest.Builder builder;
        if (isSubElementPresent(element, "allowedNumberOfFailedTransactions")) {
            int allowedNumberOfFailedTransactions = getIntegerValueFromElement(element, "allowedNumberOfFailedTransactions");
            builder = new PassedTransactionsTest.Builder(testName, allowedNumberOfFailedTransactions);
        } else {
            int allowedPercentOfFailedTransactions = getPercentAsInt(element, "allowedPercentOfFailedTransactions");
            Percent percent = new Percent(allowedPercentOfFailedTransactions);
            builder = new PassedTransactionsTest.Builder(testName, percent);
        }
        builder.withDescription(description);
        if (hasTransactionName(element)) {
            String transactionName = getTransactionName(element);
            builder.withTransactionName(transactionName);
            if (hasRegexp(element)) {
                builder.withRegexp();
            }
        }
        ClientSideTest passedTransactionsTest = builder.build();
        LightningTests.addClientSideTest(passedTransactionsTest);
    }
}
