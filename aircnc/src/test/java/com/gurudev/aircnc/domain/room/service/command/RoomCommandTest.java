package com.gurudev.aircnc.domain.room.service.command;

import static com.gurudev.aircnc.domain.room.service.command.RoomCommand.ROOM_DESCRIPTION_MIN_LENGTH;
import static com.gurudev.aircnc.domain.room.service.command.RoomCommand.ROOM_PRICE_PER_DAY_MIN_VALUE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import com.gurudev.aircnc.controller.dto.RoomDto.RoomRegisterRequest;
import com.gurudev.aircnc.domain.room.entity.Address;
import com.gurudev.aircnc.domain.room.entity.RoomPhoto;
import com.gurudev.aircnc.domain.room.service.command.RoomCommand.RoomRegisterCommand;
import com.gurudev.aircnc.domain.room.service.command.RoomCommand.RoomUpdateCommand;
import java.util.List;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class RoomCommandTest {

  @Nested
  class 숙소_생성_명령 {

    @Test
    void 숙소_등록_명령_생성_성공() {
      //given
      RoomRegisterRequest request = new RoomRegisterRequest(
          "나의 숙소",
          "달나라 1번지",
          "달나라 1길",
          "100호",
          "1234",
          "달토끼가 사는 나의 숙소",
          100000,
          2);

      List<RoomPhoto> roomPhoto = List.of(new RoomPhoto("photo.jpg"));

      Long hostId = 1L;

      //when
      RoomRegisterCommand command = RoomRegisterCommand.of(request, roomPhoto, hostId);

      //then
      assertThat(command).isNotNull()
          .extracting(
              RoomRegisterCommand::getName,
              RoomRegisterCommand::getAddress,
              RoomRegisterCommand::getDescription,
              RoomRegisterCommand::getPricePerDay,
              RoomRegisterCommand::getCapacity,
              RoomRegisterCommand::getHostId
          )
          .containsExactly(
              "나의 숙소",
              new Address("달나라 1번지", "달나라 1길", "100호", "1234"),
              "달토끼가 사는 나의 숙소",
              100000,
              2,
              1L
          );
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 이름이_공백인_숙소_생성_실패(String invalidName) {
      //given
      RoomRegisterRequest request = new RoomRegisterRequest(
          invalidName,
          "달나라 1번지",
          "달나라 1길",
          "100호",
          "1234",
          "달토끼가 사는 나의 숙소",
          100000,
          2);
      List<RoomPhoto> roomPhoto = List.of(new RoomPhoto("photo.jpg"));
      Long hostId = 1L;

      //then
      assertThatIllegalArgumentException()
          .isThrownBy(() -> RoomRegisterCommand.of(request, roomPhoto, hostId));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 설명이_공백인_숙소_생성_실패(String invalidDescription) {
      //given
      RoomRegisterRequest request = new RoomRegisterRequest(
          "나의 숙소",
          "달나라 1번지",
          "달나라 1길",
          "100호",
          "1234",
          invalidDescription,
          100000,
          2);
      List<RoomPhoto> roomPhoto = List.of(new RoomPhoto("photo.jpg"));
      Long hostId = 1L;

      //then
      assertThatIllegalArgumentException()
          .isThrownBy(() -> RoomRegisterCommand.of(request, roomPhoto, hostId));
    }

    @Test
    void 설명의_길이_제한에_맞지않는_숙소_생성_실패() {
      //given
      String invalidDescription = RandomString.make(ROOM_DESCRIPTION_MIN_LENGTH - 1);

      RoomRegisterRequest request = new RoomRegisterRequest(
          "나의 숙소",
          "달나라 1번지",
          "달나라 1길",
          "100호",
          "1234",
          invalidDescription,
          100000,
          2);
      List<RoomPhoto> roomPhoto = List.of(new RoomPhoto("photo.jpg"));
      Long hostId = 1L;

      //then
      assertThatIllegalArgumentException()
          .isThrownBy(() -> RoomRegisterCommand.of(request, roomPhoto, hostId));
    }

    @Test
    void 가격이_제한에_맞지않는_숙소_생성_실패() {
      //given
      int invalidPricePerDay = ROOM_PRICE_PER_DAY_MIN_VALUE - 1;

      RoomRegisterRequest request = new RoomRegisterRequest(
          "나의 숙소",
          "달나라 1번지",
          "달나라 1길",
          "100호",
          "1234",
          "달토끼가 사는 나의 숙소",
          invalidPricePerDay,
          2);
      List<RoomPhoto> roomPhoto = List.of(new RoomPhoto("photo.jpg"));
      Long hostId = 1L;

      //then
      assertThatIllegalArgumentException()
          .isThrownBy(() -> RoomRegisterCommand.of(request, roomPhoto, hostId));
    }

    @Test
    void 인원수가_0이하인_숙소_생성_실패() {
      //given
      int invalidCapacity = 0;

      RoomRegisterRequest request = new RoomRegisterRequest(
          "나의 숙소",
          "달나라 1번지",
          "달나라 1길",
          "100호",
          "1234",
          "달토끼가 사는 나의 숙소",
          100000,
          invalidCapacity);
      List<RoomPhoto> roomPhoto = List.of(new RoomPhoto("photo.jpg"));
      Long hostId = 1L;

      //then
      assertThatIllegalArgumentException()
          .isThrownBy(() -> RoomRegisterCommand.of(request, roomPhoto, hostId));
    }

  }

  @Nested
  class 숙소_수정_명령 {

    @ParameterizedTest
    @CsvSource(value = {
        "변경된 숙소 이름, 변경된 숙소 설명입니다, 25000",
        "변경된 숙소 이름, , ",
        " , 변경된 숙소 설명입니다, ",
        " , , 25000"
    })
    void 숙소_수정_명령_생성_성공(String name, String description, Integer pricePerDay) {
      //when
      RoomUpdateCommand command
          = new RoomUpdateCommand(1L, 2L, name, description, pricePerDay);

      //then
      assertThat(command).isNotNull()
          .extracting(
              RoomUpdateCommand::getName,
              RoomUpdateCommand::getDescription,
              RoomUpdateCommand::getPricePerDay
          ).containsExactly(name, description, pricePerDay);
    }
  }
}