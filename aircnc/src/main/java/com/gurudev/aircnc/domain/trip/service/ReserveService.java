package com.gurudev.aircnc.domain.trip.service;

import com.gurudev.aircnc.domain.trip.service.command.TripCommand.TripReserveCommand;
import com.gurudev.aircnc.infrastructure.event.EventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReserveService {

  private final EventPublisher eventPublisher;

  public TripReserveCommand reserve(TripReserveCommand tripReserveCommand) {
    eventPublisher.publishTripEvent(tripReserveCommand.toTripEvent());

    return tripReserveCommand;
  }

}
