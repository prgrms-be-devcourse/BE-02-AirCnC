package com.gurudev.aircnc.infrastructure.scheduler;

import com.gurudev.aircnc.domain.trip.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 여행 상태 스케쥴러
 */
@Component
@RequiredArgsConstructor
public class TripStatusScheduler {

  private final TripService tripService;

  /**
   * checkIn 스케쥴 오전 12시가 되면 해당 날짜에 해당하는 RESERVED -> TRAVELLING
   **/
  @Scheduled(cron = "0 0 0 * * *")
  public void bulkStatusUpdateToTravelling() {
    tripService.bulkStatusToTravelling();
  }

  /**
   * checkOut 스케쥴 오전 12시가 되면 해당 날짜에 해당하는 RESERVED -> TRAVELLING
   **/
  @Scheduled(cron = "0 0 0 * * *")
  public void bulkStatusUpdateToDone() {
    tripService.bulkStatusToDone();
  }

}
