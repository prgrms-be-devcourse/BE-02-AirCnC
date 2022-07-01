package com.gurudev.aircnc.domain.room.service;

import static com.gurudev.aircnc.domain.util.Fixture.createGuest;
import static com.gurudev.aircnc.domain.util.Fixture.createHost;
import static com.gurudev.aircnc.domain.util.Fixture.createRoom;
import static com.gurudev.aircnc.domain.util.Fixture.createRoomPhoto;
import static com.gurudev.aircnc.util.AssertionUtil.assertThatNotFoundException;
import static java.time.Period.between;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

import com.gurudev.aircnc.domain.member.entity.Member;
import com.gurudev.aircnc.domain.member.service.MemberService;
import com.gurudev.aircnc.domain.member.service.command.MemberCommand.MemberRegisterCommand;
import com.gurudev.aircnc.domain.room.entity.Room;
import com.gurudev.aircnc.domain.room.entity.RoomPhoto;
import com.gurudev.aircnc.domain.room.service.command.RoomCommand.RoomDeleteCommand;
import com.gurudev.aircnc.domain.room.service.command.RoomCommand.RoomRegisterCommand;
import com.gurudev.aircnc.domain.room.service.command.RoomCommand.RoomUpdateCommand;
import com.gurudev.aircnc.domain.trip.entity.Trip;
import com.gurudev.aircnc.domain.trip.service.TripService;
import com.gurudev.aircnc.domain.util.Command;
import com.gurudev.aircnc.infrastructure.event.TripEvent;
import com.gurudev.aircnc.infrastructure.mail.service.EmailService;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest(webEnvironment = NONE)
class RoomServiceImplTest {

  @Autowired
  private MemberService memberService;

  @Autowired
  private RoomService roomService;

  @Autowired
  private TripService tripService;

  private Member host;

  private Member guest;
  private Member fakeHost;

  private List<RoomPhoto> roomPhotos;

  private LocalDate checkIn;
  private LocalDate checkOut;
  private int totalPrice;
  private int headCount;

  @MockBean(name = "roomEmailService")
  private EmailService roomEmailService;

  @MockBean(name = "tripEmailService")
  private EmailService tripEmailService;

  @BeforeEach
  void setUp() {
    // 회원 세팅
    host = memberService.register(Command.ofRegisterMember(createHost()));
    guest = memberService.register(Command.ofRegisterMember(createGuest()));

    // 가짜 회원 세팅
    MemberRegisterCommand fakeHostRegisterCommand = Command.ofRegisterMember(createHost());
    ReflectionTestUtils.setField(fakeHostRegisterCommand, "email", "fakeHost@email.com");
    fakeHost = memberService.register(fakeHostRegisterCommand);

    //숙소의 필드 세팅
    Room fixtureRoom = createRoom();
    roomPhotos = List.of(createRoomPhoto(), createRoomPhoto());
    checkIn = LocalDate.now();
    checkOut = checkIn.plusDays(1);
    totalPrice = between(checkIn, checkOut).getDays() * fixtureRoom.getPricePerDay();
    headCount = fixtureRoom.getCapacity();
  }

  @Test
  void 숙소_등록_성공() {
    //given
    Room room = createRoom();
    List<RoomPhoto> roomPhotos = List.of(createRoomPhoto(), createRoomPhoto());

    //then
    RoomRegisterCommand roomRegisterCommand = Command.ofRegisterRoom(room, roomPhotos,
        host.getId());
    Room registeredRoom = roomService.register(roomRegisterCommand);

    //then
    assertThat(registeredRoom.getId()).isNotNull();
    assertThat(registeredRoom.getHost()).isEqualTo(host);
    assertThat(registeredRoom.getRoomPhotos()).containsExactlyElementsOf(roomPhotos);
  }

  @Test
  void 숙소_리스트_조회_성공() {
    //given
    Room roomA = roomService.register(defaultRoomRegisterCommand());
    Room roomB = roomService.register(defaultRoomRegisterCommand());

    //when
    List<Room> rooms = roomService.getAll();

    //then
    assertThat(rooms).hasSize(2).containsExactly(roomA, roomB);
  }

  @ParameterizedTest
  @CsvSource(value = {
      "변경된 숙소 이름, 변경된 숙소 설명입니다, 25000",
      "변경된 숙소 이름, , ",
      " , 변경된 숙소 설명입니다, ",
      " , , 25000"
  })
  void 숙소_이름_설명_가격_변경_성공(String name, String description, Integer pricePerDay) {
    //given
    RoomRegisterCommand command = defaultRoomRegisterCommand();
    Room room = roomService.register(command);

    String originalName = room.getName();
    String originalDescription = room.getDescription();
    int originalPricePerDay = room.getPricePerDay();

    //when
    RoomUpdateCommand roomUpdateCommand =
        new RoomUpdateCommand(room.getHost().getId(), room.getId(), name, description, pricePerDay);
    Room updatedRoom = roomService.update(roomUpdateCommand);

    //then
    assertThat(updatedRoom)
        .extracting(
            Room::getName,
            Room::getDescription,
            Room::getPricePerDay
        ).isEqualTo(
            List.of(name == null ? originalName : name,
                description == null ? originalDescription : description,
                pricePerDay == null ? originalPricePerDay : pricePerDay));
  }

  @Test
  void 해당_숙소의_호스트가_아닌_경우_변경_실패() {
    //given
    RoomRegisterCommand command = defaultRoomRegisterCommand();
    Room room = roomService.register(command);

    //then
    RoomUpdateCommand roomUpdateCommand =
        new RoomUpdateCommand(fakeHost.getId(), room.getId(), "변경할 숙소 이름", "변경할 숙소 설명입니다", 25000);
    assertThatNotFoundException()
        .isThrownBy(() -> roomService.update(roomUpdateCommand));
  }

  @Test
  void 숙소_삭제_성공() {
    //given
    RoomRegisterCommand command = defaultRoomRegisterCommand();

    //when
    Room room = roomService.register(command);
    roomService.delete(Command.ofDeleteRoom(host.getId(), room.getId()));

    //then
    assertThatNotFoundException()
        .isThrownBy(() -> roomService.getById(room.getId()));
  }

  @Test
  void 여행_중_혹은_예약이_존재하는_숙소_삭제_실패() {
    //given
    RoomRegisterCommand command = defaultRoomRegisterCommand();
    Room room = roomService.register(command);

    TripEvent tripEvent
        = Command.ofReserveTrip(new Trip(guest, room, checkIn, checkOut, totalPrice, headCount));
    tripService.reserve(tripEvent);

    //then
    RoomDeleteCommand roomDeleteCommand = Command.ofDeleteRoom(host.getId(), room.getId());
    assertThatIllegalArgumentException()
        .isThrownBy(() -> roomService.delete(roomDeleteCommand));
  }

  @Test
  void 호스트가_아닌_경우_숙소_삭제_실패() {
    //given
    RoomRegisterCommand command = defaultRoomRegisterCommand();
    Room room = roomService.register(command);

    //then
    RoomDeleteCommand roomDeleteCommand = Command.ofDeleteRoom(fakeHost.getId(), room.getId());
    assertThatIllegalArgumentException()
        .isThrownBy(() -> roomService.delete(roomDeleteCommand));
  }

  private RoomRegisterCommand defaultRoomRegisterCommand() {
    return Command.ofRegisterRoom(createRoom(), roomPhotos, host.getId());
  }

  @Test
  void 숙소_상세_조회_성공() {
    //given
    RoomRegisterCommand command = defaultRoomRegisterCommand();
    Room room = roomService.register(command);

    //when
    Room detailRoom = roomService.getDetailById(room.getId());

    //then
    assertThat(detailRoom).isEqualTo(room);
    assertThat(detailRoom.getRoomPhotos()).containsAll(roomPhotos);
    assertThat(detailRoom.getHost()).isEqualTo(host);
  }

  @Test
  void 숙소_상세_조회_실패() {
    //given
    Long invalidRoomId = -1L;

    //then
    assertThatNotFoundException().isThrownBy(() -> roomService.getDetailById(invalidRoomId));
  }
}
