package com.gurudev.aircnc.domain.trip.service;

import com.gurudev.aircnc.domain.member.entity.Member;
import com.gurudev.aircnc.domain.trip.entity.Trip;
import java.time.LocalDate;

public interface TripService {

  Trip reserve(Member guest, Long roomId, LocalDate checkIn, LocalDate checkOut, int headCount,
      int totalPrice);

}
