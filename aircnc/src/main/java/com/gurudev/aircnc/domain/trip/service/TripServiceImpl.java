package com.gurudev.aircnc.domain.trip.service;

import com.gurudev.aircnc.domain.member.entity.Member;
import com.gurudev.aircnc.domain.member.repository.MemberRepository;
import com.gurudev.aircnc.domain.room.entity.Room;
import com.gurudev.aircnc.domain.room.repository.RoomRepository;
import com.gurudev.aircnc.domain.trip.entity.Trip;
import com.gurudev.aircnc.domain.trip.entity.TripStatus;
import com.gurudev.aircnc.domain.trip.repository.TripRepository;
import com.gurudev.aircnc.domain.trip.service.command.TripCommand.TripReserveCommand;
import com.gurudev.aircnc.exception.NotFoundException;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
  public Trip getDetailedById(Long id, Long guestId) {

    return tripRepository.findByIdAndGuestId(id, guestId)
        .orElseThrow(() -> new NotFoundException(Trip.class));
  }

  @Override
  public List<Trip> getByGuestId(Long guestId) {
    return tripRepository.findByGuestId(guestId);
  }

  @Override
  public Trip cancel(Long tripId, Long guestId) {

    Trip trip = findTripByIdAndGuestId(tripId, guestId);

    trip.cancel();

    return trip;
  }

  private Room findRoomById(Long roomId) {
    return roomRepository.findById(roomId).orElseThrow(() -> new NotFoundException(Room.class));
  }

  private Member findMemberById(Long memberId) {
    return memberRepository.findById(memberId)
        .orElseThrow(() -> new NotFoundException(Member.class));
  }

  private Trip findTripByIdAndGuestId(Long id, Long guestId) {
    return tripRepository.findTripByIdAndGuestId(id, guestId)
        .orElseThrow(() -> new NotFoundException(Trip.class));
  }

  @Override
  public List<Trip> findByRoomIdAndTripStatus(Long roomId, Set<TripStatus> tripStatuses) {
    PageRequest pageRequest = PageRequest.of(0, 2000, Sort.by("checkIn").descending());
    return tripRepository.findByRoomIdAndStatusSet(roomId, tripStatuses, pageRequest);
  }
}
