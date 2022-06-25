package com.gurudev.aircnc.domain.room.service.cmd;

import com.gurudev.aircnc.domain.room.entity.RoomPhoto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RoomPhotoCommand {

  @RequiredArgsConstructor
  public static class RoomPhotoCreateCommand {

    private final String fileName;

    public static RoomPhotoCreateCommand of(RoomPhoto roomPhoto) {
      return new RoomPhotoCreateCommand(roomPhoto.getFileName());
    }

    public RoomPhoto toEntity() {
      return new RoomPhoto(fileName);
    }
  }
}
