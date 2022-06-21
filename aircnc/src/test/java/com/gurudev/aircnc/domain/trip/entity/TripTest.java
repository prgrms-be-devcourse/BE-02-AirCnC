package com.gurudev.aircnc.domain.trip.entity;

import static com.gurudev.aircnc.domain.trip.entity.Trip.TRIP_HEADCOUNT_MIN_VALUE;
import static com.gurudev.aircnc.domain.trip.entity.Trip.TRIP_TOTAL_PRICE_MIN_VALUE;
import static com.gurudev.aircnc.domain.trip.entity.TripStatus.RESERVED;
import static com.gurudev.aircnc.domain.trip.entity.TripStatus.TRAVELLING;
import static java.time.LocalDate.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import com.gurudev.aircnc.domain.member.entity.Member;
import com.gurudev.aircnc.domain.room.entity.Room;
import com.gurudev.aircnc.domain.util.Fixture;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class TripTest {

  private final Member guest = Fixture.createGuest();
  private final Room room = Fixture.createRoom();
  private final LocalDate checkIn = now().plusDays(1);
  private final LocalDate checkOut = now().plusDays(2);

  @Test
  void Trip_생성_성공() {
    Trip trip = Trip.ofReserved(guest, room, checkIn, checkOut, TRIP_TOTAL_PRICE_MIN_VALUE,
        TRIP_HEADCOUNT_MIN_VALUE);

    assertThat(trip).extracting(Trip::getGuest, Trip::getRoom, Trip::getCheckIn, Trip::getCheckOut,
            Trip::getTotalPrice, Trip::getHeadCount, Trip::getStatus)
        .isEqualTo(List.of(guest, room, checkIn, checkOut, TRIP_TOTAL_PRICE_MIN_VALUE,
            TRIP_HEADCOUNT_MIN_VALUE, RESERVED));
  }

  @Test
  void Trip_status를_변경할_수_있다() {
    Trip trip = Trip.ofReserved(guest, room, checkIn, checkOut, TRIP_TOTAL_PRICE_MIN_VALUE,
        TRIP_HEADCOUNT_MIN_VALUE);

    trip.changeStatus(TRAVELLING);

    assertThat(trip).extracting(Trip::getGuest, Trip::getRoom, Trip::getCheckIn, Trip::getCheckOut,
            Trip::getTotalPrice, Trip::getHeadCount, Trip::getStatus)
        .isEqualTo(List.of(guest, room, checkIn, checkOut, TRIP_TOTAL_PRICE_MIN_VALUE,
            TRIP_HEADCOUNT_MIN_VALUE, TRAVELLING));
  }

  @Test
  void CheckIn이_CheckOut_이전_이여야_한다() {
    assertThatIllegalArgumentException()
        .isThrownBy(() -> Trip.ofReserved(guest, room, now(), now().minusDays(1),
            TRIP_TOTAL_PRICE_MIN_VALUE, TRIP_HEADCOUNT_MIN_VALUE));
  }

  @Test
  void 인원이_제한_인원_이하_일_수_없다() {
    assertThatIllegalArgumentException()
        .isThrownBy(
            () -> Trip.ofReserved(guest, room, now().plusDays(1), now().plusDays(2),
                TRIP_TOTAL_PRICE_MIN_VALUE, TRIP_HEADCOUNT_MIN_VALUE - 1));
  }

  @Test
  void 총_가격은_제한가격_미만일_수_없다() {
    assertThatIllegalArgumentException()
        .isThrownBy(
            () -> Trip.ofReserved(guest, room, now().plusDays(1), now().plusDays(2),
                TRIP_TOTAL_PRICE_MIN_VALUE - 1, TRIP_HEADCOUNT_MIN_VALUE));
  }
}