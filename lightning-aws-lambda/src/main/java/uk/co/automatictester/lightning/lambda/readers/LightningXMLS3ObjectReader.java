package uk.co.automatictester.lightning.lambda.readers;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import uk.co.automatictester.lightning.exceptions.XMLFileException;
import uk.co.automatictester.lightning.exceptions.XMLFileNoTestsException;
import uk.co.automatictester.lightning.lambda.s3.S3Client;
import uk.co.automatictester.lightning.readers.LightningXMLFileReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class LightningXMLS3ObjectReader extends LightningXMLFileReader {

    private static S3Client s3Client;

    public LightningXMLS3ObjectReader(String region, String bucket) {
        s3Client = new S3Client(region, bucket);
    }

    public void readTests(String xmlObject) {
        String xmlObjectContent = s3Client.getS3ObjectContent(xmlObject);

        try (InputStream is = new ByteArrayInputStream(xmlObjectContent.getBytes())) {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(is);
            doc.getDocumentElement().normalize();

            addRespTimeAvgTests(doc);
            addRespTimeStdDevTestNodes(doc);
            addPassedTransactionsTestNodes(doc);
            addRespTimeNthPercTests(doc);
            addThroughputTests(doc);
            addRespTimeMaxTests(doc);
            addRespTimeMedianTests(doc);
            addServerSideTests(doc);

        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new XMLFileException(e);
        }

        if (getTestCount() == 0) {
            throw new XMLFileNoTestsException("No tests of expected type found in XML file");
        }
    }
}
