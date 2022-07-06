package com.gurudev.aircnc.domain.common;

import static org.springframework.util.StringUtils.hasText;

import java.io.IOException;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class AttachedFile {

  private final String originalFileName;
  private final String contentType;
  private final byte[] bytes;

  public AttachedFile(MultipartFile multipartFile) {
    checkMultipartFile(multipartFile);

    this.originalFileName = multipartFile.getOriginalFilename();
    this.contentType = multipartFile.getContentType();
    try {
      this.bytes = multipartFile.getBytes();
    } catch (IOException e) {
      throw new IllegalStateException("파일 등록 중 예외 발생", e);
    }
  }

  private void checkMultipartFile(MultipartFile multipartFile) {
    if (multipartFile == null || multipartFile.getSize() <= 0 || !hasText(
        multipartFile.getOriginalFilename())) {
      throw new IllegalArgumentException("사진을 등록하지 않았습니다");
    }

    String contentType = multipartFile.getContentType();
    if (!(hasText(contentType) && contentType.toLowerCase().startsWith("image"))) {
      throw new IllegalArgumentException("지원되지 않는 확장파일입니다");
    }
  }
}
