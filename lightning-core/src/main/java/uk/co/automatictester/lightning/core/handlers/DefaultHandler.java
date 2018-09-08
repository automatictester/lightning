package uk.co.automatictester.lightning.core.handlers;

import org.w3c.dom.Element;
import uk.co.automatictester.lightning.core.state.tests.LightningTestSet;

import java.util.Optional;

public class DefaultHandler extends ElementHandler {

    public DefaultHandler(LightningTestSet testSet) {
        this.testSet = testSet;
    }

    @Override
    protected Optional<String> expectedElementName() {
        return Optional.empty();
    }

    @Override
    protected void handleHere(Element element) {
        // We follow the existing behaviour of ignoring unknown test types
    }
}
