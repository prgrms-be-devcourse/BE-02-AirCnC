package com.gurudev.aircnc.domain.room.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class RoomPhotoTest {

  @Test
  void 숙소_사진_생성() {
    RoomPhoto roomPhoto = new RoomPhoto("photo.jpg");

    assertThat(roomPhoto.getPhotoUrl()).isEqualTo("photo.jpg");
  }

  @ParameterizedTest
  @NullAndEmptySource
  void URL_이_공백인_숙소_사진_생성_실패(String invalidPhotoUrl) {
    assertThatIllegalArgumentException()
        .isThrownBy(() -> new RoomPhoto(invalidPhotoUrl));
  }
}