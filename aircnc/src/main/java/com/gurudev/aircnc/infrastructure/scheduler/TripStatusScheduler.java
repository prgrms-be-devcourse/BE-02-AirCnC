package com.gurudev.aircnc.infrastructure.scheduler;

import com.gurudev.aircnc.domain.trip.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * checkIn, checkOut 스케쥴러
 */
@Component
@RequiredArgsConstructor
public class TripStatusScheduler {

  private final TripService tripService;

  /**
   * checkIn 스케줄러 오전 12시가 되면 해당 날짜에 해당하는 RESERVED -> Traverling
   **/
  @Scheduled(cron = "0 0 12 * * *")
  public void startCheckIn() {
    tripService.checkInTrips();
  }

}
