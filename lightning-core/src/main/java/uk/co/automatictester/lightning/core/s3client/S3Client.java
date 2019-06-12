package uk.co.automatictester.lightning.core.s3client;

import java.io.IOException;

public interface S3Client {

    S3Client setBucket(String bucket);

    String getObjectAsString(String key);

    String putObject(String key, String content);

    void putObjectFromFile(String file) throws IOException;

    boolean createBucketIfDoesNotExist(String bucket);
}
