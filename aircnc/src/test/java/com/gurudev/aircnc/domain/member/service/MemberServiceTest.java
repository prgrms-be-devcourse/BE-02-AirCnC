package com.gurudev.aircnc.domain.member.service;

import static com.gurudev.aircnc.domain.util.Fixture.createGuestDto;
import static com.gurudev.aircnc.util.AssertionUtil.assertThatNotFoundException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.gurudev.aircnc.domain.member.dto.MemberDto;
import com.gurudev.aircnc.domain.member.entity.Email;
import com.gurudev.aircnc.domain.member.entity.Member;
import com.gurudev.aircnc.domain.member.entity.Password;
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

  private final MemberDto memberDto = createGuestDto();

  @Test
  void 회원_생성_조회_성공_테스트() {
    Member registeredMember = memberService.register(memberDto);

    Member foundMember = memberService.getById(registeredMember.getId());

    assertThat(foundMember).extracting(
        Member::getEmail,
        Member::getPassword,
        Member::getName,
        Member::getBirthDate,
        Member::getPhoneNumber,
        Member::getRole
    ).isEqualTo(
        List.of(registeredMember.getEmail(), registeredMember.getPassword(),
            registeredMember.getName(), registeredMember.getBirthDate(),
            registeredMember.getPhoneNumber(), registeredMember.getRole()));
  }

  @Test
  void 존재하지_않는_회원에_대한_조회_실패() {
    Email email = new Email(memberDto.getEmail());

    assertThatNotFoundException()
        .isThrownBy(() -> memberService.getByEmail(email));
  }

  @Test
  void 로그인_성공_테스트() {
    String rawPassword = memberDto.getPassword();
    memberService.register(memberDto);

    Member loginMember = memberService.login(new Email(memberDto.getEmail()), new Password(rawPassword));

    assertThat(Email.toString(loginMember.getEmail())).isEqualTo(memberDto.getEmail());
  }

  @Test
  void 로그인_실패_테스트() {
    memberService.register(memberDto);

    Email email = new Email(memberDto.getEmail());
    Password illegalPassword = new Password("wrongpassword");
    assertThatThrownBy(() -> memberService.login(email, illegalPassword)).isInstanceOf(
        BadCredentialsException.class);
  }
}
