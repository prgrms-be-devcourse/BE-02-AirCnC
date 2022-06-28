package com.gurudev.aircnc.domain.room.service;

import static com.gurudev.aircnc.domain.util.Fixture.createRoom;
import static com.gurudev.aircnc.domain.util.Fixture.createRoomPhoto;
import static com.gurudev.aircnc.util.AssertionUtil.assertThatAircncRuntimeException;
import static com.gurudev.aircnc.util.AssertionUtil.assertThatNotFoundException;
import static org.assertj.core.api.Assertions.assertThat;

import com.gurudev.aircnc.domain.member.entity.Member;
import com.gurudev.aircnc.domain.member.service.MemberService;
import com.gurudev.aircnc.domain.room.entity.Room;
import com.gurudev.aircnc.domain.room.entity.RoomPhoto;
import com.gurudev.aircnc.domain.room.repository.RoomRepository;
import com.gurudev.aircnc.domain.room.service.command.RoomCommand.RoomUpdateCommand;
import com.gurudev.aircnc.domain.trip.service.TripService;
import com.gurudev.aircnc.domain.util.Command;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class RoomServiceImplTest {

  @Autowired
  RoomRepository roomRepository;

  @Autowired
  private MemberService memberService;

  @Autowired
  private RoomService roomService;

  @Autowired
  private TripService tripService;

  private Member host;
  private Member guest;
  private Room room1;
  private Room room2;

  private List<RoomPhoto> roomPhotos;

  @BeforeEach
  void setUp() {
    host = memberService.register(Command.ofHost());

    room1 = createRoom();
    room2 = createRoom();

    roomPhotos = List.of(createRoomPhoto(), createRoomPhoto());

    guest = memberService.register(Command.ofGuest());
  }

  @Test
  void 숙소_등록_성공() {
    Room room = createRoom();

    Room registeredRoom = roomService.register(Command.ofRoom(room, roomPhotos, host.getId()));

    assertThat(registeredRoom.getId()).isNotNull();
    assertThat(registeredRoom.getHost()).isEqualTo(host);
    assertThat(registeredRoom.getRoomPhotos()).containsExactlyElementsOf(roomPhotos);
  }

  @Test
  void 숙소_리스트_조회_성공() {
    room1 = roomService.register(Command.ofRoom(room1, roomPhotos, host.getId()));
    room2 = roomService.register(Command.ofRoom(room2, Collections.emptyList(), host.getId()));

    List<Room> rooms = roomService.getAll();

    assertThat(rooms).hasSize(2)
        .containsExactly(room1, room2);
  }

  @ParameterizedTest
  @CsvSource(value = {
      "변경된 숙소 이름, 변경된 숙소 설명입니다, 25000",
      "변경된 숙소 이름, , ",
      " , 변경된 숙소 설명입니다, ",
      " , , 25000"
  })
  void 숙소_이름_설명_가격_변경_성공(String updatedName, String updatedDescription,
      Integer updatedPricePerDay) {
    //given
    room1 = roomService.register(Command.ofRoom(room1, roomPhotos, host.getId()));
    String originalName = room1.getName();
    String originalDescription = room1.getDescription();
    Integer originalPricePerDay = room1.getPricePerDay();
    RoomUpdateCommand roomUpdateCommand = new RoomUpdateCommand(room1.getHost().getId(),
        room1.getId(), updatedName, updatedDescription, updatedPricePerDay);

    //when
    Room updatedRoom = roomService.update(roomUpdateCommand);

    //then
    assertThat(updatedRoom)
        .extracting(
            Room::getName,
            Room::getDescription,
            Room::getPricePerDay
        ).isEqualTo(
            List.of(updatedName == null ? originalName : updatedName,
                updatedDescription == null ? originalDescription : updatedDescription,
                updatedPricePerDay == null ? originalPricePerDay : updatedPricePerDay));
  }

  @Test
  void 해당_숙소의_호스트가_아닌_경우_변경_실패() {
    //given
    room1 = roomService.register(Command.ofRoom(room1, roomPhotos, host.getId()));
    RoomUpdateCommand roomUpdateCommand = new RoomUpdateCommand(host.getId() + 1,
        room1.getId(), "변경할 숙소 이름", "변경할 숙소 설명입니다", 25000);

    //then
    assertThatNotFoundException()
        .isThrownBy(() -> roomService.update(roomUpdateCommand));
  }

  @Test
  void 숙소_삭제_성공() {
    //when
    room1 = roomService.register(Command.ofRoom(room1, roomPhotos, host.getId()));
    roomService.delete(Command.ofHost(host.getId(), room1.getId()));

    //then
    assertThatNotFoundException()
        .isThrownBy(() -> roomService.getById(room1.getId()));
  }

  @Test
  void 여행_중_혹은_예약이_존재하는_숙소_삭제_실패() {
    //given
    room2 = roomService.register(Command.ofRoom(room2, Collections.emptyList(), host.getId()));
    tripService.reserve(guest, room2.getId(), LocalDate.now(), LocalDate.now().plusDays(1), 3,
        room2.getPricePerDay());

    //then
    assertThatAircncRuntimeException()
        .isThrownBy(() -> roomService.delete(Command.ofHost(host.getId(), room2.getId())));
  }

  @Test
  void 호스트가_아닌_경우_숙소_삭제_실패() {
    //given
    Member fakeHost = memberService.register(Command.ofHost("fakeHost@email.com"));
    room2 = roomService.register(Command.ofRoom(room2, Collections.emptyList(), host.getId()));
    tripService.reserve(guest, room2.getId(), LocalDate.now(), LocalDate.now().plusDays(1), 3,
        room2.getPricePerDay());

    //then
    assertThatAircncRuntimeException()
        .isThrownBy(() -> roomService.delete(Command.ofHost(fakeHost.getId(), room2.getId())));
  }
}
