package com.gurudev.aircnc.domain.trip.repository;

import com.gurudev.aircnc.domain.member.entity.Member;
import com.gurudev.aircnc.domain.trip.entity.Trip;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripRepository extends JpaRepository<Trip, Long> {

  List<Trip> findByGuest(Member guest);

}
