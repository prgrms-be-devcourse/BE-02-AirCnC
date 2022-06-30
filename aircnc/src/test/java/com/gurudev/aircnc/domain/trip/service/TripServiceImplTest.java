package com.gurudev.aircnc.domain.trip.service;

import static com.gurudev.aircnc.domain.trip.entity.TripStatus.CANCELLED;
import static com.gurudev.aircnc.domain.trip.entity.TripStatus.RESERVED;
import static com.gurudev.aircnc.util.AssertionUtil.assertThatNotFoundException;
import static org.assertj.core.api.Assertions.assertThat;

import com.gurudev.aircnc.domain.trip.entity.Trip;
import com.gurudev.aircnc.domain.trip.service.command.TripCommand.TripReserveCommand;
import java.util.List;
import org.junit.jupiter.api.Test;

class TripServiceImplTest extends BaseTripServiceTest {

  @Test
  void 여행_예약_성공() {
    //given
    TripReserveCommand command = defaultTripReserveCommand();

    //when
    Trip trip = tripService.reserve(command);

    //then
    assertThat(trip.getId()).isNotNull();
    assertThat(trip).extracting(Trip::getGuest, Trip::getRoom, Trip::getCheckIn, Trip::getCheckOut,
            Trip::getTotalPrice, Trip::getHeadCount, Trip::getStatus)
        .isEqualTo(List.of(guest, room, checkIn, checkOut, totalPrice, headCount, RESERVED));
  }

  @Test
  void 게스트의_여행_목록_조회() {
    //given
    TripReserveCommand command1 = defaultTripReserveCommand();
    TripReserveCommand command2 = defaultTripReserveCommand();

    Trip trip1 = tripService.reserve(command1);
    Trip trip2 = tripService.reserve(command2);

    //when
    List<Trip> findTrips = tripService.getByGuestId(guest.getId());

    //then
    assertThat(findTrips).hasSize(2).containsExactly(trip1, trip2);
  }

  @Test
  void 여행_상세_조회() {
    //given
    TripReserveCommand command = defaultTripReserveCommand();
    Trip trip1 = tripService.reserve(command);

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
    TripReserveCommand command = defaultTripReserveCommand();
    Trip reservedTrip = tripService.reserve(command);

    //when
    Trip cancelledTrip = tripService.cancel(reservedTrip.getId(), guest.getId());

    //then
    assertThat(cancelledTrip.getStatus()).isEqualTo(CANCELLED);
  }

}
