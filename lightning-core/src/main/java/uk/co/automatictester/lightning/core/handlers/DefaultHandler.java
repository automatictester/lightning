package uk.co.automatictester.lightning.core.handlers;

import org.w3c.dom.Element;

import java.util.Optional;

public class DefaultHandler extends ElementHandler {

    @Override
    protected Optional<String> expectedElementName() {
        return Optional.empty();
    }

    @Override
    protected void handleHere(Element element) {
        // We follow the existing behaviour of ignoring unknown test types
    }
}
