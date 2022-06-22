package com.gurudev.aircnc.domain.room.entity;

import static com.gurudev.aircnc.domain.room.entity.RoomPhotos.ROOM_PHOTOS_MAX_SIZE;
import static com.gurudev.aircnc.domain.util.Fixture.createRoomPhoto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class RoomPhotosTest {

  @Test
  void 숙소_사진_컬렉션_생성() {
    List<RoomPhoto> roomPhotoList = List.of(createRoomPhoto(), createRoomPhoto());

    RoomPhotos roomPhotos = new RoomPhotos(roomPhotoList);

    assertThat(roomPhotos.getRoomPhotos()).containsExactly(createRoomPhoto(), createRoomPhoto());
  }

  @Test
  void 개수_제한에_맞지않는_숙소_사진_컬렉션_생성_실패() {
    List<RoomPhoto> roomPhotoList = new ArrayList<>();

    for (int i = 0; i < ROOM_PHOTOS_MAX_SIZE + 1; i++) {
      roomPhotoList.add(createRoomPhoto());
    }

    assertThatIllegalArgumentException()
        .isThrownBy(() -> new RoomPhotos(roomPhotoList));

  }
}