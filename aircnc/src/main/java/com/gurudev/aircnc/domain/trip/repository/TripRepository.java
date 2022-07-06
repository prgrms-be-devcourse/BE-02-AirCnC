package com.gurudev.aircnc.domain.trip.repository;

import com.gurudev.aircnc.domain.room.entity.Room;
import com.gurudev.aircnc.domain.trip.entity.Trip;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
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
      + "join fetch r.host "
      + "join fetch r.roomPhotos "
      + "where t.id = :id and t.guest.id = :guestId")
  Optional<Trip> findByIdAndGuestId(Long id, Long guestId);

  @Query("select t "
      + "from Trip t "
      + "join fetch t.guest "
      + "where t.id = :id and t.guest.id = :guestId")
  Optional<Trip> findTripByIdAndGuestId(Long id, Long guestId);

  @Query("select count(t.id) > 0 "
      + "from Trip t "
      + "where t.room = :room "
      + "     and t.status in "
      + "      (com.gurudev.aircnc.domain.trip.entity.TripStatus.TRAVELLING, "
      + "      com.gurudev.aircnc.domain.trip.entity.TripStatus.RESERVED)")
  boolean existsByTravellingOrReserved(Room room);

  @Query("select count(t.id) > 0 from Trip t "
      + "where :checkIn <= t.checkIn "
      + "and t.checkIn <= :checkOut " // 기존 예약기간과 왼쪽에서 중복
      + "or :checkIn <= t.checkOut "
      + "and t.checkOut <= :checkOut "  // 기존 예약기간과 오른쪽에서 중복
      + "or t.checkIn <= :checkIn "
      + "and :checkOut <= t.checkOut " // 기존 예약기간의 일부 로 중복
      + "or :checkIn <= t.checkIn " //기존 예약기간을 포함하며 중복
      + "and t.checkOut <= :checkOut")
  boolean overlappedByReservedTrip(LocalDate checkIn, LocalDate checkOut);

  @Query("select t "
      + "from Trip t "
      + "where t.room.id = :roomId "
      + "and (t.status = com.gurudev.aircnc.domain.trip.entity.TripStatus.RESERVED "
      + "or t.status = com.gurudev.aircnc.domain.trip.entity.TripStatus.TRAVELLING) "
      + "and t.checkOut >= :today ")
  List<Trip> findTripsByRoomIdRelatedWithToday(Long roomId, LocalDate today);

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

}
