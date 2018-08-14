package uk.co.automatictester.lightning.handlers;

import org.w3c.dom.Element;

import static org.apache.commons.lang3.StringUtils.isBlank;

public abstract class ElementHandler {

    private ElementHandler nextHandler;

    public void setNextHandler(ElementHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    public void processHandler(Element element) {
        String elementName = element.getTagName();
        String expectedElementName = getExpectedElementName();
        if (isBlank(expectedElementName) || elementName.equals(expectedElementName)) {
            handleHere(element);
        } else {
            nextHandler.processHandler(element);
        }
    }

    protected abstract String getExpectedElementName();

    protected abstract void handleHere(Element element);
}
