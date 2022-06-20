package com.gurudev.aircnc.domain.trip.repository;

import com.gurudev.aircnc.domain.trip.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripRepository extends JpaRepository<Trip, Long> {

}
