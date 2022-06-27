package com.gurudev.aircnc.domain.room.entity;

import static com.gurudev.aircnc.domain.room.entity.Room.ROOM_DESCRIPTION_MIN_LENGTH;
import static com.gurudev.aircnc.domain.room.entity.Room.ROOM_PRICE_PER_DAY_MIN_VALUE;
import static com.gurudev.aircnc.domain.util.Fixture.createAddress;
import static com.gurudev.aircnc.domain.util.Fixture.createHost;
import static com.gurudev.aircnc.domain.util.Fixture.createRoom;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import com.gurudev.aircnc.domain.member.entity.Member;
import java.util.List;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class RoomTest {

  private String name;
  private Address address;
  private String description;
  private int capacity;
  private int pricePerDay;

  @BeforeEach
  void setUp() {
    name = "전주 한옥마을";
    address = createAddress();
    description = "아주 멋진 한옥마을입니다.";
    capacity = 4;
    pricePerDay = 100000;
  }

  @Test
  void 숙소_생성() {
    //when
    Room room = new Room(name, address, description, pricePerDay, capacity);

    //then
    assertThat(room)
        .extracting(Room::getName, Room::getAddress, Room::getDescription, Room::getPricePerDay,
            Room::getCapacity)
        .isEqualTo(List.of(name, address, description, pricePerDay, capacity));
  }

  @ParameterizedTest
  @NullAndEmptySource
  void 이름이_공백인_숙소_생성_실패(String invalidName) {
    //then
    assertThatIllegalArgumentException()
        .isThrownBy(() -> new Room(invalidName, address, description, pricePerDay, capacity));
  }

  @ParameterizedTest
  @NullAndEmptySource
  void 설명이_공백인_숙소_생성_실패(String invalidDescription) {
    //then
    assertThatIllegalArgumentException()
        .isThrownBy(() -> new Room(name, address, invalidDescription, pricePerDay, capacity));
  }

  @Test
  void 설명의_길이_제한에_맞지않는_숙소_생성_실패() {
    //given
    String invalidDescription = RandomString.make(ROOM_DESCRIPTION_MIN_LENGTH - 1);

    //then
    assertThatIllegalArgumentException()
        .isThrownBy(() -> new Room(name, address, invalidDescription, pricePerDay, capacity));
  }

  @Test
  void 가격이_제한에_맞지않는_숙소_생성_실패() {
    //given
    int invalidPricePerDay = ROOM_PRICE_PER_DAY_MIN_VALUE - 1;

    //then
    assertThatIllegalArgumentException()
        .isThrownBy(() -> new Room(name, address, description, invalidPricePerDay, capacity));
  }

  @Test
  void 인원수가_0이하인_숙소_생성_실패() {
    //given
    int invalidCapacity = 0;

    //then
    assertThatIllegalArgumentException()
        .isThrownBy(() -> new Room(name, address, description, pricePerDay, invalidCapacity));
  }

  @Test
  void 숙소의_이름_설명_가격을_변경할_수_있다() {
    //given
    Room room = createRoom();

    //when
    room.update("변경된 숙소 이름", "변경된 숙소 설명입니다", 20000);

    //then
    assertThat(room)
        .extracting(Room::getName, Room::getDescription, Room::getPricePerDay)
        .isEqualTo(List.of("변경된 숙소 이름", "변경된 숙소 설명입니다", 20000));
  }

  @Test
  void 숙소의_호스트_할당() {
    //given
    Room room = createRoom();
    Member host = createHost();

    //when
    room.assignHost(host);

    //then
    assertThat(room.getHost()).isEqualTo(host);
  }
}