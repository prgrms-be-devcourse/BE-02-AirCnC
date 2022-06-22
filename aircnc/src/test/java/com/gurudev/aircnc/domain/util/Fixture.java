package com.gurudev.aircnc.domain.util;

import com.gurudev.aircnc.domain.member.entity.Email;
import com.gurudev.aircnc.domain.member.entity.Member;
import com.gurudev.aircnc.domain.member.entity.Password;
import com.gurudev.aircnc.domain.member.entity.PhoneNumber;
import com.gurudev.aircnc.domain.member.entity.Role;
import com.gurudev.aircnc.domain.room.entity.Address;
import com.gurudev.aircnc.domain.room.entity.Room;
import com.gurudev.aircnc.domain.room.entity.RoomPhoto;
import java.time.LocalDate;

public class Fixture {

  public static Member createHost() {
    return new Member(new Email("ndy@haha.com"),
        new Password("paSSword!"),
        "ndy",
        LocalDate.of(1997, 8, 21),
        new PhoneNumber("010-1234-5678"),
        Role.HOST);
  }

  public static Member createGuest() {
    return new Member(new Email("ndy@haha.com"),
        new Password("paSSword!"),
        "ndy",
        LocalDate.of(1997, 8, 21),
        new PhoneNumber("010-1234-5678"),
        Role.GUEST);
  }


  public static Room createRoom() {
    return new Room("전주 한옥마을",
        new Address("전라북도 전주시 완산구 풍산동 3가"),
        "아주 멋진 한옥마을입니다.",
        100000,
        4,
        createHost());
  }

  public static RoomPhoto createRoomPhoto() {
    return new RoomPhoto("photo.jpg",
        createRoom());
  }
}
