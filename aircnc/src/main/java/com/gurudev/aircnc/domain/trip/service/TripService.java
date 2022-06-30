package com.gurudev.aircnc.domain.trip.service;

import com.gurudev.aircnc.domain.trip.entity.Trip;
import com.gurudev.aircnc.domain.trip.entity.TripStatus;
import com.gurudev.aircnc.domain.trip.service.command.TripCommand.TripReserveCommand;
import java.util.List;
import java.util.Set;

public interface TripService {

  Trip reserve(TripReserveCommand command);

  Trip getDetailedById(Long id, Long guestId);

  List<Trip> getByGuestId(Long guestId);

  Trip cancel(Long tripId, Long guestId);

  List<Trip> findByRoomIdAndTripStatus(Long roomId, Set<TripStatus> reserved);
}
