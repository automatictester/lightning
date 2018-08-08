package uk.co.automatictester.lightning.lambda.readers;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import uk.co.automatictester.lightning.exceptions.XMLFileException;
import uk.co.automatictester.lightning.readers.LightningXMLFileReader;
import uk.co.automatictester.lightning.s3.S3Client;

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
        Document doc = readXmlFile(xmlObjectContent);
        loadAllTests(doc);
        throwExceptionIfNoTests();
    }

    private Document readXmlFile(String xmlObjectContent) {
        try (InputStream is = new ByteArrayInputStream(xmlObjectContent.getBytes())) {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(is);
            doc.getDocumentElement().normalize();
            return doc;
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new XMLFileException(e);
        }
    }
}
