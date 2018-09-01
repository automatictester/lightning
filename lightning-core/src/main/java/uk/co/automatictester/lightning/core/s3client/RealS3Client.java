package uk.co.automatictester.lightning.core.s3client;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import uk.co.automatictester.lightning.core.s3client.base.S3Client;

public class RealS3Client extends S3Client {

    private RealS3Client(AmazonS3 client) {
        super(client);
    }

    public static RealS3Client createInstance(String region) {
        AmazonS3ClientBuilder amazonS3ClientBuilder = AmazonS3ClientBuilder.standard().withRegion(region);
        AmazonS3 client = amazonS3ClientBuilder.build();
        return new RealS3Client(client);
    }

    @Override
    public String toString() {
        return String.format("Region: %s, bucket: %s", client.getRegionName(), bucket);
    }
}
