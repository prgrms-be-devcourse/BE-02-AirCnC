package com.gurudev.aircnc.domain.member.service;

import static com.gurudev.aircnc.domain.util.Fixture.createHostRegisterCmd;
import static com.gurudev.aircnc.util.AssertionUtil.assertThatNotFoundException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.gurudev.aircnc.configuration.PasswordEncryptor;
import com.gurudev.aircnc.domain.member.entity.Email;
import com.gurudev.aircnc.domain.member.entity.Member;
import com.gurudev.aircnc.domain.member.entity.Password;
import com.gurudev.aircnc.domain.member.entity.PhoneNumber;
import com.gurudev.aircnc.domain.member.entity.Role;
import com.gurudev.aircnc.domain.member.service.cmd.MemberCommand.MemberRegisterCommand;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MemberServiceTest {

  @Autowired
  private MemberService memberService;

  private final MemberRegisterCommand memberRegisterCmd = createHostRegisterCmd();

  private final PasswordEncryptor passwordEncryptor = new PasswordEncryptor();

  @Test
  void 회원_생성_성공_테스트() {
    Member member = memberService.register(memberRegisterCmd);

    assertThat(member).extracting(
        Member::getEmail,
        Member::getName,
        Member::getBirthDate,
        Member::getPhoneNumber,
        Member::getRole
    ).isEqualTo(
        List.of(new Email(memberRegisterCmd.getEmail()),
            memberRegisterCmd.getName(),
            memberRegisterCmd.getBirthDate(),
            new PhoneNumber(memberRegisterCmd.getPhoneNumber()),
            Role.valueOf(memberRegisterCmd.getRole())));
    assertThat(member.getPassword().matches(
        passwordEncryptor,
        new Password(memberRegisterCmd.getPassword()))).isTrue();
  }

  @Test
  void 회원_조회_성공_테스트() {
    Member member = memberService.register(memberRegisterCmd);

    Member foundMember = memberService.getByEmail(member.getEmail());

    assertThat(foundMember).extracting(
        Member::getEmail,
        Member::getPassword,
        Member::getName,
        Member::getBirthDate,
        Member::getPhoneNumber,
        Member::getRole
    ).isEqualTo(
        List.of(member.getEmail(), member.getPassword(),
            member.getName(), member.getBirthDate(),
            member.getPhoneNumber(),
            member.getRole()));
  }

  @Test
  void 존재하지_않는_회원에_대한_조회_실패() {
    Email email = new Email(memberRegisterCmd.getEmail());

    assertThatNotFoundException()
        .isThrownBy(() -> memberService.getByEmail(email));
  }

  @Test
  void 로그인_성공_테스트() {
    String rawPassword = memberRegisterCmd.getPassword();
    Email email = new Email(memberRegisterCmd.getEmail());
    memberService.register(memberRegisterCmd);

    Member loginMember = memberService.login(email, new Password(rawPassword));

    assertThat(loginMember.getEmail()).isEqualTo(email);
  }

  @Test
  void 로그인_실패_테스트() {
    memberService.register(memberRegisterCmd);
    Email email = new Email(memberRegisterCmd.getEmail());
    Password illegalPassword = new Password("wrongpassword");

    assertThatThrownBy(() -> memberService.login(email, illegalPassword)).isInstanceOf(
        BadCredentialsException.class);
  }
}