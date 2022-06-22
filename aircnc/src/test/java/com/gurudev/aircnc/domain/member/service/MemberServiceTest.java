package com.gurudev.aircnc.domain.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.gurudev.aircnc.domain.member.entity.Email;
import com.gurudev.aircnc.domain.member.entity.Member;
import com.gurudev.aircnc.domain.member.entity.Password;
import com.gurudev.aircnc.domain.member.entity.PhoneNumber;
import com.gurudev.aircnc.domain.member.entity.Role;
import com.gurudev.aircnc.domain.util.Fixture;
import com.gurudev.aircnc.exception.NoSuchMemberException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MemberServiceTest {

  @Autowired
  private MemberService memberService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  private final Member member = Fixture.createGuest();

  @Test
  void 회원_생성_조회_성공_테스트() {
    memberService.register(member);

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
            member.getPhoneNumber(), member.getRole()));
  }

  @Test
  void 존재하지_않는_회원에_대한_조회_실패() {
    Email email = member.getEmail();
    assertThatThrownBy(() -> memberService.getByEmail(email)).isInstanceOf(
        NoSuchMemberException.class);

  }

  @Test
  void 로그인_성공_테스트() {
    Password beforeEncodingPassword = member.getPassword();
    memberService.register(member.encodePassword(passwordEncoder::encode));

    Member loginUser = memberService.login(member.getEmail(), beforeEncodingPassword);

    assertThat(loginUser).extracting(Member::getEmail, Member::getPassword).isEqualTo(
        List.of(member.getEmail(), member.getPassword()));
  }

  @Test
  void 로그인_싪패_테스트() {
    memberService.register(member);

    Email email = member.getEmail();
    Password wrongPassword = new Password("wrongpassword");
    assertThatThrownBy(() -> memberService.login(email, wrongPassword)).isInstanceOf(
        BadCredentialsException.class);
  }
}