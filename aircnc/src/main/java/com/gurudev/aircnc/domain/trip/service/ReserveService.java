package com.gurudev.aircnc.domain.trip.service;

import com.gurudev.aircnc.domain.trip.service.command.TripCommand.TripEvent;
import com.gurudev.aircnc.infrastructure.event.EventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReserveService {

  private final EventPublisher eventPublisher;

  public TripEvent reserve(TripEvent tripEvent) {
    eventPublisher.publishTripEvent(tripEvent);

    return tripEvent;
  }

}
