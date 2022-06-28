package com.gurudev.aircnc.domain.trip.repository;

import com.gurudev.aircnc.domain.member.entity.Member;
import com.gurudev.aircnc.domain.trip.entity.Trip;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

  @Modifying(clearAutomatically = true)
  @Query("update Trip t "
      + "set t.status = com.gurudev.aircnc.domain.trip.entity.TripStatus.TRAVELLING "
      + "where t.checkIn = :date "
      + "and t.status = com.gurudev.aircnc.domain.trip.entity.TripStatus.RESERVED")
  int bulkStatusToTravelling(@Param("date") LocalDate date);


}
