package com.gurudev.aircnc.domain.trip.service;

import static com.gurudev.aircnc.domain.trip.entity.TripStatus.RESERVED;
import static org.assertj.core.api.Assertions.assertThat;

import com.gurudev.aircnc.domain.trip.entity.Trip;
import com.gurudev.aircnc.domain.trip.service.command.TripCommand.TripReserveCommand;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TripServiceForSchedulerImplTest extends BaseTripServiceTest {

  @Autowired
  protected TripServiceForScheduler tripServiceForScheduler;

  @Test
  void 여행_상태_bulk_업데이트_테스트() {
    // given
    TripReserveCommand command1 = defaultTripReserveCommand();
    TripReserveCommand command2 = defaultTripReserveCommand();

    tripService.reserve(command1);
    tripService.reserve(command2);

    // when
    tripServiceForScheduler.bulkStatusToTravelling();

    // then
    List<Trip> trips = tripService.getByGuestId(guest.getId());

    assertThat(trips).extracting(Trip::getStatus)
        .hasSize(2)
        .containsExactly(RESERVED, RESERVED);
  }

}