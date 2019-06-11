package uk.co.automatictester.lightning.lambda.s3client;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

public class MockedS3Client extends AbstractS3Client {

    private MockedS3Client(AmazonS3 client) {
        super(client);
    }

    public static MockedS3Client createInstance(String region) {
        String s3MockUrl = getMockedUrl();
        AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(s3MockUrl, region);
        AmazonS3 amazonS3 = AmazonS3ClientBuilder
                .standard()
                .withPathStyleAccessEnabled(true)
                .withEndpointConfiguration(endpointConfiguration)
                .build();
        return new MockedS3Client(amazonS3);
    }

    static String getMockedUrl() {
        return String.format("http://localhost:%s", getMockedPort());
    }

    static String getMockedPort() {
        return System.getProperty("mockS3Port", "8001");
    }

    @Override
    public String toString() {
        return String.format("URL: %s, bucket: %s", getMockedUrl(), bucket);
    }
}
