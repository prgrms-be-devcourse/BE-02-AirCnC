package com.gurudev.aircnc.domain.trip.entity;

import static com.gurudev.aircnc.domain.trip.entity.Trip.TRIP_HEADCOUNT_MIN_VALUE;
import static com.gurudev.aircnc.domain.trip.entity.Trip.TRIP_TOTAL_PRICE_MIN_VALUE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import com.gurudev.aircnc.domain.member.entity.Member;
import com.gurudev.aircnc.domain.room.Room;
import com.gurudev.aircnc.domain.util.Fixture;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class TripTest {

  @Test
  void Trip_생성_성공() {
    Member guest = Fixture.createGuest();
    Room room = Fixture.createRoom();
    LocalDate checkIn = LocalDate.now().plusDays(1);
    LocalDate checkOut = LocalDate.now().plusDays(2);

    Trip trip = new Trip(guest, room, checkIn, checkOut,
        TRIP_TOTAL_PRICE_MIN_VALUE, TRIP_HEADCOUNT_MIN_VALUE, TripStatus.RESERVED);

    assertThat(trip).extracting(Trip::getGuest, Trip::getRoom, Trip::getCheckIn, Trip::getCheckOut,
            Trip::getTotalPrice, Trip::getHeadCount, Trip::getStatus)
        .isEqualTo(List.of(guest, room, TRIP_TOTAL_PRICE_MIN_VALUE, TRIP_HEADCOUNT_MIN_VALUE,
            TripStatus.RESERVED));
  }

  @Test
  void CheckIn이_CheckOut_이전_이여야_한다() {
    Member guest = Fixture.createGuest();
    Room room = Fixture.createRoom();

    assertThatIllegalArgumentException()
        .isThrownBy(() -> new Trip(guest, room, LocalDate.now(), LocalDate.now().minusDays(1),
            TRIP_TOTAL_PRICE_MIN_VALUE, TRIP_HEADCOUNT_MIN_VALUE, TripStatus.RESERVED));
  }

  @Test
  void 인원이_제한_인원_이하_일_수_없다() {
    Member guest = Fixture.createGuest();
    Room room = Fixture.createRoom();

    assertThatIllegalArgumentException()
        .isThrownBy(
            () -> new Trip(guest, room, LocalDate.now().plusDays(1), LocalDate.now().plusDays(2),
                TRIP_TOTAL_PRICE_MIN_VALUE, TRIP_HEADCOUNT_MIN_VALUE - 1, TripStatus.RESERVED));
  }

  @Test
  void 총_가격은_제한가격_미만일_수_없다() {
    Member guest = Fixture.createGuest();
    Room room = Fixture.createRoom();

    assertThatIllegalArgumentException()
        .isThrownBy(
            () -> new Trip(guest, room, LocalDate.now().plusDays(1), LocalDate.now().plusDays(2),
                TRIP_TOTAL_PRICE_MIN_VALUE - 1, TRIP_HEADCOUNT_MIN_VALUE, TripStatus.RESERVED));
  }
}