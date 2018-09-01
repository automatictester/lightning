package uk.co.automatictester.lightning.core.s3.client;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

public class MockedS3Client extends S3Client {

    private static final String S3_MOCK_URL = "http://localhost:8001";

    private MockedS3Client(AmazonS3 client) {
        super(client);
    }

    static MockedS3Client createInstance(String region) {
        AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(S3_MOCK_URL, region);
        AmazonS3 client = AmazonS3ClientBuilder
                .standard()
                .withPathStyleAccessEnabled(true)
                .withEndpointConfiguration(endpointConfiguration)
                .build();
        return new MockedS3Client(client);
    }
}
