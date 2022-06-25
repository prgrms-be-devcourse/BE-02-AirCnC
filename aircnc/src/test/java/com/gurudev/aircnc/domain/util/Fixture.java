package com.gurudev.aircnc.domain.util;

import com.gurudev.aircnc.domain.member.entity.Email;
import com.gurudev.aircnc.domain.member.entity.Member;
import com.gurudev.aircnc.domain.member.entity.Password;
import com.gurudev.aircnc.domain.member.entity.PhoneNumber;
import com.gurudev.aircnc.domain.member.entity.Role;
import com.gurudev.aircnc.domain.room.entity.Address;
import com.gurudev.aircnc.domain.room.entity.Room;
import com.gurudev.aircnc.domain.room.entity.RoomPhoto;
import com.gurudev.aircnc.domain.trip.entity.Trip;
import java.time.LocalDate;

public class Fixture {

  public static Member createHost() {
    return new Member(new Email("host@haha.com"),
                      new Password("paSSword!"),
                      "ndy",
                      LocalDate.of(1997, 8, 21),
                      new PhoneNumber("010-1234-5678"),
                      Role.HOST);
  }

  public static Member createGuest() {
    return new Member(new Email("guest@haha.com"),
                      new Password("paSSword!"),
                      "ndy",
                      LocalDate.of(1997, 8, 21),
                      new PhoneNumber("010-1234-5678"),
                      Role.GUEST);
  }

  public static Address createAddress() {
    return new Address("전라북도 전주시 완산구 풍산동 123-1",
                       "전라북도 전주시 완산구 풍산1로 1길",
                       "123호",
                       "12345");
  }

  public static Room createRoom() {
    return new Room("전주 한옥마을",
                    createAddress(),
                    "아주 멋진 한옥마을입니다.",
                    100000,
                    4);
  }

  public static RoomPhoto createRoomPhoto() {
    return new RoomPhoto("photo.jpg");
  }

  public static Trip createTrip() {
    return new Trip(createGuest(), createRoom(), LocalDate.now(),
                    LocalDate.now().plusDays(1), 100000, 4);
  }
}
