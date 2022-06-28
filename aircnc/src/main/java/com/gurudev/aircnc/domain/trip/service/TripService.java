package com.gurudev.aircnc.domain.trip.service;

import com.gurudev.aircnc.domain.member.entity.Member;
import com.gurudev.aircnc.domain.trip.entity.Trip;
import com.gurudev.aircnc.domain.trip.service.command.TripCommand.TripReserveCommand;
import java.util.List;

public interface TripService {

  Trip reserve(TripReserveCommand command);

  Trip getById(Long id);

  List<Trip> getByGuest(Member guest);

  Trip cancel(Long tripId);

  void bulkStatusToTravelling();

  void bulkStatusToDone();

}
