package com.gurudev.aircnc.domain.trip.service.command;


import static com.gurudev.aircnc.domain.trip.service.command.TripCommand.TripReserveCommand.TRIP_TOTAL_PRICE_MIN_VALUE;
import static java.time.LocalDate.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import com.gurudev.aircnc.domain.trip.service.command.TripCommand.TripReserveCommand;
import java.time.LocalDate;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class TripCommandTest {

  @Nested
  class 여행_예약_명령 {

    @Test
    void 여행_예약_명령_생성_성공() {
      //when
      TripReserveCommand command =
          new TripReserveCommand(1L, 2L, now(), now().plusDays(1L), 2, 10000
          );

      //then
      assertThat(command).isNotNull()
          .extracting(TripReserveCommand::getGuestId,
              TripReserveCommand::getRoomId,
              TripReserveCommand::getCheckIn,
              TripReserveCommand::getCheckOut,
              TripReserveCommand::getHeadCount,
              TripReserveCommand::getTotalPrice)
          .containsExactly(1L, 2L, now(), now().plusDays(1L), 2, 10000);
    }

    @Test
    void CheckIn이_CheckOut_이전_이여야_한다() {
      //given
      LocalDate invalidCheckOut = now().minusDays(1);

      //then
      assertThatIllegalArgumentException().isThrownBy(() ->
          new TripReserveCommand(1L, 2L, now(), invalidCheckOut, 2, 10000));
    }

    @Test
    void 인원이_0명_이하_일_수_없다() {
      //given
      int invalidHeadCount = 0;

      //then
      assertThatIllegalArgumentException().isThrownBy(() ->
          new TripReserveCommand(1L, 2L, now(), now().plusDays(1L), invalidHeadCount, 10000));
    }

    @Test
    void 총_가격은_제한가격_미만일_수_없다() {
      //given
      int invalidTotalPrice = TRIP_TOTAL_PRICE_MIN_VALUE - 1;

      //then
      assertThatIllegalArgumentException().isThrownBy(() ->
          new TripReserveCommand(1L, 2L, now(), now().plusDays(1L), 2, invalidTotalPrice));
    }

  }
}