package com.gurudev.aircnc.domain.trip.service;


import static com.gurudev.aircnc.domain.util.Fixture.createGuest;
import static com.gurudev.aircnc.domain.util.Fixture.createHost;
import static com.gurudev.aircnc.domain.util.Fixture.createRoom;
import static com.gurudev.aircnc.domain.util.Fixture.createRoomPhoto;
import static java.time.LocalDate.now;
import static java.time.Period.between;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

import com.gurudev.aircnc.domain.member.entity.Member;
import com.gurudev.aircnc.domain.member.service.MemberService;
import com.gurudev.aircnc.domain.room.entity.Room;
import com.gurudev.aircnc.domain.room.entity.RoomPhoto;
import com.gurudev.aircnc.domain.room.service.RoomService;
import com.gurudev.aircnc.domain.trip.entity.Trip;
import com.gurudev.aircnc.domain.util.Command;
import com.gurudev.aircnc.infrastructure.event.TripEvent;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Disabled
@Transactional
@SpringBootTest(webEnvironment = NONE)
public abstract class BaseTripServiceTest {

  @Autowired
  protected TripService tripService;

  @Autowired
  protected MemberService memberService;

  @Autowired
  protected RoomService roomService;

  protected Room room;
  protected RoomPhoto roomPhoto;

  protected Member host;
  protected Member guest;

  protected LocalDate checkIn;
  protected LocalDate checkOut;

  protected int headCount;
  protected int totalPrice;

  @BeforeEach
  void setUp() {
    //회원 세팅
    host = memberService.register(Command.ofRegisterMember(createHost()));
    guest = createGuest();
    guest = memberService.register(Command.ofRegisterMember(guest));

    //숙소 세팅
    room = createRoom();
    roomPhoto = createRoomPhoto();
    room = roomService.register(Command.ofRegisterRoom(room, List.of(roomPhoto), host.getId()));

    //여행의 필드 세팅
    checkIn = now().plusDays(1);
    checkOut = now().plusDays(2);
    headCount = room.getCapacity();
    totalPrice = between(checkIn, checkOut).getDays() * room.getPricePerDay();
  }

  protected TripEvent defaultTripEvent() {
    return Command.ofReserveTrip(new Trip(guest, room, checkIn, checkOut, totalPrice, headCount));
  }
}
