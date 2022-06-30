package com.gurudev.aircnc.domain.trip.service;

import com.gurudev.aircnc.domain.trip.entity.Trip;
import com.gurudev.aircnc.domain.trip.service.command.TripCommand.TripEvent;
import java.util.List;

public interface TripService {

  Trip reserve(TripEvent event);

  Trip getDetailedById(Long id, Long guestId);

  List<Trip> getByGuestId(Long guestId);

  Trip cancel(Long tripId, Long guestId);

}
