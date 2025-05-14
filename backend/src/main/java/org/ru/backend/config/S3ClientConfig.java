package org.ru.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;

import java.net.URI;

@Configuration
class S3ClientConfig{
    @Value("${s3.region}")
    private String region;
    @Value("${s3.url}")
    private String url;
    @Value("${s3.accessKeyId}")
    private String accessKeyId;
    @Value("${s3.secretAccessKey}")
    private String  secretAccessKey;

    @Bean
    public S3Client s3Client() {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKeyId, secretAccessKey);

        S3ClientBuilder builder = S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .region(Region.of(region));

        if (!url.isEmpty()) {
            builder.endpointOverride(URI.create(url))
                    .forcePathStyle(true);
        }

        return builder.build();
    }
}