package com.gurudev.aircnc.domain.util;

import com.gurudev.aircnc.domain.member.entity.Email;
import com.gurudev.aircnc.domain.member.entity.Member;
import com.gurudev.aircnc.domain.member.entity.Password;
import com.gurudev.aircnc.domain.member.entity.PhoneNumber;
import com.gurudev.aircnc.domain.member.entity.Role;
import java.time.LocalDate;

public class Fixture {

  public static Member createHost(){

    return new Member(new Email("ndy@haha.com"),
                      new Password("paSSword!"),
                      "ndy",
                      LocalDate.of(1997, 8, 21),
                      new PhoneNumber("010-1234-5678"),
                      Role.HOST);
  }

  public static Member createGuest(){

    return new Member(new Email("ndy@haha.com"),
        new Password("paSSword!"),
        "ndy",
        LocalDate.of(1997, 8, 21),
        new PhoneNumber("010-1234-5678"),
        Role.GUEST);
  }
}
