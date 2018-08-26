package uk.co.automatictester.lightning.core.s3;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import uk.co.automatictester.lightning.core.config.LightningConfig;
import uk.co.automatictester.lightning.core.exceptions.XMLFileException;
import uk.co.automatictester.lightning.core.s3.client.S3Client;
import uk.co.automatictester.lightning.core.s3.client.S3ClientFlyweightFactory;
import uk.co.automatictester.lightning.core.structures.LightningTests;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class LightningLambdaConfig extends LightningConfig {

    private static S3Client s3Client;

    public LightningLambdaConfig(String region, String bucket) {
        s3Client = S3ClientFlyweightFactory.getInstance(region).setS3Bucket(bucket);
    }

    public void readTests(String xmlObject) {
        LightningTests.flush();
        String xmlObjectContent = s3Client.getObjectAsString(xmlObject);
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
