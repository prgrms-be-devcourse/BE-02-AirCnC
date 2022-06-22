package com.gurudev.aircnc.domain.trip.service;

import com.gurudev.aircnc.domain.member.entity.Member;
import com.gurudev.aircnc.domain.room.entity.Room;
import com.gurudev.aircnc.domain.room.repository.RoomRepository;
import com.gurudev.aircnc.domain.trip.entity.Trip;
import com.gurudev.aircnc.domain.trip.repository.TripRepository;
import com.gurudev.aircnc.exception.NotFoundException;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TripServiceImpl implements TripService {

  private final TripRepository tripRepository;
  private final RoomRepository roomRepository;

  @Transactional
  @Override
  public Trip reserve(Member guest, Long roomId, LocalDate checkIn, LocalDate checkOut,
      int headCount, int totalPrice) {
    Room room = roomRepository.findById(roomId)
        .orElseThrow(() -> new NotFoundException(Room.class));

    return tripRepository.save(
        Trip.ofReserved(guest, room, checkIn, checkOut, totalPrice, headCount));
  }
}
