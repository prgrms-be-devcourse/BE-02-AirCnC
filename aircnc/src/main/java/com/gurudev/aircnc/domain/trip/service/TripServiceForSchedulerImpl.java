package com.gurudev.aircnc.domain.trip.service;

import com.gurudev.aircnc.domain.trip.repository.TripRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class TripServiceForSchedulerImpl implements
    TripServiceForScheduler {

  private final TripRepository tripRepository;

  @Override
  public void bulkStatusToTravelling() {
    tripRepository.bulkStatusToTravelling(LocalDate.now());
  }

  @Override
  public void bulkStatusToDone() {
    tripRepository.bulkStatusToDone(LocalDate.now().minusDays(1));
  }
}
