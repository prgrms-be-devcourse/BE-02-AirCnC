package com.gurudev.aircnc.domain.trip.service;

import com.gurudev.aircnc.domain.trip.entity.Trip;
import com.gurudev.aircnc.domain.trip.entity.TripStatus;
import com.gurudev.aircnc.infrastructure.event.TripEvent;
import java.time.LocalDate;
import java.util.List;

public interface TripService {

  Trip reserve(TripEvent command);

  Trip getDetailedById(Long id, Long guestId);

  List<Trip> getByGuestId(Long guestId);

  Trip cancel(Long tripId, Long guestId);

  List<LocalDate> getReservedDaysById(Long roomId);
}
