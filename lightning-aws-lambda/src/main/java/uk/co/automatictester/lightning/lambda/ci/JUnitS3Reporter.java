package uk.co.automatictester.lightning.lambda.ci;

import org.w3c.dom.Node;
import uk.co.automatictester.lightning.TestSet;
import uk.co.automatictester.lightning.ci.JUnitReporter;
import uk.co.automatictester.lightning.exceptions.JunitReportGenerationException;
import uk.co.automatictester.lightning.lambda.s3.S3Client;
import uk.co.automatictester.lightning.tests.LightningTest;

import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;

public class JUnitS3Reporter extends JUnitReporter {

    private static S3Client s3Client;

    public JUnitS3Reporter(String region, String bucket) {
        s3Client = new S3Client(region, bucket);
    }

    public void setTestSet(TestSet testSet) {
        this.testSet = testSet;
    }

    public String generateJUnitReportToS3() {
        Node rootElement = doc.appendChild(getTestsuite());
        for (LightningTest test : testSet.getTests()) {
            rootElement.appendChild(getTestcase(test));
        }
        return saveReportToS3();
    }

    private String saveReportToS3() {
        Transformer transformer;
        try {
            transformer = TransformerFactory.newInstance().newTransformer();
        } catch (TransformerConfigurationException e) {
            throw new JunitReportGenerationException(e);
        }
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        DOMSource source = new DOMSource(doc);

        StringWriter writer = new StringWriter();
        try {
            transformer.transform(source, new StreamResult(writer));
        } catch (TransformerException e) {
            throw new JunitReportGenerationException(e);
        }
        String junitReport = writer.toString();
        return s3Client.putS3Object("output/junit.xml", junitReport);
    }
}
