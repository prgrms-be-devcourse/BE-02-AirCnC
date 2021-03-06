package com.gurudev.aircnc.domain.trip.service;

import static com.gurudev.aircnc.domain.trip.entity.TripStatus.RESERVED;
import static com.gurudev.aircnc.domain.trip.entity.TripStatus.TRAVELLING;
import static java.time.LocalDate.now;
import static org.assertj.core.api.Assertions.assertThat;

import com.gurudev.aircnc.domain.trip.entity.Trip;
import com.gurudev.aircnc.infrastructure.event.TripEvent;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TripServiceForSchedulerImplTest extends BaseTripServiceTest {

  @Autowired
  protected TripServiceForScheduler tripServiceForScheduler;

  @Test
  void 여행_상태_bulk_업데이트_테스트() {
    // given
    TripEvent tripEvent1 = defaultTripEvent(now(), now().plusDays(1L));
    TripEvent tripEvent2 = defaultTripEvent(now().plusDays(2L), now().plusDays(3L));

    tripService.reserve(tripEvent1);
    tripService.reserve(tripEvent2);

    // when
    tripServiceForScheduler.bulkStatusToTravelling();

    // then
    List<Trip> trips = tripService.getByGuestId(guest.getId());

    assertThat(trips).extracting(Trip::getStatus)
        .hasSize(2)
        .containsExactly(TRAVELLING, RESERVED);
  }

}
