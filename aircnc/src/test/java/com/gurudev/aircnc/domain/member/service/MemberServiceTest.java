package com.gurudev.aircnc.domain.member.service;

import static com.gurudev.aircnc.domain.util.Fixture.createGuest;
import static com.gurudev.aircnc.util.AssertionUtil.assertThatNotFoundException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

import com.gurudev.aircnc.domain.member.entity.Email;
import com.gurudev.aircnc.domain.member.entity.Member;
import com.gurudev.aircnc.domain.member.entity.Password;
import com.gurudev.aircnc.domain.member.service.command.MemberCommand.MemberRegisterCommand;
import com.gurudev.aircnc.domain.util.Command;
import com.gurudev.aircnc.infrastructure.security.PasswordEncryptor;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest(webEnvironment = NONE)
class MemberServiceTest {

  @Autowired
  private MemberService memberService;

  @Autowired
  private PasswordEncryptor passwordEncryptor;


  @Test
  void 회원_가입_성공_테스트() {
    //given
    Member member = createGuest();
    MemberRegisterCommand command = Command.ofRegisterMember(member);
    Password rawPassword = command.getPassword();

    //when
    member = memberService.register(command);

    //then
    //생성된 회원의 필드가 회원 가입 명령의 필드와 일치하는지 검증
    assertThat(member).extracting(Member::getEmail, Member::getName, Member::getBirthDate,
            Member::getPhoneNumber, Member::getRole)
        .isEqualTo(List.of(command.getEmail(), command.getName(), command.getBirthDate(),
            command.getPhoneNumber(), command.getRole()));

    //생성된 회원의 비밀번호 암호와 여부 검증
    Password password = member.getPassword();
    assertThatNoException()
        .isThrownBy(() -> password.verifyPassword(passwordEncryptor, rawPassword));
  }

  @Test
  void 회원_조회_성공_테스트() {
    //given
    Member member = createGuest();
    MemberRegisterCommand command = Command.ofRegisterMember(member);
    member = memberService.register(command);

    //when
    Member foundMember = memberService.getByEmail(member.getEmail());

    //then
    assertThat(foundMember).isEqualTo(member);
  }

  @Test
  void 존재하지_않는_회원에_대한_조회_실패() {
    //given
    Member member = createGuest();
    MemberRegisterCommand command = Command.ofRegisterMember(member);
    Email email = command.getEmail();

    //then
    assertThatNotFoundException()
        .isThrownBy(() -> memberService.getByEmail(email));
  }

  @Test
  void 로그인_성공_테스트() {
    //given
    Member member = createGuest();
    MemberRegisterCommand command = Command.ofRegisterMember(member);
    Password rawPassword = command.getPassword();
    Email email = command.getEmail();
    member = memberService.register(command);

    //when
    Member loginMember = memberService.login(email, rawPassword);

    //then
    assertThat(loginMember).isEqualTo(member);
  }

  @Test
  void 로그인_실패_테스트() {
    //given
    Member member = createGuest();
    MemberRegisterCommand command = Command.ofRegisterMember(member);
    memberService.register(command);

    //then
    Email email = command.getEmail();
    Password invalidPassword = new Password("invalidPassword");
    assertThatIllegalArgumentException()
        .isThrownBy(() -> memberService.login(email, invalidPassword));
  }
}