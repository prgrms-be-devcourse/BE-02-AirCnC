package com.gurudev.aircnc.domain.room.dto;

import com.gurudev.aircnc.domain.room.entity.RoomPhoto;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RoomPhotoDto {

  private final String fileName;

  public static RoomPhotoDto of(RoomPhoto roomPhoto) {
    return new RoomPhotoDto(roomPhoto.getFileName());
  }

  public RoomPhoto toEntity() {
    return new RoomPhoto(fileName);
  }
}
