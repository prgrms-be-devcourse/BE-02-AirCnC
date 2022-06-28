package com.gurudev.aircnc.domain.base;

import static org.springframework.util.StringUtils.hasText;

import com.gurudev.aircnc.exception.AircncRuntimeException;
import java.io.IOException;
import java.util.Map;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class AttachedFile {

  private final String originalFileName;
  private final String contentType;
  private final byte[] bytes;
  private final Map<String, String> metadata;

  public AttachedFile(MultipartFile multipartFile) {
    checkMultipartFile(multipartFile);

    this.originalFileName = multipartFile.getOriginalFilename();
    this.contentType = multipartFile.getContentType();
    try {
      this.bytes = multipartFile.getBytes();
    } catch (IOException e) {
      throw new AircncRuntimeException("파일 등록 중 예외 발생", e);
    }
    this.metadata = Map.of("originalFileName", originalFileName);
  }

  private void checkMultipartFile(MultipartFile multipartFile) {
    if (multipartFile == null || multipartFile.getSize() <= 0 || !hasText(
        multipartFile.getOriginalFilename())) {
      throw new AircncRuntimeException("파일 등록 중 예외 발생");
    }

    String contentType = multipartFile.getContentType();
    if (!(hasText(contentType) && contentType.toLowerCase().startsWith("image"))) {
      throw new AircncRuntimeException("파일 등록 중 예외 발생");
    }
  }
}
