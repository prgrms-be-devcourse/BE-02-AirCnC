package com.gurudev.aircnc.domain.room.service;

import static com.gurudev.aircnc.domain.util.Fixture.createHost;
import static com.gurudev.aircnc.domain.util.Fixture.createHostDto;
import static com.gurudev.aircnc.domain.util.Fixture.createRoom;
import static com.gurudev.aircnc.domain.util.Fixture.createRoomPhoto;
import static org.assertj.core.api.Assertions.assertThat;

import com.gurudev.aircnc.domain.member.dto.MemberDto;
import com.gurudev.aircnc.domain.member.entity.Member;
import com.gurudev.aircnc.domain.member.service.MemberService;
import com.gurudev.aircnc.domain.room.entity.Room;
import com.gurudev.aircnc.domain.room.entity.RoomPhoto;
import com.gurudev.aircnc.domain.util.Dto;
import java.util.Collections;
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
  private MemberDto hostDto;
  private Room room1;
  private Room room2;

  private List<RoomPhoto> roomPhotos;

  @BeforeEach
  void setUp() {
    host = memberService.register(createHostDto());

    room1 = createRoom();
    room2 = createRoom();

    roomPhotos = List.of(createRoomPhoto(), createRoomPhoto());

    room1 = roomService.register(Dto.of(room1), Dto.listOf(roomPhotos), host.getId());
    room2 = roomService.register(Dto.of(room2), Collections.emptyList(), host.getId());
  }

  @Test
  void 숙소_등록_성공() {
    Room room = createRoom();

    Room registeredRoom = roomService.register(Dto.of(room), Dto.listOf(roomPhotos), host.getId());

    assertThat(registeredRoom.getId()).isNotNull();
    assertThat(registeredRoom.getHost()).isEqualTo(host);
    assertThat(registeredRoom.getRoomPhotos()).containsExactlyElementsOf(roomPhotos);
  }

  @Test
  void 숙소_리스트_조회_성공() {
    List<Room> rooms = roomService.getAll();

    assertThat(rooms).hasSize(2)
        .containsExactly(room1, room2);
  }
}
