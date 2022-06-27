package com.gurudev.aircnc.domain.trip.service;

import static com.gurudev.aircnc.domain.trip.entity.TripStatus.CANCELLED;
import static com.gurudev.aircnc.domain.trip.entity.TripStatus.RESERVED;

import static com.gurudev.aircnc.domain.util.Fixture.createGuest;
import static com.gurudev.aircnc.domain.util.Fixture.createHost;
import static com.gurudev.aircnc.domain.util.Fixture.createRoom;
import static com.gurudev.aircnc.domain.util.Fixture.createRoomPhoto;
import static com.gurudev.aircnc.util.AssertionUtil.assertThatNotFoundException;
import static java.time.LocalDate.now;
import static java.time.Period.between;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

import com.gurudev.aircnc.domain.member.entity.Member;
import com.gurudev.aircnc.domain.member.service.MemberService;
import com.gurudev.aircnc.domain.room.entity.Room;
import com.gurudev.aircnc.domain.room.entity.RoomPhoto;
import com.gurudev.aircnc.domain.room.service.RoomService;
import com.gurudev.aircnc.domain.trip.entity.Trip;
import com.gurudev.aircnc.domain.util.Command;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest(webEnvironment = NONE)
class TripServiceImplTest {

  @Autowired
  private TripService tripService;

  @Autowired
  private MemberService memberService;

  @Autowired
  private RoomService roomService;

  private Room room;
  private RoomPhoto roomPhoto;

  private Member guest;

  private LocalDate checkIn;
  private LocalDate checkOut;

  private int headCount;
  private int totalPrice;

  @BeforeEach
  void setUp() {
    Member host = memberService.register(Command.ofRegisterMember(createHost()));

    guest = createGuest();
    room = createRoom();
    roomPhoto = createRoomPhoto();
    room = roomService.register(Command.ofRegisterRoom(room, List.of(roomPhoto), host.getId()));

    guest = memberService.register(Command.ofRegisterMember(guest));

    checkIn = now().plusDays(1);
    checkOut = now().plusDays(2);
    headCount = room.getCapacity();
    
    totalPrice = between(checkIn, checkOut).getDays() * room.getPricePerDay();
  }

  @Test
  void 여행_생성_성공() {
    //when
    Trip trip = tripService.reserve(guest, room.getId(), checkIn, checkOut, headCount, totalPrice);

    //then
    assertThat(trip.getId()).isNotNull();
    assertThat(trip).extracting(Trip::getGuest, Trip::getRoom, Trip::getCheckIn, Trip::getCheckOut,
            Trip::getTotalPrice, Trip::getHeadCount, Trip::getStatus)
        .isEqualTo(List.of(guest, room, checkIn, checkOut, totalPrice, headCount, RESERVED));
  }

  @Test
  void 게스트의_여행_목록_조회() {
    //given
    Trip trip1 = tripService.reserve(guest, room.getId(), checkIn, checkOut, headCount, totalPrice);
    Trip trip2 = tripService.reserve(guest, room.getId(), checkIn, checkOut, headCount, totalPrice);

    //when
    List<Trip> findTrips = tripService.getByGuest(guest);

    //then
    assertThat(findTrips).hasSize(2).containsExactly(trip1, trip2);
  }

  @Test
  void 여행_상세_조회() {

    //given
    Trip trip1 = tripService.reserve(guest, room.getId(), checkIn, checkOut, headCount, totalPrice);

    //when
    Trip trip = tripService.getById(trip1.getId());

    //then
    assertThat(trip).isEqualTo(trip1);
    assertThat(trip.getGuest()).isEqualTo(guest);
    assertThat(trip.getRoom()).isEqualTo(room);
    assertThat(trip.getRoom().getRoomPhotos()).containsExactly(roomPhoto);
  }

  @Test
  void 없는_아이디로_여행_상세_조회_실패() {
    //given
    Long invalidTripId = -1L;

    //then
    assertThatNotFoundException()
        .isThrownBy(() -> tripService.getById(invalidTripId));
  }

  @Test
  void 예약_상태의_여행_취소_성공() {
    //given
    Trip reservedTrip =
        tripService.reserve(guest, room.getId(), checkIn, checkOut, headCount, totalPrice);

    //when
    Trip cancelledTrip = tripService.cancel(reservedTrip.getId());

    //then
    assertThat(cancelledTrip.getStatus()).isEqualTo(CANCELLED);
  }

  @Test
  void trip_status_변경_테스트() {
    // given
    for (int i = 0; i < 5; ++i) {
      totalPrice = between(LocalDate.now(), checkOut).getDays() * room.getPricePerDay();
      tripService.reserve(guest, room.getId(), LocalDate.now(), checkOut, headCount, totalPrice);
    }

    // when
    tripService.checkInTrips();

    // then
    List<Trip> trips = tripService.getByGuest(guest);

    assertThat(trips).extracting(Trip::getStatus).allMatch(status -> status == TRAVELLING);


  }
}
