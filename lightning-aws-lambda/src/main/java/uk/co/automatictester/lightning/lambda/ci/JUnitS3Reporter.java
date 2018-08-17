package uk.co.automatictester.lightning.lambda.ci;

import uk.co.automatictester.lightning.core.state.TestSet;
import uk.co.automatictester.lightning.core.ci.JUnitReporter;
import uk.co.automatictester.lightning.core.exceptions.JunitReportGenerationException;
import uk.co.automatictester.lightning.core.s3.S3Client;

import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;

public class JUnitS3Reporter extends JUnitReporter {

    private static S3Client s3Client;

    public JUnitS3Reporter(String region, String bucket) {
        s3Client = new S3Client(region, bucket);
    }

    public String generateJUnitReportToS3(TestSet testSet) {
        generateJUnitReportContent(testSet);
        return saveReportToS3();
    }

    private String saveReportToS3() {
        Transformer transformer = getTransformer();
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
