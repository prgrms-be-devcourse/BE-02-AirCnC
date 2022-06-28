package com.gurudev.aircnc.domain.trip.service;

import com.gurudev.aircnc.domain.member.entity.Member;
import com.gurudev.aircnc.domain.member.repository.MemberRepository;
import com.gurudev.aircnc.domain.room.entity.Room;
import com.gurudev.aircnc.domain.room.repository.RoomRepository;
import com.gurudev.aircnc.domain.trip.entity.Trip;
import com.gurudev.aircnc.domain.trip.repository.TripRepository;
import com.gurudev.aircnc.domain.trip.service.command.TripCommand.TripReserveCommand;
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
  private final MemberRepository memberRepository;
  private final RoomRepository roomRepository;

  @Transactional
  @Override
  public Trip reserve(TripReserveCommand command) {
    Room room = findRoomById(command.getRoomId());
    Member guest = findMemberById(command.getGuestId());

    //TODO: 예약 겹치는지 검증 로직 필요

    return tripRepository.save(
        new Trip(guest,
            room,
            command.getCheckIn(),
            command.getCheckOut(),
            command.getTotalPrice(),
            command.getHeadCount())
    );
  }

  @Override
  public Trip getById(Long id) {
    return findById(id);
  }

  @Override
  public List<Trip> getByGuestId(Long guestId) {
    return tripRepository.findByGuestId(guestId);
  }

  @Override
  public Trip cancel(Long tripId) {
    Trip trip = findTripByIdFetchGuest(tripId);

    trip.cancel();

    return trip;
  }

  @Override
  public void bulkStatusToTravelling() {
    tripRepository.bulkStatusToTravelling(LocalDate.now());
  }

  @Override
  public void bulkStatusToDone() {
    tripRepository.bulkStatusToDone(LocalDate.now().minusDays(1));
  }

  private Trip findById(Long id) {
    return tripRepository.findById(id).orElseThrow(() -> new NotFoundException(Trip.class));
  }

  private Room findRoomById(Long roomId) {
    return roomRepository.findById(roomId).orElseThrow(() -> new NotFoundException(Room.class));
  }

  private Member findMemberById(Long memberId) {
    return memberRepository.findById(memberId)
        .orElseThrow(() -> new NotFoundException(Member.class));
  }

  private Trip findTripByIdFetchGuest(Long id) {
    return tripRepository.findByIdFetchGuest(id)
        .orElseThrow(() -> new NotFoundException(Trip.class));
  }
}
