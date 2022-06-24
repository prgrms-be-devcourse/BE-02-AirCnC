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

import com.gurudev.aircnc.domain.member.entity.Member;
import com.gurudev.aircnc.domain.member.service.MemberService;
import com.gurudev.aircnc.domain.room.entity.Room;
import com.gurudev.aircnc.domain.room.entity.RoomPhoto;
import com.gurudev.aircnc.domain.room.service.RoomService;
import com.gurudev.aircnc.domain.trip.entity.Trip;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
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

  private Trip trip1;
  private Trip trip2;

  @BeforeEach
  void setUp() {
    Member host = createHost();
    memberService.register(host);

    room = createRoom();
    roomPhoto = createRoomPhoto();
    roomService.register(room, List.of(roomPhoto), host.getId());

    guest = createGuest();
    memberService.register(guest);

    checkIn = now().plusDays(1);
    checkOut = now().plusDays(2);
    headCount = room.getCapacity();
    totalPrice = between(checkIn, checkOut).getDays() * room.getPricePerDay();

    trip1 = tripService.reserve(guest, room.getId(), checkIn, checkOut, headCount, totalPrice);
    trip2 = tripService.reserve(guest, room.getId(), checkIn, checkOut, headCount, totalPrice);
  }

  @Test
  void 여행_생성_성공() {
    Trip trip = tripService.reserve(guest, room.getId(), checkIn, checkOut, headCount,
        totalPrice);

    assertThat(trip.getId()).isNotNull();
    assertThat(trip).extracting(Trip::getGuest, Trip::getRoom, Trip::getCheckIn, Trip::getCheckOut,
            Trip::getTotalPrice, Trip::getHeadCount, Trip::getStatus)
        .isEqualTo(List.of(guest, room, checkIn, checkOut, totalPrice, headCount, RESERVED));
  }

  @Test
  void 게스트의_여행_목록_조회() {
    List<Trip> findTrips = tripService.getByGuest(guest);

    assertThat(findTrips).hasSize(2).containsExactly(trip1, trip2);
  }

  @Test
  void 여행_상세_조회() {
    Trip trip = tripService.getById(trip1.getId());

    assertThat(trip).isEqualTo(trip1);
    assertThat(trip.getGuest()).isEqualTo(guest);
    assertThat(trip.getRoom()).isEqualTo(room);
    assertThat(trip.getRoom().getRoomPhotos()).containsExactly(roomPhoto);
  }

  @Test
  void 없는_아이디로_여행_상세_조회_실패() {
    Long invalidTripId = -1L;

    assertThatNotFoundException()
        .isThrownBy(() -> tripService.getById(invalidTripId));
  }

  @Test
  void 예약_상태의_여행_취소_성공() {
    Trip cancelledTrip = tripService.cancel(guest, trip1.getId());

    assertThat(cancelledTrip.getStatus()).isEqualTo(CANCELLED);
  }
}
