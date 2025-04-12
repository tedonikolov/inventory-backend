package bg.tuvarna.services.producers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Produces;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

@ApplicationScoped
public class S3ClientProducer {
    @ConfigProperty(name = "quarkus.s3.endpoint")
    String url;
    @ConfigProperty(name = "quarkus.s3.aws.credentials.access-key-id")
    String accessKey;
    @ConfigProperty(name = "quarkus.s3.aws.credentials.secret-access-key")
    String secretKey;
    @ConfigProperty(name = "quarkus.s3.aws.region")
    String region;

    @Produces
    @ApplicationScoped
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
                .endpointOverride(URI.create(url))
                .forcePathStyle(true)
                .build();
    }
}
