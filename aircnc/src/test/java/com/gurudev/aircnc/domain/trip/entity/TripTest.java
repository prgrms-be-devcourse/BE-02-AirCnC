package com.gurudev.aircnc.domain.trip.entity;

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
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

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
    //when
    Trip trip = new Trip(guest, room, checkIn, checkOut, totalPrice, headCount);

    //then
    assertThat(trip).extracting(Trip::getGuest, Trip::getRoom, Trip::getCheckIn, Trip::getCheckOut,
            Trip::getTotalPrice, Trip::getHeadCount, Trip::getStatus)
        .isEqualTo(List.of(guest, room, checkIn, checkOut, totalPrice, headCount, RESERVED));
  }


  @Test
  void 계산된_가격과_요청된_가격이_같아야_한다() {
    //given
    int invalidTotalPrice = this.totalPrice + 1;

    //then
    assertThatIllegalArgumentException()
        .isThrownBy(() -> new Trip(guest, room, checkIn, checkOut, invalidTotalPrice, headCount));
  }

  @Test
  void 여행_인원_수는_숙소의_최대_인원을_초과_할_수_없다() {
    //given
    int invalidCapacity = room.getCapacity() + 1;

    //then
    assertThatIllegalArgumentException()
        .isThrownBy(() -> new Trip(guest, room, checkIn, checkOut, totalPrice, invalidCapacity));
  }

  @Test
  void 예약_상태의_여행_취소_성공() {
    //given
    Trip trip = createTrip();

    //when
    trip.cancel();

    //then
    assertThat(trip.getStatus()).isEqualTo(CANCELLED);
  }

  @ParameterizedTest
  @CsvSource({
      "TRAVELLING",
      "DONE",
      "CANCELLED"
  })
  void 취소_상태의_여행_취소_실패(String status) throws Exception {

    Trip trip = createTrip();
    changeTripStatus(trip, TripStatus.valueOf(status));

    //then
    assertThatExceptionOfType(TripCancelException.class)
        .isThrownBy(trip::cancel);
  }

  private Trip changeTripStatus(Trip trip, TripStatus status)
      throws Exception {
    final Field statusField = trip.getClass().getDeclaredField("status");
    statusField.setAccessible(true);
    statusField.set(trip, status);

    return trip;
  }
}
