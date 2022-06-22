package com.gurudev.aircnc.domain.room.entity;

import static com.gurudev.aircnc.domain.room.entity.Room.ROOM_CAPACITY_MIN_VALUE;
import static com.gurudev.aircnc.domain.room.entity.Room.ROOM_DESCRIPTION_MIN_LENGTH;
import static com.gurudev.aircnc.domain.room.entity.Room.ROOM_PRICE_PER_DAY_MIN_VALUE;
import static com.gurudev.aircnc.domain.util.Fixture.createGuest;
import static com.gurudev.aircnc.domain.util.Fixture.createHost;
import static com.gurudev.aircnc.util.AssertionUtil.assertThatAircncRuntimeException;
import static org.assertj.core.api.Assertions.assertThat;

import com.gurudev.aircnc.domain.member.entity.Member;
import java.util.List;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class RoomTest {

  private final String name = "전주 한옥마을";
  private final Address address = new Address("전라북도 전주시 완산구 풍산동 3가");
  private final String description = "아주 멋진 한옥마을입니다.";
  private final int capacity = 4;
  private final int pricePerDay = 100000;
  private final Member host = createHost();
  private final Member guest = createGuest();

  @Test
  void 숙소_생성() {
    Room room = new Room(name, address, description, pricePerDay, capacity, host);

    assertThat(room).extracting(Room::getName, Room::getAddress, Room::getDescription,
            Room::getPricePerDay, Room::getCapacity, Room::getHost)
        .isEqualTo(List.of(name, address, description, pricePerDay, capacity, host));
  }

  @ParameterizedTest
  @NullAndEmptySource
  void 이름이_공백인_숙소_생성_실패(String invalidName) {
    assertThatAircncRuntimeException()
        .isThrownBy(() -> new Room(invalidName, address, description, pricePerDay, capacity, host));
  }

  @ParameterizedTest
  @NullAndEmptySource
  void 설명이_공백인_숙소_생성_실패(String invalidDescription) {
    assertThatAircncRuntimeException()
        .isThrownBy(() -> new Room(name, address, invalidDescription, pricePerDay, capacity, host));
  }

  @Test
  void 설명의_길이_제한에_맞지않는_숙소_생성_실패() {
    String invalidDescription = RandomString.make(ROOM_DESCRIPTION_MIN_LENGTH - 1);

    assertThatAircncRuntimeException()
        .isThrownBy(() -> new Room(name, address, invalidDescription, pricePerDay, capacity, host));
  }

  @Test
  void 가격이_제한에_맞지않는_숙소_생성_실패() {
    int invalidPricePerDay = ROOM_PRICE_PER_DAY_MIN_VALUE - 1;

    assertThatAircncRuntimeException()
        .isThrownBy(() -> new Room(name, address, description, invalidPricePerDay, capacity, host));
  }


  @Test
  void 인원수가_제한에_맞지않는_숙소_생성_실패() {
    int invalidCapacity = ROOM_CAPACITY_MIN_VALUE - 1;

    assertThatAircncRuntimeException()
        .isThrownBy(() -> new Room(name, address, description, pricePerDay, invalidCapacity, host));
  }

  @Test
  void 게스트의_숙소_생성_실패() {
    assertThatAircncRuntimeException()
        .isThrownBy(() -> new Room(name, address, description, pricePerDay, capacity, guest));
  }
}