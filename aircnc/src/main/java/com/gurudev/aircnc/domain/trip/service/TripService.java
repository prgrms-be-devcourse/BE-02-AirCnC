package com.gurudev.aircnc.domain.trip.service;

import com.gurudev.aircnc.domain.trip.entity.Trip;
import com.gurudev.aircnc.infrastructure.event.TripEvent;
import java.util.List;

public interface TripService {

  Trip reserve(TripReserveCommand command);

  Trip getDetailedById(Long id, Long guestId);

  List<Trip> getByGuestId(Long guestId);

  Trip cancel(Long tripId, Long guestId);

  List<Trip> findByRoomIdAndTripStatus(Long roomId, Set<TripStatus> reserved);
}
