package com.gurudev.aircnc.domain.room.service.cmd;

import com.gurudev.aircnc.domain.room.entity.RoomPhoto;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RoomPhotoCommand {

  private final String fileName;

  public static RoomPhotoCommand of(RoomPhoto roomPhoto) {
    return new RoomPhotoCommand(roomPhoto.getFileName());
  }

  public RoomPhoto toEntity() {
    return new RoomPhoto(fileName);
  }
}
