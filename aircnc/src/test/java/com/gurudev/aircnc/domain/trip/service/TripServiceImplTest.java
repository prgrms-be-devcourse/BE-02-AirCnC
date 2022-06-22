package com.gurudev.aircnc.domain.trip.service;

import static com.gurudev.aircnc.domain.trip.entity.TripStatus.RESERVED;
import static com.gurudev.aircnc.domain.util.Fixture.createGuest;
import static com.gurudev.aircnc.domain.util.Fixture.createHost;
import static com.gurudev.aircnc.domain.util.Fixture.createRoom;
import static java.time.LocalDate.now;
import static java.time.Period.between;
import static org.assertj.core.api.Assertions.assertThat;

import com.gurudev.aircnc.domain.member.entity.Member;
import com.gurudev.aircnc.domain.member.service.MemberService;
import com.gurudev.aircnc.domain.room.entity.Room;
import com.gurudev.aircnc.domain.room.service.RoomService;
import com.gurudev.aircnc.domain.trip.entity.Trip;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class TripServiceImplTest {

  private final LocalDate checkIn = now().plusDays(1);
  private final LocalDate checkOut = now().plusDays(2);
  @Autowired
  private TripService tripService;
  @Autowired
  private MemberService memberService;
  @Autowired
  private RoomService roomService;
  private Member guest;
  private Room room;
  private int totalPrice;
  private int headCount;

  @BeforeEach
  void setUp() {
    Member host = createHost();
    memberService.register(host);
    room = createRoom(host);
    roomService.register(room, Collections.emptyList());

    guest = createGuest();
    memberService.register(guest);

    headCount = room.getCapacity();
    totalPrice = room.getPricePerDay() * between(checkIn, checkOut).getDays();
  }

  @Test
  void 여행_생성_성공() {
    Trip trip = tripService.reserve(guest, room.getId(), checkIn, checkOut, headCount,
        totalPrice);

    assertThat(trip.getId()).isNotNull();
    assertThat(trip).extracting(Trip::getGuest, Trip::getRoom, Trip::getCheckIn, Trip::getCheckOut,
            Trip::getTotalPrice, Trip::getHeadCount, Trip::getStatus)
        .isEqualTo(List.of(guest, room, checkIn, checkOut, totalPrice, headCount, RESERVED));
  }
}