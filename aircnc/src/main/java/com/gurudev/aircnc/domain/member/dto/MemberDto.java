package com.gurudev.aircnc.domain.member.dto;

import com.gurudev.aircnc.domain.member.entity.Email;
import com.gurudev.aircnc.domain.member.entity.Member;
import com.gurudev.aircnc.domain.member.entity.Password;
import com.gurudev.aircnc.domain.member.entity.PhoneNumber;
import com.gurudev.aircnc.domain.member.entity.Role;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class MemberDto {
  private final String email;
  private final String password;
  private final String name;
  private final LocalDate birthDate;
  private final String phoneNumber;
  private final String role;

  public Member toEntity() {
    return new Member(new Email(email),
        new Password(password),
        name, birthDate, new PhoneNumber(phoneNumber),
        Role.valueOf(role));
  }
}
