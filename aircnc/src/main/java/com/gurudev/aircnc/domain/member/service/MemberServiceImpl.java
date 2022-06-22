package com.gurudev.aircnc.domain.member.service;

import static java.lang.String.format;

import com.gurudev.aircnc.domain.member.entity.Email;
import com.gurudev.aircnc.domain.member.entity.Member;
import com.gurudev.aircnc.domain.member.entity.Password;
import com.gurudev.aircnc.domain.member.repository.MemberRepository;
import com.gurudev.aircnc.exception.NoSuchMemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

  private final MemberRepository memberRepository;

  private final PasswordEncoder passwordEncoder;

  @Override
  @Transactional
  public Member register(Member member) {
    return memberRepository.save(member.encodePassword(passwordEncoder::encode));
  }

  @Override
  public Member getByEmail(Email email) {
    return memberRepository.findByEmail(email)
        .orElseThrow(() -> new NoSuchMemberException(format("해당하는 이메일(%s) 가진 멤버가 존재하지 않습니다", email)));
  }

  @Override
  public Member login(Email email, Password password) {
    Member member = getByEmail(email);

    member.verifyPassword(encodedPassword -> passwordEncoder.matches(Password.toString(password), Password.toString(encodedPassword)));

    return member;
  }
}
