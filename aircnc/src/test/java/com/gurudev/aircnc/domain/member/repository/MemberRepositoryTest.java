package com.gurudev.aircnc.domain.member.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.gurudev.aircnc.domain.member.entity.Email;
import com.gurudev.aircnc.domain.member.entity.Member;
import com.gurudev.aircnc.domain.member.entity.Password;
import com.gurudev.aircnc.domain.member.entity.PhoneNumber;
import com.gurudev.aircnc.domain.member.entity.Role;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MemberRepositoryTest {

  @Autowired
  private MemberRepository memberRepository;

  @PersistenceContext
  private EntityManager em;

  @Test
  void Member_상태_저장() {
    Email email = new Email("ndy@haha.com");
    Password password = new Password("paSSword!");
    String name = "ndy";
    LocalDate birthDate = LocalDate.of(1997, 8, 21);
    PhoneNumber phoneNumber = new PhoneNumber("010-1234-5678");

    Member member = new Member(email, password, name, birthDate, phoneNumber, Role.GUEST);

    memberRepository.save(member);

    em.clear();

    Member findMember = memberRepository.findById(member.getId())
        .orElseThrow(IllegalArgumentException::new);

    assertThat(findMember).extracting(Member::getEmail, Member::getPassword, Member::getName,
            Member::getBirthDate, Member::getPhoneNumber)
        .isEqualTo(List.of(email, password, name, birthDate, phoneNumber));
  }

}