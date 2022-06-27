package com.gurudev.aircnc.infrastructure.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;

@RequiredArgsConstructor
public class S3Client {

  private final AmazonS3 amazonS3;
  private final String url;
  private final String bucketName;

  public S3Object get(String key) {
    GetObjectRequest request = new GetObjectRequest(bucketName, key);
    return amazonS3.getObject(request);
  }

  public String upload(File file) {
    PutObjectRequest request = new PutObjectRequest(bucketName, file.getName(), file);
    return executePut(request);
  }

  public String upload(byte[] bytes, Map<String, String> metadata, String basePath) {
    String name = basePath + "/" + UUID.randomUUID();
    return upload(new ByteArrayInputStream(bytes), bytes.length, name + ".jpeg", "image/jpeg",
        metadata);
  }

  public String upload(InputStream in, long length, String key, String contentType,
      Map<String, String> metadata) {
    ObjectMetadata objectMetadata = new ObjectMetadata();
    objectMetadata.setContentLength(length);
    objectMetadata.setContentType(contentType);
    if (metadata != null && !metadata.isEmpty()) {
      objectMetadata.setUserMetadata(metadata);
    }

    PutObjectRequest request = new PutObjectRequest(bucketName, key, in, objectMetadata);
    return executePut(request);
  }

  public void delete(String url) {
    String key = FilenameUtils.getName(url);
    DeleteObjectRequest request = new DeleteObjectRequest(bucketName, key);
    executeDelete(request);
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

  private void executeDelete(DeleteObjectRequest request) {
    amazonS3.deleteObject(request);
  }
}
