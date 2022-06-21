package com.gurudev.aircnc.domain.room.service;

import static com.gurudev.aircnc.domain.util.Fixture.createRoom;
import static com.gurudev.aircnc.domain.util.Fixture.createRoomPhoto;
import static org.assertj.core.api.Assertions.assertThat;

import com.gurudev.aircnc.domain.room.entity.Room;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class RoomServiceImplTest {

  @Autowired
  private RoomService roomService;

  @Test
  void 숙소_등록_성공() {
    Room room = createRoom();

    Room registeredRoom = roomService.register(room,
        List.of(createRoomPhoto(), createRoomPhoto()));

    assertThat(registeredRoom.getId()).isNotNull();
    assertThat(registeredRoom)
        .extracting(Room::getName, Room::getAddress, Room::getDescription, Room::getPricePerDay,
            Room::getCapacity, Room::getReviewCount)
        .isEqualTo(
            List.of(room.getName(), room.getAddress(), room.getDescription(), room.getPricePerDay(),
                room.getCapacity(), room.getReviewCount()));

    assertThat(registeredRoom.getHost()).isEqualTo(room.getHost());
    assertThat(registeredRoom.getRoomPhotos()).containsExactly(createRoomPhoto(),
        createRoomPhoto());
  }
}