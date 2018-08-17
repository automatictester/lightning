package uk.co.automatictester.lightning.core.handlers;

import org.w3c.dom.Element;
import uk.co.automatictester.lightning.core.structures.LightningTests;
import uk.co.automatictester.lightning.core.tests.base.ClientSideTest;
import uk.co.automatictester.lightning.core.tests.PassedTransactionsAbsoluteTest;
import uk.co.automatictester.lightning.core.tests.PassedTransactionsRelativeTest;

import static uk.co.automatictester.lightning.core.utils.LightningConfigProcessingHelper.*;

public class PassedTransactionsTestHandler extends ElementHandler {

    protected String getExpectedElementName() {
        return "passedTransactionsTest";
    }

    protected void handleHere(Element element) {
        String testName = getTestName(element);
        String description = getTestDescription(element);
        ClientSideTest test;
        boolean isAbsolute = isSubElementPresent(element, "allowedNumberOfFailedTransactions");

        if (isAbsolute) {
            int allowedNumberOfFailedTransactions = getIntegerValueFromElement(element, "allowedNumberOfFailedTransactions");
            PassedTransactionsAbsoluteTest.Builder builder = new PassedTransactionsAbsoluteTest.Builder(testName, allowedNumberOfFailedTransactions);
            builder.withDescription(description);
            if (hasTransactionName(element)) {
                String transactionName = getTransactionName(element);
                builder.withTransactionName(transactionName);
                if (hasRegexp(element)) {
                    builder.withRegexp();
                }
            }
            test = builder.build();
        } else {
            int percent = getPercentAsInt(element, "allowedPercentOfFailedTransactions");
            PassedTransactionsRelativeTest.Builder builder = new PassedTransactionsRelativeTest.Builder(testName, percent);
            builder.withDescription(description);
            if (hasTransactionName(element)) {
                String transactionName = getTransactionName(element);
                builder.withTransactionName(transactionName);
                if (hasRegexp(element)) {
                    builder.withRegexp();
                }
            }
            test = builder.build();
        }

        LightningTests.add(test);
    }
}
