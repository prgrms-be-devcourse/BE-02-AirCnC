package com.gurudev.aircnc.domain.trip.repository;

import com.gurudev.aircnc.domain.member.entity.Member;
import com.gurudev.aircnc.domain.trip.entity.Trip;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TripRepository extends JpaRepository<Trip, Long> {

  List<Trip> findByGuest(Member guest);

  @Query("select t "
      + "from Trip t "
      + "join fetch t.guest "
      + "where t.id = :id")
  Optional<Trip> findByIdFetchGuest(Long id);
}
