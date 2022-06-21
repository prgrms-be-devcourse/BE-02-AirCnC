package com.gurudev.aircnc.domain.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.gurudev.aircnc.domain.member.entity.Email;
import com.gurudev.aircnc.domain.member.entity.Member;
import com.gurudev.aircnc.domain.member.entity.Password;
import com.gurudev.aircnc.domain.member.entity.PhoneNumber;
import com.gurudev.aircnc.domain.member.entity.Role;
import com.gurudev.aircnc.exception.NoSuchMemberException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MemberServiceTest {

  @Autowired
  private MemberService memberService;

  private final Email email = new Email("test@gmail.com");
  private final Password password = new Password("123456789");
  private final String username = "testUser";
  private final LocalDate birthDate = LocalDate.now();
  private final PhoneNumber phoneNumber = new PhoneNumber("010-1234-5678");
  private final Role role = Role.GUEST;

  @Test
  void 회원_생성_로직_테스트() {
    Member member = new Member(email, password, username, birthDate, phoneNumber, role);

    memberService.register(member);
    Member foundMember = memberService.getByEmail(member.getEmail());

    assertThat(foundMember).extracting(Member::getEmail, Member::getPassword,
            Member::getName, Member::getBirthDate, Member::getPhoneNumber, Member::getRole)
        .isEqualTo(
            List.of(email, password, username, birthDate,
                phoneNumber, role));
  }

  @Test
  void 존재하지_않는_회원에_대한_조회() {
    Member member = new Member(email, password, username, birthDate, phoneNumber, role);

    assertThatThrownBy(() -> memberService.getByEmail(member.getEmail())).isInstanceOf(
        NoSuchMemberException.class);

  }
}