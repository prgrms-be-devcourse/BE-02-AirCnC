package com.gurudev.aircnc.domain.member.service.command;

import static java.time.LocalDate.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import com.gurudev.aircnc.domain.member.entity.Email;
import com.gurudev.aircnc.domain.member.entity.Password;
import com.gurudev.aircnc.domain.member.entity.PhoneNumber;
import com.gurudev.aircnc.domain.member.entity.Role;
import com.gurudev.aircnc.domain.member.service.command.MemberCommand.MemberRegisterCommand;
import java.time.LocalDate;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class MemberCommandTest {

  @Nested
  class 회원_가입_명령 {

    @Test
    void 회원_가입_명령_생성_성공() {
      //when
      MemberRegisterCommand command
          = new MemberRegisterCommand("ndy@haha.com", "paSSword!", "ndy", of(1997, 8, 21),
          "010-1234-5678", "GUEST");

      //then
      assertThat(command).isNotNull()
          .extracting(MemberRegisterCommand::getEmail,
              MemberRegisterCommand::getPassword,
              MemberRegisterCommand::getName,
              MemberRegisterCommand::getBirthDate,
              MemberRegisterCommand::getPhoneNumber,
              MemberRegisterCommand::getRole)
          .containsExactly(
              new Email("ndy@haha.com"),
              new Password("paSSword!"),
              "ndy",
              of(1997, 8, 21),
              new PhoneNumber("010-1234-5678"),
              Role.GUEST
          );
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 이름_공백_불가(String name) {
      //then
      assertThatIllegalArgumentException()
          .isThrownBy(
              () -> new MemberRegisterCommand("ndy@haha.com", "paSSword!", name, of(1997, 8, 21),
                  "010-1234-5678", "GUEST"));
    }

    @Test
    void 생일_null_불가() {
      //given
      LocalDate birthDate = null;

      //then
      assertThatIllegalArgumentException()
          .isThrownBy(() -> new MemberRegisterCommand("ndy@haha.com", "paSSword!", "ndy", birthDate,
              "010-1234-5678", "GUEST"));
    }
  }
}