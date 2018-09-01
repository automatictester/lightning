package uk.co.automatictester.lightning.core.reporters.junit;

import uk.co.automatictester.lightning.core.reporters.junit.base.AbstractJunitReporter;
import uk.co.automatictester.lightning.core.exceptions.JunitReportGenerationException;
import uk.co.automatictester.lightning.core.s3client.base.S3Client;
import uk.co.automatictester.lightning.core.s3client.factory.S3ClientFlyweightFactory;
import uk.co.automatictester.lightning.core.state.tests.results.LightningTestSetResult;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;

public class JunitS3Reporter extends AbstractJunitReporter {

    private static S3Client s3Client;

    public JunitS3Reporter(String region, String bucket) {
        s3Client = S3ClientFlyweightFactory.getInstance(region).setBucket(bucket);
    }

    public String generateJUnitReportToS3(LightningTestSetResult testSet) {
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
        return s3Client.putObject("output/junit.xml", junitReport);
    }
}
