package com.gurudev.aircnc.domain.member.service.command;

import static lombok.AccessLevel.PRIVATE;

import com.gurudev.aircnc.domain.member.entity.Email;
import com.gurudev.aircnc.domain.member.entity.Member;
import com.gurudev.aircnc.domain.member.entity.Password;
import com.gurudev.aircnc.domain.member.entity.PhoneNumber;
import com.gurudev.aircnc.domain.member.entity.Role;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class MemberCommand {

  @RequiredArgsConstructor
  @Getter
  public static class MemberRegisterCommand {

    private final String email;

    private final String password;

    private final String name;

    private final LocalDate birthDate;

    private final String phoneNumber;

    private final String role;

    public Member toEntity() {
      return new Member(
          new Email(this.email), new Password(this.password),
          this.name, this.birthDate,
          new PhoneNumber(this.phoneNumber),
          Role.valueOf(this.role));
    }

  }

}
