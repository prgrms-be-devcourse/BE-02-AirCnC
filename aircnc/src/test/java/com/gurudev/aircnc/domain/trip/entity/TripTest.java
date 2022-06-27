package com.gurudev.aircnc.domain.trip.entity;

import static com.gurudev.aircnc.domain.trip.entity.Trip.TRIP_HEADCOUNT_MIN_VALUE;
import static com.gurudev.aircnc.domain.trip.entity.Trip.TRIP_TOTAL_PRICE_MIN_VALUE;
import static com.gurudev.aircnc.domain.trip.entity.TripStatus.CANCELLED;
import static com.gurudev.aircnc.domain.trip.entity.TripStatus.RESERVED;
import static com.gurudev.aircnc.domain.util.Fixture.createGuest;
import static com.gurudev.aircnc.domain.util.Fixture.createRoom;
import static com.gurudev.aircnc.domain.util.Fixture.createTrip;
import static java.time.LocalDate.now;
import static java.time.Period.between;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import com.gurudev.aircnc.domain.member.entity.Member;
import com.gurudev.aircnc.domain.room.entity.Room;
import com.gurudev.aircnc.exception.TripCancelException;
import com.gurudev.aircnc.exception.TripReservationException;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TripTest {

  private Member guest;
  private Room room;

  private LocalDate checkIn;
  private LocalDate checkOut;

  private int totalPrice;
  private int headCount;

  @BeforeEach
  void setUp() {
    guest = createGuest();
    room = createRoom();

    checkIn = now().plusDays(1);
    checkOut = now().plusDays(2);

    totalPrice = between(checkIn, checkOut).getDays() * room.getPricePerDay();
    headCount = room.getCapacity();
  }

  @Test
  void Trip_생성_성공() {
    Trip trip = new Trip(guest, room, checkIn, checkOut, totalPrice, headCount);

    assertThat(trip).extracting(Trip::getGuest, Trip::getRoom, Trip::getCheckIn, Trip::getCheckOut,
            Trip::getTotalPrice, Trip::getHeadCount, Trip::getStatus)
        .isEqualTo(List.of(guest, room, checkIn, checkOut, totalPrice, headCount, RESERVED));
  }

  @Test
  void CheckIn이_CheckOut_이전_이여야_한다() {
    LocalDate invalidCheckOut = checkIn.minusDays(1);

    assertThatIllegalArgumentException()
        .isThrownBy(() -> new Trip(guest, room, checkIn, invalidCheckOut, totalPrice, headCount));
  }

  @Test
  void 인원이_제한_인원_이하_일_수_없다() {
    assertThatIllegalArgumentException()
        .isThrownBy(() -> new Trip(guest, room, checkIn, checkOut, totalPrice,
            TRIP_HEADCOUNT_MIN_VALUE - 1));
  }

  @Test
  void 총_가격은_제한가격_미만일_수_없다() {
    int invalidTotalPrice = TRIP_TOTAL_PRICE_MIN_VALUE - 1;

    assertThatIllegalArgumentException()
        .isThrownBy(() -> new Trip(guest, room, checkIn, checkOut, invalidTotalPrice,
            headCount));
  }

  @Test
  void 계산된_가격과_요청된_가격이_같아야_한다() {
    int invalidTotalPrice = this.totalPrice + 1;

    assertThatExceptionOfType(TripReservationException.class)
        .isThrownBy(() -> new Trip(guest, room, checkIn, checkOut, invalidTotalPrice, headCount));
  }

  @Test
  void 여행_인원_수는_숙소의_최대_인원을_초과_할_수_없다() {
    int invalidCapacity = room.getCapacity() + 1;

    assertThatExceptionOfType(TripReservationException.class)
        .isThrownBy(() -> new Trip(guest, room, checkIn, checkOut, totalPrice, invalidCapacity));
  }

  @Test
  void 예약_상태의_여행_취소_성공() {
    Trip trip = createTrip();

    trip.cancel();

    assertThat(trip.getStatus()).isEqualTo(CANCELLED);
  }

  @Test
  void 취소_상태의_여행_취소_실패() {
    Trip trip = createTrip();
    trip.cancel(); // -> trip.status = CANCELLED

    assertThatExceptionOfType(TripCancelException.class)
        .isThrownBy(trip::cancel);
  }
}