package uk.co.automatictester.lightning.core.reporters.junit;

import uk.co.automatictester.lightning.core.exceptions.JunitReportGenerationException;
import uk.co.automatictester.lightning.core.reporters.junit.base.AbstractJunitReporter;
import uk.co.automatictester.lightning.core.state.tests.TestSet;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class JunitReporter extends AbstractJunitReporter {

    public void generateJunitReport(TestSet testSet) {
        generateJUnitReportContent(testSet);
        saveReportToDisk();
    }

    private void saveReportToDisk() {
        Transformer transformer = transformer();
        DOMSource source = new DOMSource(doc);

        File junitReport = new File("junit.xml");
        StreamResult file = new StreamResult(junitReport);
        try {
            transformer.transform(source, file);
        } catch (TransformerException e) {
            throw new JunitReportGenerationException(e);
        }
    }
}
