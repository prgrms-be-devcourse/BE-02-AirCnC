package com.gurudev.aircnc.domain.room.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class RoomPhotoTest {

  @Test
  void 숙소_사진_생성() {
    //given
    String fileName = "photo.jpg";

    //when
    RoomPhoto roomPhoto = new RoomPhoto(fileName);

    //then
    assertThat(roomPhoto.getFileName()).isEqualTo(fileName);
  }

  @ParameterizedTest
  @NullAndEmptySource
  void 파일_이름이_공백인_숙소_사진_생성_실패(String invalidFileName) {
    //then
    assertThatIllegalArgumentException()
        .isThrownBy(() -> new RoomPhoto(invalidFileName));
  }
}