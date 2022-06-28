package com.gurudev.aircnc.domain.trip.service;

import com.gurudev.aircnc.domain.trip.entity.Trip;
import com.gurudev.aircnc.domain.trip.service.command.TripCommand.TripReserveCommand;
import java.util.List;

public interface TripService {

  Trip reserve(TripReserveCommand command);

  Trip getByIdAndGuestId(Long id, Long guestId);

  List<Trip> getByGuestId(Long guestId);

  Trip cancel(Long tripId);

  void bulkStatusToTravelling();

  void bulkStatusToDone();

}
