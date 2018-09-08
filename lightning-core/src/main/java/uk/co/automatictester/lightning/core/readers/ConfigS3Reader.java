package uk.co.automatictester.lightning.core.readers;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import uk.co.automatictester.lightning.core.exceptions.XMLFileException;
import uk.co.automatictester.lightning.core.s3client.base.S3Client;
import uk.co.automatictester.lightning.core.s3client.factory.S3ClientFlyweightFactory;
import uk.co.automatictester.lightning.core.state.tests.LightningTestSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ConfigS3Reader extends ConfigReader {

    private ConfigS3Reader() {
    }

    public static void readTests(String region, String bucket, String key) {
        S3Client client = S3ClientFlyweightFactory.getInstance(region).setBucket(bucket);
        LightningTestSet.getInstance().flush();
        String xmlObjectContent = client.getObjectAsString(key);
        NodeList nodes = readXmlFile(xmlObjectContent);
        loadAllTests(nodes);
        throwExceptionIfNoTests();
    }

    private static NodeList readXmlFile(String xmlObjectContent) {
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
