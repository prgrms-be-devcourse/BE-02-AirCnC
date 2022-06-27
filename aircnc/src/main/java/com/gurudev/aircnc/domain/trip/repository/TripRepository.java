package com.gurudev.aircnc.domain.trip.repository;

import com.gurudev.aircnc.domain.member.entity.Member;
import com.gurudev.aircnc.domain.trip.entity.Trip;
import com.gurudev.aircnc.domain.trip.entity.TripStatus;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TripRepository extends JpaRepository<Trip, Long> {

  List<Trip> findByGuest(Member guest);

  @Query("select t "
      + "from Trip t "
      + "join fetch t.guest "
      + "join fetch t.room r "
      + "join fetch r.roomPhotos "
      + "where t.id = :id")
  Optional<Trip> findById(Long id);

  @Query("select t "
      + "from Trip t "
      + "join fetch t.guest "
      + "where t.id = :id")
  Optional<Trip> findByIdFetchGuest(Long id);

  @Query("select t "
      + "from Trip t "
      + "where t.room.id = :roomId and t.status in :statusSet")
  List<Trip> findByRoomIdAndStatusSet(Long roomId, Set<TripStatus> statusSet, Pageable pageable);
}
