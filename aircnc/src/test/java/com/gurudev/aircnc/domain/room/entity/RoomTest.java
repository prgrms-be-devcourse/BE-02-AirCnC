package com.gurudev.aircnc.domain.room.entity;

import static com.gurudev.aircnc.domain.room.entity.Room.ROOM_DESCRIPTION_MIN_LENGTH;
import static com.gurudev.aircnc.domain.room.entity.Room.ROOM_PRICE_PER_DAY_MIN_VALUE;
import static com.gurudev.aircnc.domain.util.Fixture.createAddress;
import static com.gurudev.aircnc.domain.util.Fixture.createHost;
import static com.gurudev.aircnc.domain.util.Fixture.createRoom;
import static com.gurudev.aircnc.util.AssertionUtil.assertThatAircncRuntimeException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import com.gurudev.aircnc.domain.member.entity.Member;
import java.util.List;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class RoomTest {

  private final String name = "전주 한옥마을";
  private final Address address = createAddress();
  private final String description = "아주 멋진 한옥마을입니다.";
  private final int capacity = 4;
  private final int pricePerDay = 100000;

  @Test
  void 숙소_생성() {
    Room room = new Room(name, address, description, pricePerDay, capacity);

    assertThat(room)
        .extracting(Room::getName, Room::getAddress, Room::getDescription, Room::getPricePerDay,
            Room::getCapacity)
        .isEqualTo(List.of(name, address, description, pricePerDay, capacity));
  }

  @ParameterizedTest
  @NullAndEmptySource
  void 이름이_공백인_숙소_생성_실패(String invalidName) {
    assertThatIllegalArgumentException()
        .isThrownBy(() -> new Room(invalidName, address, description, pricePerDay, capacity));
  }

  @ParameterizedTest
  @NullAndEmptySource
  void 설명이_공백인_숙소_생성_실패(String invalidDescription) {
    assertThatIllegalArgumentException()
        .isThrownBy(() -> new Room(name, address, invalidDescription, pricePerDay, capacity));
  }

  @Test
  void 설명의_길이_제한에_맞지않는_숙소_생성_실패() {
    String invalidDescription = RandomString.make(ROOM_DESCRIPTION_MIN_LENGTH - 1);

    assertThatIllegalArgumentException()
        .isThrownBy(() -> new Room(name, address, invalidDescription, pricePerDay, capacity));
  }

  @Test
  void 가격이_제한에_맞지않는_숙소_생성_실패() {
    int invalidPricePerDay = ROOM_PRICE_PER_DAY_MIN_VALUE - 1;

    assertThatIllegalArgumentException()
        .isThrownBy(() -> new Room(name, address, description, invalidPricePerDay, capacity));
  }

  @Test
  void 인원수가_0이하인_작은_숙소_생성_실패() {
    int invalidCapacity = 0;

    assertThatIllegalArgumentException()
        .isThrownBy(() -> new Room(name, address, description, pricePerDay, invalidCapacity));
  }

  @Test
  void 숙소의_이름_설명_가격을_변경할_수_있다() {
    Room room = createRoom();

    room.update("변경된 숙소 이름", "변경된 숙소 설명입니다", 20000);

    assertThat(room)
        .extracting(Room::getName, Room::getDescription, Room::getPricePerDay)
        .isEqualTo(List.of("변경된 숙소 이름", "변경된 숙소 설명입니다", 20000));
  }

  @Test
  void 숙소의_호스트_할당() {
    Room room = createRoom();
    Member host = createHost();

    room.assignHost(host);

    assertThat(room.getHost()).isEqualTo(host);
  }
}