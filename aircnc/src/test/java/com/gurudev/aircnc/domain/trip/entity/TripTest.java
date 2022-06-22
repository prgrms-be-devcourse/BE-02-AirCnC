package com.gurudev.aircnc.domain.trip.entity;

import static com.gurudev.aircnc.domain.trip.entity.Trip.TRIP_HEADCOUNT_MIN_VALUE;
import static com.gurudev.aircnc.domain.trip.entity.Trip.TRIP_TOTAL_PRICE_MIN_VALUE;
import static com.gurudev.aircnc.domain.trip.entity.TripStatus.RESERVED;
import static com.gurudev.aircnc.domain.trip.entity.TripStatus.TRAVELLING;
import static com.gurudev.aircnc.domain.util.Fixture.createGuest;
import static com.gurudev.aircnc.domain.util.Fixture.createRoom;
import static java.time.LocalDate.now;
import static java.time.Period.between;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.gurudev.aircnc.domain.member.entity.Member;
import com.gurudev.aircnc.domain.room.entity.Room;
import com.gurudev.aircnc.exception.TripReservationException;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TripTest {

  private final Member guest = createGuest();
  private final Room room = createRoom();
  private LocalDate checkIn;
  private LocalDate checkOut;
  private int totalPrice;
  private int headCount;

  @BeforeEach
  void setUp() {
    checkIn = now().plusDays(1);
    checkOut = now().plusDays(2);

    totalPrice = between(checkIn, checkOut).getDays() * room.getPricePerDay();
    headCount = room.getCapacity();
  }

  @Test
  void Trip_생성_성공() {
    Trip trip = Trip.ofReserved(guest, room, checkIn, checkOut, totalPrice, headCount);

    assertThat(trip).extracting(Trip::getGuest, Trip::getRoom, Trip::getCheckIn, Trip::getCheckOut,
            Trip::getTotalPrice, Trip::getHeadCount, Trip::getStatus)
        .isEqualTo(List.of(guest, room, checkIn, checkOut, totalPrice, headCount, RESERVED));
  }

  @Test
  void Trip_status를_변경할_수_있다() {
    Trip trip = Trip.ofReserved(guest, room, checkIn, checkOut, totalPrice, headCount);

    trip.changeStatus(TRAVELLING);

    assertThat(trip.getStatus()).isEqualTo(TRAVELLING);
  }

  @Test
  void CheckIn이_CheckOut_이전_이여야_한다() {
    assertThatIllegalArgumentException()
        .isThrownBy(
            () -> Trip.ofReserved(guest, room, checkIn, checkIn.minusDays(1), totalPrice,
                headCount));
  }

  @Test
  void 인원이_제한_인원_이하_일_수_없다() {
    assertThatIllegalArgumentException()
        .isThrownBy(
            () -> Trip.ofReserved(guest, room, checkIn, checkOut, totalPrice,
                TRIP_HEADCOUNT_MIN_VALUE - 1));
  }

  @Test
  void 총_가격은_제한가격_미만일_수_없다() {
    assertThatIllegalArgumentException()
        .isThrownBy(
            () -> Trip.ofReserved(guest, room, checkIn, checkOut, TRIP_TOTAL_PRICE_MIN_VALUE - 1,
                headCount));
  }

  @Test
  void 계산된_가격과_요청된_가격이_같아야_한다() {
    assertThatThrownBy(() -> Trip.ofReserved(guest, room, checkIn, checkOut, totalPrice + 1,
        headCount)).isInstanceOf(
        TripReservationException.class);
  }

  @Test
  void 여행_인원_수는_숙소의_최대_인원을_초과_할_수_없다() {
    assertThatThrownBy(() -> Trip.ofReserved(guest, room, checkIn, checkOut, totalPrice,
        room.getCapacity() + 1)).isInstanceOf(
        TripReservationException.class);
  }
}