package uk.co.automatictester.lightning.core.config;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import uk.co.automatictester.lightning.core.handlers.*;
import uk.co.automatictester.lightning.core.state.tests.TestSet;

public abstract class AbstractConfigReader implements ConfigReader {

    protected TestSet testSet = new TestSet();

    public void loadAllTests(NodeList nodes) {
        for (int i = 0; i < nodes.getLength(); i++) {
            if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) nodes.item(i);

                AvgRespTimeTestHandler avgRespTimeTestHandler = new AvgRespTimeTestHandler(testSet);
                RespTimeStdDevTestHandler respTimeStdDevTestHandler = new RespTimeStdDevTestHandler(testSet);
                PassedTransactionsTestHandler passedTransactionsTestHandler = new PassedTransactionsTestHandler(testSet);
                NthPercRespTimeTestHandler nthPercRespTimeTestHandler = new NthPercRespTimeTestHandler(testSet);
                ThroughputTestHandler throughputTestHandler = new ThroughputTestHandler(testSet);
                MaxRespTimeTestHandler maxRespTimeTestHandler = new MaxRespTimeTestHandler(testSet);
                MedianRespTimeTestHandler medianRespTimeTestHandler = new MedianRespTimeTestHandler(testSet);
                ServerSideTestHandler serverSideTestHandler = new ServerSideTestHandler(testSet);
                DefaultHandler defaultHandler = new DefaultHandler(testSet);

                avgRespTimeTestHandler.setNextHandler(respTimeStdDevTestHandler);
                respTimeStdDevTestHandler.setNextHandler(passedTransactionsTestHandler);
                passedTransactionsTestHandler.setNextHandler(nthPercRespTimeTestHandler);
                nthPercRespTimeTestHandler.setNextHandler(throughputTestHandler);
                throughputTestHandler.setNextHandler(maxRespTimeTestHandler);
                maxRespTimeTestHandler.setNextHandler(medianRespTimeTestHandler);
                medianRespTimeTestHandler.setNextHandler(serverSideTestHandler);
                serverSideTestHandler.setNextHandler(defaultHandler);

                avgRespTimeTestHandler.processHandler(element);
            }
        }
    }

    protected void throwExceptionIfNoTests() {
        if (testSet.size() == 0) {
            throw new IllegalStateException("No tests of expected type found in XML file");
        }
    }
}
