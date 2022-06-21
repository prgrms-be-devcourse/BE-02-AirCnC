package com.gurudev.aircnc.domain.member.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.gurudev.aircnc.domain.member.entity.Email;
import com.gurudev.aircnc.domain.member.entity.Member;
import com.gurudev.aircnc.domain.member.entity.Password;
import com.gurudev.aircnc.domain.member.entity.PhoneNumber;
import com.gurudev.aircnc.domain.member.entity.Role;
import com.gurudev.aircnc.domain.util.Fixture;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
class MemberServiceTest {

  @Autowired
  private MemberService memberService;

  private final Email email = new Email("test@gmail.com");
  private final Password password = new Password("1234");
  private final String username = "testUser";
  private final LocalDate birthDate = LocalDate.now();
  private final PhoneNumber phoneNumber = new PhoneNumber("010-1234-5678");
  private final Role role = Role.GUEST;

  @Test
  void 회원_생성_로직_테스트() {
    Member member = new Member(email, password, username, birthDate, phoneNumber, role);

    memberService.registerMember(member);
    Optional<Member> memberOptional = memberService.getMemberByEmail(member.getEmail());

    assertThat(memberOptional).isPresent();
    assertThat(memberOptional.get()).extracting(Member::getEmail, Member::getPassword,
            Member::getName, Member::getBirthDate, Member::getPhoneNumber, Member::getRole)
        .isEqualTo(
            List.of(email, password, username, birthDate,
                phoneNumber, role));
  }
}