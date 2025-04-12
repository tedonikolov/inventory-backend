package bg.tuvarna.services.impl;

import bg.tuvarna.services.S3Service;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@ApplicationScoped
public class S3ServiceImpl implements S3Service {
    @Inject
    S3Client s3Client;

    public byte[] getFile(String bucketName, String fileName) {
        try {
            if (s3Client.listBuckets().buckets().stream().noneMatch(b -> b.name().equals(bucketName))) {
                throw new RuntimeException("Bucket does not exist: " + bucketName);
            }
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();
            return s3Client.getObject(getObjectRequest).readAllBytes();
        } catch (Exception e) {
            throw new RuntimeException("Error getting file from S3: " + e.getMessage());
        }
    }

    public void uploadToS3(String bucketName, File file, String keyName) throws IOException {
        try (InputStream inputStream = new FileInputStream(file)) {
            if (s3Client.listBuckets().buckets().stream().noneMatch(b -> b.name().equals(bucketName))) {
                s3Client.createBucket(CreateBucketRequest.builder().bucket(bucketName).build());
            }

            s3Client.putObject(
                    PutObjectRequest.builder().bucket(bucketName).key(keyName).build(),
                    RequestBody.fromInputStream(inputStream, file.length())
            );
        }
    }
}
