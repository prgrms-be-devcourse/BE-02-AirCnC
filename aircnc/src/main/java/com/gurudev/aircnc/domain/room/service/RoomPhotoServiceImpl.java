package com.gurudev.aircnc.domain.room.service;

import com.gurudev.aircnc.domain.base.AttachedFile;
import com.gurudev.aircnc.domain.room.entity.RoomPhoto;
import com.gurudev.aircnc.infrastructure.aws.s3.S3Client;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomPhotoServiceImpl implements RoomPhotoService {

  public static final String BASE_PATH = "room-photos";

  private final S3Client s3Client;

  @Override
  public RoomPhoto upload(AttachedFile attachedFiles) {
    String path = s3Client.upload(
        attachedFiles.getBytes(),
        BASE_PATH
    );

    return new RoomPhoto(path);
  }
}
