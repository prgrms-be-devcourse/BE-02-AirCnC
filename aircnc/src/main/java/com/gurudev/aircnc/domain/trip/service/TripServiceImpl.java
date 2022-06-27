package com.gurudev.aircnc.domain.trip.service;

import static com.gurudev.aircnc.domain.trip.entity.TripStatus.RESERVED;
import static com.gurudev.aircnc.domain.trip.entity.TripStatus.TRAVELLING;

import com.gurudev.aircnc.domain.member.entity.Member;
import com.gurudev.aircnc.domain.room.entity.Room;
import com.gurudev.aircnc.domain.room.repository.RoomRepository;
import com.gurudev.aircnc.domain.trip.entity.Trip;
import com.gurudev.aircnc.domain.trip.repository.TripRepository;
import com.gurudev.aircnc.exception.NotFoundException;
import java.time.LocalDate;
import java.util.List;
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
    Room room = findRoomById(roomId);

    //TODO: 예약 겹치는지 검증 로직 필요

    return tripRepository.save(new Trip(guest, room, checkIn, checkOut, totalPrice, headCount));
  }

  @Override
  public Trip getById(Long id) {
    return findById(id);
  }

  @Override
  public List<Trip> getByGuest(Member guest) {
    return tripRepository.findByGuest(guest);
  }

  @Override
  public Trip cancel(Long tripId) {
    Trip trip = findTripByIdFetchGuest(tripId);

    trip.cancel();

    return trip;
  }

  @Override
  public void checkInTrips() {
    tripRepository.bulkCheckIn(LocalDate.now(), RESERVED, TRAVELLING);
  }


  private Trip findById(Long id) {
    return tripRepository.findById(id).orElseThrow(() -> new NotFoundException(Trip.class));
  }

  private Room findRoomById(Long roomId) {
    return roomRepository.findById(roomId).orElseThrow(() -> new NotFoundException(Room.class));
  }

  private Trip findTripByIdFetchGuest(Long id) {
    return tripRepository.findByIdFetchGuest(id)
        .orElseThrow(() -> new NotFoundException(Trip.class));
  }
}
