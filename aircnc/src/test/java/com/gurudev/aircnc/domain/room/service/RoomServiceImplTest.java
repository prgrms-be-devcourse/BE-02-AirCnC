package com.gurudev.aircnc.domain.room.service;

import static com.gurudev.aircnc.domain.util.Fixture.createHost;
import static com.gurudev.aircnc.domain.util.Fixture.createRoom;
import static com.gurudev.aircnc.domain.util.Fixture.createRoomPhoto;
import static org.assertj.core.api.Assertions.assertThat;

import com.gurudev.aircnc.domain.member.entity.Member;
import com.gurudev.aircnc.domain.member.service.MemberService;
import com.gurudev.aircnc.domain.room.entity.Room;
import com.gurudev.aircnc.domain.room.entity.RoomPhoto;
import com.gurudev.aircnc.domain.util.Command;
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
  private MemberService memberService;

  @Autowired
  private RoomService roomService;

  private Member host;

  @BeforeEach
  void setUp() {
    host = createHost();
    host = memberService.register(Command.ofRegisterMember(host));
  }

  @Test
  void 숙소_등록_성공() {
    //given
    Room room = createRoom();
    List<RoomPhoto> roomPhotos = List.of(createRoomPhoto(), createRoomPhoto());

    //then
    Room registeredRoom =
        roomService.register(Command.ofRegisterRoom(room, roomPhotos, host.getId()));

    //then
    assertThat(registeredRoom.getId()).isNotNull();
    assertThat(registeredRoom.getHost()).isEqualTo(host);
    assertThat(registeredRoom.getRoomPhotos()).containsExactlyElementsOf(roomPhotos);
  }

  @Test
  void 숙소_리스트_조회_성공() {
    //given
    Room room1 = createRoom();
    Room room2 = createRoom();

    List<RoomPhoto> roomPhotos1 = List.of(createRoomPhoto(), createRoomPhoto());
    List<RoomPhoto> roomPhotos2 = List.of(createRoomPhoto(), createRoomPhoto());

    room1 = roomService.register(Command.ofRegisterRoom(room1, roomPhotos1, host.getId()));
    room2 = roomService.register(Command.ofRegisterRoom(room2, roomPhotos2, host.getId()));

    //when
    List<Room> rooms = roomService.getAll();

    //then
    assertThat(rooms).hasSize(2).containsExactly(room1, room2);
  }
}
