package com.gurudev.aircnc.domain.member.service;

import static com.gurudev.aircnc.domain.util.Fixture.createGuest;
import static com.gurudev.aircnc.util.AssertionUtil.assertThatNotFoundException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import com.gurudev.aircnc.domain.member.entity.Email;
import com.gurudev.aircnc.domain.member.entity.Member;
import com.gurudev.aircnc.domain.member.entity.Password;
import com.gurudev.aircnc.domain.member.entity.PhoneNumber;
import com.gurudev.aircnc.domain.member.entity.Role;
import com.gurudev.aircnc.domain.member.service.command.MemberCommand.MemberRegisterCommand;
import com.gurudev.aircnc.domain.util.Command;
import com.gurudev.aircnc.infrastructure.security.PasswordEncryptor;
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

  @Autowired
  private PasswordEncryptor passwordEncryptor;

  private Member member = createGuest();

  @Test
  void 회원_가입_성공_테스트() {
    MemberRegisterCommand command = Command.ofRegisterMember(member);
    member = memberService.register(command);

    //생성된 회원의 필드가 회원 가입 명령의 필드와 일치하는지 검증
    assertThat(member).extracting(Member::getEmail, Member::getName, Member::getBirthDate,
            Member::getPhoneNumber, Member::getRole)
        .isEqualTo(List.of(new Email(command.getEmail()), command.getName(), command.getBirthDate(),
            new PhoneNumber(command.getPhoneNumber()), Role.valueOf(command.getRole())));

    //생성된 회원의 비밀번호 암호와 여부 검증
    assertThat(member.getPassword().matches(passwordEncryptor, new Password(command.getPassword())))
        .isTrue();
  }

  @Test
  void 회원_조회_성공_테스트() {
    MemberRegisterCommand command = Command.ofRegisterMember(member);
    Member member = memberService.register(command);

    Member foundMember = memberService.getByEmail(member.getEmail());

    assertThat(foundMember).isEqualTo(foundMember);
  }

  @Test
  void 존재하지_않는_회원에_대한_조회_실패() {
    MemberRegisterCommand command = Command.ofRegisterMember(member);
    Email email = new Email(command.getEmail());

    assertThatNotFoundException()
        .isThrownBy(() -> memberService.getByEmail(email));
  }

  @Test
  void 로그인_성공_테스트() {
    MemberRegisterCommand command = Command.ofRegisterMember(member);
    memberService.register(command);

    String rawPassword = command.getPassword();
    Email email = new Email(command.getEmail());
    Member loginMember = memberService.login(email, new Password(rawPassword));

    assertThat(loginMember.getEmail()).isEqualTo(email);
  }

  @Test
  void 로그인_실패_테스트() {
    MemberRegisterCommand command = Command.ofRegisterMember(member);
    memberService.register(command);

    Email email = new Email(command.getEmail());
    Password invalidPassword = new Password("invalidPassword");
    assertThatExceptionOfType(BadCredentialsException.class)
        .isThrownBy(() -> memberService.login(email, invalidPassword));
  }
}