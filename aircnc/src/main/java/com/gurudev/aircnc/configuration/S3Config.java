package com.gurudev.aircnc.configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.gurudev.aircnc.infrastructure.aws.s3.S3Client;
import com.gurudev.aircnc.infrastructure.aws.s3.S3Properties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {

  @Bean
  public AmazonS3 amazonS3Client(S3Properties properties) {
    return AmazonS3ClientBuilder.standard()
        .withRegion(Regions.fromName(properties.getRegion()))
        .withCredentials(
            new AWSStaticCredentialsProvider(
                new BasicAWSCredentials(
                    properties.getAccessKey(),
                    properties.getSecretKey())
            )
        )
        .build();
  }

  @Bean
  public S3Client s3Client(AmazonS3 amazonS3, S3Properties properties) {
    return new S3Client(amazonS3, properties.getUrl(), properties.getBucketName());
  }
}
