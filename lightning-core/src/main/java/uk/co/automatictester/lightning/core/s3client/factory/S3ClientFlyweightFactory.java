package uk.co.automatictester.lightning.core.s3client.factory;

import uk.co.automatictester.lightning.core.s3client.MockedS3Client;
import uk.co.automatictester.lightning.core.s3client.RealS3Client;
import uk.co.automatictester.lightning.core.s3client.S3Client;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class S3ClientFlyweightFactory {

    private static final Map<String, S3Client> instances = new HashMap<>();

    private S3ClientFlyweightFactory() {
    }

    public static synchronized S3Client getInstance(String region) {
        if (isMockedInstanceExpected()) {
            return getMockedInstance(region);
        } else {
            return getRealInstance(region);
        }
    }

    private static boolean isMockedInstanceExpected() {
        return System.getProperty("mockS3") != null;
    }

    private static S3Client getMockedInstance(String region) {
        Supplier<S3Client> supplier = () -> MockedS3Client.createInstance(region);
        return getInstance(region, supplier);
    }

    private static S3Client getRealInstance(String region) {
        Supplier<S3Client> supplier = () -> RealS3Client.createInstance(region);
        return getInstance(region, supplier);
    }

    private static S3Client getInstance(String region, Supplier<S3Client> supplier) {
        if (!hasInstance(region)) {
            S3Client instance = supplier.get();
            putInstance(region, instance);
        }
        return getExistingInstance(region);
    }

    private static boolean hasInstance(String region) {
        return instances.containsKey(region);
    }

    private static void putInstance(String region, S3Client instance) {
        instances.put(region, instance);
    }

    private static S3Client getExistingInstance(String region) {
        return instances.get(region);
    }
}
