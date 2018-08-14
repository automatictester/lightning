package uk.co.automatictester.lightning.handlers;

import org.w3c.dom.Element;

public class DefaultHandler extends ElementHandler {

    protected String getExpectedElementName() {
        return "";
    }

    protected void handleHere(Element element) {
        // We follow the existing behaviour of ignoring unknown test types
    }
}
