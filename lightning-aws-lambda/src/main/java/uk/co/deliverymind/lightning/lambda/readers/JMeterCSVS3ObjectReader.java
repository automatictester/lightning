package uk.co.deliverymind.lightning.lambda.readers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.co.deliverymind.lightning.data.JMeterTransactions;
import uk.co.deliverymind.lightning.exceptions.CSVFileIOException;
import uk.co.deliverymind.lightning.exceptions.CSVFileNoTransactionsException;
import uk.co.deliverymind.lightning.lambda.s3.S3Client;
import uk.co.deliverymind.lightning.readers.JMeterCSVFileReader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class JMeterCSVS3ObjectReader extends JMeterCSVFileReader {

    private static final Logger log = LogManager.getLogger(JMeterCSVS3ObjectReader.class);
    private static S3Client s3Client;

    public JMeterCSVS3ObjectReader(String region, String bucket) {
        s3Client = new S3Client(region, bucket);
    }

    public JMeterTransactions getTransactions(String csvObject) {
        long start = System.currentTimeMillis();
        log.debug("Reading CSV file - start");
        JMeterTransactions jmeterTransactions = new JMeterTransactions();
        String csvObjectContent = s3Client.getS3ObjectContent(csvObject);

        try (InputStreamReader isr = new InputStreamReader(new ByteArrayInputStream(csvObjectContent.getBytes()))) {
            jmeterTransactions.addAll(getParser().parseAll(isr));
        } catch (IOException e) {
            throw new CSVFileIOException(e);
        }

        if (jmeterTransactions.isEmpty()) {
            throw new CSVFileNoTransactionsException();
        }

        long finish = System.currentTimeMillis();
        long millisecondsBetween = finish - start;
        log.debug("Reading CSV file - finish, read {} rows, took {}ms", jmeterTransactions.size(), millisecondsBetween);

        return jmeterTransactions;
    }
}
