package com.gurudev.aircnc.domain.trip.service;

import com.gurudev.aircnc.domain.trip.repository.TripRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class TripServiceForSchedulerImpl implements TripServiceForScheduler {

  private final TripRepository tripRepository;

  @Override
  public void bulkStatusToTravelling() {
    int count = tripRepository.bulkStatusToTravelling(LocalDate.now());


    log.info("{}개의 여행 : 여행 예약 -> 여행 중", count);
  }

  @Override
  public void bulkStatusToDone() {
    int count = tripRepository.bulkStatusToDone(LocalDate.now().minusDays(1));

    log.info("{}개의 여행 : 여행 중 -> 여행 완료", count);
  }
}
