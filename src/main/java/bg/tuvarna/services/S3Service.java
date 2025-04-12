package bg.tuvarna.services;

import java.io.File;
import java.io.IOException;

public interface S3Service {
    void uploadToS3(String bucketName, File file, String keyName) throws IOException;

    byte[] getFile(String bucketName, String fileName);
}
