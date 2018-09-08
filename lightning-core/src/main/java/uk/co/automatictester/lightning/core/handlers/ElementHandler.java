package uk.co.automatictester.lightning.core.handlers;

import org.w3c.dom.Element;
import uk.co.automatictester.lightning.core.state.tests.LightningTestSet;

import java.util.Optional;

public abstract class ElementHandler {

    private ElementHandler nextHandler;
    protected LightningTestSet testSet;

    public void setNextHandler(ElementHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    public void processHandler(Element element) {
        String elementName = element.getTagName();
        Optional<String> expectedElementName = expectedElementName();
        if (!expectedElementName.isPresent() || elementName.equals(expectedElementName.get())) {
            handleHere(element);
        } else {
            nextHandler.processHandler(element);
        }
    }

    protected abstract Optional<String> expectedElementName();

    protected abstract void handleHere(Element element);
}
