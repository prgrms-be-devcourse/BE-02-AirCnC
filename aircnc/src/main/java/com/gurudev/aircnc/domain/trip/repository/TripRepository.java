package com.gurudev.aircnc.domain.trip.repository;

import com.gurudev.aircnc.domain.trip.entity.Trip;
import com.gurudev.aircnc.domain.trip.entity.TripStatus;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TripRepository extends JpaRepository<Trip, Long> {

  List<Trip> findByGuestId(Long guestId);

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

  @Modifying(clearAutomatically = true)
  @Query("update Trip t "
      + "set t.status = com.gurudev.aircnc.domain.trip.entity.TripStatus.TRAVELLING "
      + "where t.checkIn = :date "
      + "and t.status = com.gurudev.aircnc.domain.trip.entity.TripStatus.RESERVED")
  int bulkStatusToTravelling(@Param("date") LocalDate date);

  @Modifying(clearAutomatically = true)
  @Query("update Trip t "
      + "set t.status = com.gurudev.aircnc.domain.trip.entity.TripStatus.DONE "
      + "where t.checkOut = :date "
      + "and t.status = com.gurudev.aircnc.domain.trip.entity.TripStatus.TRAVELLING")
  int bulkStatusToDone(@Param("date") LocalDate date);

  @Query("select t "
      + "from Trip t "
      + "where t.id = :id and t.guest.id = :guestId")
  Optional<Trip> findByIdAndGuestId(Long id, Long guestId);
}
