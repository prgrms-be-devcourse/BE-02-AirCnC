package com.gurudev.aircnc.domain.room.service;

import com.gurudev.aircnc.domain.room.entity.RoomPhoto;
import com.gurudev.aircnc.exception.RoomPhotoUploadException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@Transactional(readOnly = true)
public class LocalRoomPhotoServiceImpl implements RoomPhotoService {

  @Value("${room-photos.path}")
  private String roomPhotosPath;

  @Transactional
  @Override
  public RoomPhoto upload(MultipartFile multipartFile) {
    String fileName = getFileName(multipartFile);

    try {
      multipartFile.transferTo(Path.of(roomPhotosPath, fileName));
    } catch (IOException e) {
      throw new RoomPhotoUploadException("숙소 사진 저장 중 예외 발생 : " + multipartFile.getOriginalFilename(), e);
    }

    return new RoomPhoto(fileName);
  }

  private String getFileName(MultipartFile multipartFile) {
    UUID photoName = UUID.randomUUID();
    String ext = getExtension(multipartFile);

    String fileName = photoName + "." + ext;
    return fileName;
  }

  private String getExtension(MultipartFile multipartFile) {
    String originalFilename = multipartFile.getOriginalFilename();
    int lastIndexOfDot = originalFilename.lastIndexOf(".");

    return originalFilename.substring(lastIndexOfDot + 1);
  }
}
