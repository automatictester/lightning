package uk.co.automatictester.lightning.core.handlers;

import org.w3c.dom.Element;

public class DefaultHandler extends ElementHandler {

    @Override
    protected String getExpectedElementName() {
        return "";
    }

    @Override
    protected void handleHere(Element element) {
        // We follow the existing behaviour of ignoring unknown test types
    }
}
