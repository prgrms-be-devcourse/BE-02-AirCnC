package com.gurudev.aircnc.domain.room.entity;

import static com.gurudev.aircnc.domain.util.Fixture.createAddress;
import static com.gurudev.aircnc.domain.util.Fixture.createHost;
import static com.gurudev.aircnc.domain.util.Fixture.createRoom;
import static org.assertj.core.api.Assertions.assertThat;

import com.gurudev.aircnc.domain.member.entity.Member;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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