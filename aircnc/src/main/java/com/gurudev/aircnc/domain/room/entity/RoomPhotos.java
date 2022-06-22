package com.gurudev.aircnc.domain.room.entity;

import static com.google.common.base.Preconditions.checkArgument;
import static lombok.AccessLevel.PROTECTED;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

/* 숙소 사진 컬렉션 */
@Getter
@NoArgsConstructor(access = PROTECTED)
public class RoomPhotos {

  public static final int ROOM_PHOTOS_MAX_SIZE = 10;

  private List<RoomPhoto> roomPhotos = new ArrayList<>();

  public RoomPhotos(List<RoomPhoto> roomPhotos) {
    checkArgument(roomPhotos.size() <= ROOM_PHOTOS_MAX_SIZE,
        "숙소의 사진은 최대 %d 장입니다".formatted(ROOM_PHOTOS_MAX_SIZE));

    this.roomPhotos = roomPhotos;
  }
}
