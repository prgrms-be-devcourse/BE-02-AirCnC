package com.gurudev.aircnc.infrastructure.aws.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class S3Client {

  private final AmazonS3 amazonS3;
  private final String url;
  private final String bucketName;

  public String upload(byte[] bytes, String basePath) {
    String name = basePath + "/" + UUID.randomUUID();
    return upload(new ByteArrayInputStream(bytes), bytes.length, name + ".jpeg", "image/jpeg");
  }

  public String upload(InputStream in, long length, String key, String contentType) {
    ObjectMetadata objectMetadata = new ObjectMetadata();
    objectMetadata.setContentLength(length);
    objectMetadata.setContentType(contentType);

    PutObjectRequest request = new PutObjectRequest(bucketName, key, in, objectMetadata);
    return executePut(request);
  }

  private String executePut(PutObjectRequest request) {
    amazonS3.putObject(request);
    StringBuilder sb = new StringBuilder(url);
    if (!url.endsWith("/")) {
      sb.append("/");
    }
    sb.append(bucketName);
    sb.append("/");
    sb.append(request.getKey());
    return sb.toString();
  }

}
