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

  @Embedded
  private Email email;

  @Embedded
  private Password password;

  @Column(length = 20, nullable = false)
  private String name;

  @Column(nullable = false)
  private LocalDate birthDate;

  @Embedded
  private PhoneNumber phoneNumber;

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
