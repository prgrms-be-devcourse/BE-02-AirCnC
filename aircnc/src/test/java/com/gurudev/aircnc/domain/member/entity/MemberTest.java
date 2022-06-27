package com.gurudev.aircnc.domain.member.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class MemberTest {

  private Email email;
  private Password password;
  private String name;
  private LocalDate birthDate;
  private PhoneNumber phoneNumber;

  @BeforeEach
  void setUp() {
    //given
    email = new Email("ndy@haha.com");
    password = new Password("paSSword!");
    name = "ndy";
    birthDate = LocalDate.of(1997, 8, 21);
    phoneNumber = new PhoneNumber("010-1234-5678");
  }

  @Test
  void 회원_생성() {
    //when
    Member member = new Member(email, password, name, birthDate, phoneNumber, Role.GUEST);

    //tehn
    assertThat(member).extracting(Member::getEmail, Member::getPassword, Member::getName,
            Member::getBirthDate, Member::getPhoneNumber)
        .isEqualTo(List.of(email, password, name, birthDate, phoneNumber));
  }

  @ParameterizedTest
  @NullAndEmptySource
  void 이름_공백_불가(String name) {
    //then
    assertThatIllegalArgumentException()
        .isThrownBy(() -> new Member(email, password, name, birthDate, phoneNumber, Role.GUEST));
  }

  @Test
  void 생일_null_불가() {
    //then
    assertThatIllegalArgumentException()
        .isThrownBy(() -> new Member(email, password, name, null, phoneNumber, Role.GUEST));
  }
}