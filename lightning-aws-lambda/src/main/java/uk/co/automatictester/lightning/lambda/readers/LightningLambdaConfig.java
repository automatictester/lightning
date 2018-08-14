package uk.co.automatictester.lightning.lambda.readers;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import uk.co.automatictester.lightning.config.LightningConfig;
import uk.co.automatictester.lightning.exceptions.XMLFileException;
import uk.co.automatictester.lightning.s3.S3Client;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class LightningLambdaConfig extends LightningConfig {

    private static S3Client s3Client;

    public LightningLambdaConfig(String region, String bucket) {
        s3Client = new S3Client(region, bucket);
    }

    public void readTests(String xmlObject) {
        String xmlObjectContent = s3Client.getS3ObjectContent(xmlObject);
        NodeList nodes = readXmlFile(xmlObjectContent);
        loadAllTests(nodes);
        throwExceptionIfNoTests();
    }

    private NodeList readXmlFile(String xmlObjectContent) {
        try (InputStream is = new ByteArrayInputStream(xmlObjectContent.getBytes())) {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(is);
            Node node = doc.getDocumentElement();
            return node.getChildNodes();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new XMLFileException(e);
        }
    }
}
