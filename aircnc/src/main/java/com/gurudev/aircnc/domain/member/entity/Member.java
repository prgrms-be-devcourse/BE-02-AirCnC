package com.gurudev.aircnc.domain.member.entity;

import static com.google.common.base.Preconditions.checkArgument;
import static org.springframework.util.StringUtils.hasText;

import com.gurudev.aircnc.domain.base.BaseIdEntity;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseIdEntity {

  /* 이메일 */
  @Embedded
  private Email email;

  /* 비밀번호 */
  @Embedded
  private Password password;

  /* 이름 */
  @Column(length = 20, nullable = false)
  private String name;

  /* 생일 */
  @Column(nullable = false)
  private LocalDate birthDate;

  /* 휴대폰 번호 */
  @Embedded
  private PhoneNumber phoneNumber;

  /* 멤버의 역할 */
  @Enumerated(value = EnumType.STRING)
  @Column(nullable = false)
  private Role role;

  public Member(Email email, Password password, String name, LocalDate birthDate,
      PhoneNumber phoneNumber, Role role) {
    checkArgument(hasText(name), "이름은 공백이 될 수 없습니다");
    checkArgument(birthDate != null, "생일은 null 이 될 수 없습니다");

    this.email = email;
    this.name = name;
    this.password = password;
    this.birthDate = birthDate;
    this.phoneNumber = phoneNumber;
    this.role = role;
  }

}
