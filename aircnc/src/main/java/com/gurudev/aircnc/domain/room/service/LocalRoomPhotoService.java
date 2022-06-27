package com.gurudev.aircnc.domain.room.service;

import static java.util.stream.Collectors.toList;

import com.gurudev.aircnc.domain.room.entity.RoomPhoto;
import com.gurudev.aircnc.exception.RoomRegisterException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
public class LocalRoomPhotoService implements RoomPhotoService {

  @Value("${room-photos.path}")
  private String roomPhotosPath;

  @Override
  @Transactional
  public List<RoomPhoto> upload(List<MultipartFile> multipartFile) {
    return multipartFile.stream()
        .map(this::upload)
        .collect(toList());
  }

  @Override
  @Transactional
  public RoomPhoto upload(MultipartFile multipartFile) {
    String fileName = getFileName(multipartFile);

    try {
      multipartFile.transferTo(Path.of(roomPhotosPath, fileName));
    } catch (IOException e) {
      throw new RoomRegisterException("숙소 사진 저장 중 예외 발생 : " + multipartFile.getOriginalFilename(),
          e);
    }

    return new RoomPhoto(fileName);
  }

  private String getFileName(MultipartFile multipartFile) {
    UUID uuid = UUID.randomUUID();
    String ext = getExtension(multipartFile);

    return uuid + "." + ext;
  }

  private String getExtension(MultipartFile multipartFile) {
    String originalFilename = multipartFile.getOriginalFilename();

    return originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
  }
}
