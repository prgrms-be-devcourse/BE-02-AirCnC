package com.gurudev.aircnc.domain.member.entity;

import static javax.persistence.EnumType.STRING;

import com.gurudev.aircnc.domain.common.BaseIdEntity;
import java.time.LocalDate;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 회원
 * <li> 이메일은 중복될 수 없다. </li>
 * <li> 이메일은 로그인시 아이디로 활용된다. </li>
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseIdEntity {

  @Embedded
  private Email email;

  @Embedded
  private Password password;

  private String name;

  private LocalDate birthDate;

  @Embedded
  private PhoneNumber phoneNumber;

  @Enumerated(value = STRING)
  private Role role;

  /**
   * 회원 가입시 사용되는 생성자 <br> 회원 가입을 위해 이메일, 비밀번호, 이름, 생년월일, 전화번호, 역할을 제공해야 한다.
   */
  public Member(Email email, Password password, String name, LocalDate birthDate,
      PhoneNumber phoneNumber, Role role) {

    this.email = email;
    this.name = name;
    this.password = password;
    this.birthDate = birthDate;
    this.phoneNumber = phoneNumber;
    this.role = role;
  }

  public boolean isHost() {
    return role == Role.HOST;
  }
}
