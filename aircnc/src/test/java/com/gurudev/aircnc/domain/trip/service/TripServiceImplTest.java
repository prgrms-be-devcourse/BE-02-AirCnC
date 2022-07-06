package com.gurudev.aircnc.domain.trip.service;

import static com.gurudev.aircnc.domain.trip.entity.TripStatus.CANCELLED;
import static com.gurudev.aircnc.domain.trip.entity.TripStatus.RESERVED;
import static com.gurudev.aircnc.util.AssertionUtil.assertThatAircncRuntimeException;
import static com.gurudev.aircnc.util.AssertionUtil.assertThatNotFoundException;
import static java.time.LocalDate.now;
import static java.time.Period.between;
import static org.assertj.core.api.Assertions.assertThat;

import com.gurudev.aircnc.domain.trip.entity.Trip;
import com.gurudev.aircnc.domain.util.Command;
import com.gurudev.aircnc.infrastructure.event.TripEvent;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class TripServiceImplTest extends BaseTripServiceTest {

  @Test
  void 여행_예약_성공() {
    //given
    TripEvent tripEvent = defaultTripEvent();

    //when
    Trip trip = tripService.reserve(tripEvent);

    //then
    assertThat(trip.getId()).isNotNull();
    assertThat(trip).extracting(Trip::getGuest, Trip::getRoom, Trip::getCheckIn, Trip::getCheckOut,
            Trip::getTotalPrice, Trip::getHeadCount, Trip::getStatus)
        .isEqualTo(List.of(guest, room, checkIn, checkOut, totalPrice, headCount, RESERVED));
  }

  @Test
  void 여행기간_중복_시_예약_실패() {
    //given
    LocalDate checkIn = now().plusDays(2);
    LocalDate checkOut = now().plusDays(5);
    int totalPrice = between(checkIn, checkOut).getDays() * room.getPricePerDay();
    Trip trip = new Trip(guest, room, checkIn, checkOut, totalPrice, headCount);

    TripEvent tripEvent = Command.ofReserveTrip(trip);
    tripService.reserve(tripEvent);

    // when
    LocalDate tryCheckIn = now().plusDays(1);
    LocalDate tryCheckOut = now().plusDays(3);
    int tryTotalPrice = between(tryCheckIn, tryCheckOut).getDays() * room.getPricePerDay();
    Trip tryTrip = new Trip(guest, room, tryCheckIn, tryCheckOut, tryTotalPrice, headCount);

    //then
    TripEvent tryTripEvent = Command.ofReserveTrip(tryTrip);
    assertThatAircncRuntimeException()
        .isThrownBy(() -> tripService.reserve(tryTripEvent));
  }

  @Test
  void 게스트의_여행_목록_조회() {
    //given
    TripEvent tripEvent1 = defaultTripEvent(now(), now().plusDays(1L));
    TripEvent tripEvent2 = defaultTripEvent(now().plusDays(2L), now().plusDays(3L));

    Trip trip1 = tripService.reserve(tripEvent1);
    Trip trip2 = tripService.reserve(tripEvent2);

    //when
    List<Trip> findTrips = tripService.getByGuestId(guest.getId());

    //then
    assertThat(findTrips).hasSize(2).containsExactly(trip1, trip2);
  }

  @Test
  void 여행_상세_조회() {
    //given
    TripEvent tripEvent = defaultTripEvent();
    Trip trip1 = tripService.reserve(tripEvent);

    //when
    Trip trip = tripService.getDetailedById(trip1.getId(), guest.getId());

    //then
    assertThat(trip).isEqualTo(trip1);
    assertThat(trip.getGuest()).isEqualTo(guest);
    assertThat(trip.getRoom()).isEqualTo(room);
    assertThat(trip.getRoom().getRoomPhotos()).containsExactly(roomPhoto);
    assertThat(trip.getRoom().getHost()).isEqualTo(host);
  }

  @Test
  void 없는_아이디로_여행_상세_조회_실패() {
    //given
    Long invalidTripId = -1L;

    //then
    assertThatNotFoundException()
        .isThrownBy(() -> tripService.getDetailedById(invalidTripId, guest.getId()));
  }

  @Test
  void 예약_상태의_여행_취소_성공() {
    //given
    TripEvent tripEvent = defaultTripEvent();
    Trip reservedTrip = tripService.reserve(tripEvent);

    //when
    Trip cancelledTrip = tripService.cancel(reservedTrip.getId(), guest.getId());

    //then
    assertThat(cancelledTrip.getStatus()).isEqualTo(CANCELLED);
  }

  @Test
  void 예약_불가능한_날짜_조회() {
    // given

    //예약1
    LocalDate checkIn1 = now();
    LocalDate checkOut1 = now().plusDays(1);
    int firstTotalPrice = between(checkIn1, checkOut1).getDays() * room.getPricePerDay();
    tripService.reserve(Command.ofReserveTrip(
        new Trip(guest, room, checkIn1, checkOut1, firstTotalPrice, headCount)));
    //예약2
    LocalDate checkIn2 = now().plusDays(4);
    LocalDate checkOut2 = now().plusDays(7);
    int secondTotalPrice = between(checkIn2, checkOut2).getDays() * room.getPricePerDay();
    tripService.reserve(Command.ofReserveTrip(
        new Trip(guest, room, checkIn2, checkOut2, secondTotalPrice, headCount)));
    // when
    List<LocalDate> reservedDays = tripService.getReservedDaysById(room.getId());

    // then
    assertThat(reservedDays).containsExactly(checkIn1, checkOut1, checkIn2, checkOut2);
  }
}
