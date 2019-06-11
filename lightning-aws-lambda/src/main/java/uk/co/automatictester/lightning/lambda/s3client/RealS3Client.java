package uk.co.automatictester.lightning.lambda.s3client;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

public class RealS3Client extends AbstractS3Client {

    private RealS3Client(AmazonS3 client) {
        super(client);
    }

    public static RealS3Client createInstance(String region) {
        AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard().withRegion(region).build();
        return new RealS3Client(amazonS3);
    }

    @Override
    public String toString() {
        return String.format("Region: %s, bucket: %s", CLIENT.getRegionName(), bucket);
    }
}
