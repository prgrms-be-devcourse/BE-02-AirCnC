package com.gurudev.aircnc.domain.member.service;

import com.gurudev.aircnc.configuration.PasswordEncryptor;
import com.gurudev.aircnc.domain.member.entity.Email;
import com.gurudev.aircnc.domain.member.entity.Member;
import com.gurudev.aircnc.domain.member.entity.Password;
import com.gurudev.aircnc.domain.member.repository.MemberRepository;
import com.gurudev.aircnc.domain.member.service.cmd.MemberCommand.MemberRegisterCommand;
import com.gurudev.aircnc.exception.AircncRuntimeException;
import com.gurudev.aircnc.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

  private final MemberRepository memberRepository;

  private final PasswordEncryptor passwordEncryptor;

  @Override
  public Member getById(Long id) {
    return memberRepository.findById(id)
        .orElseThrow(() -> new AircncRuntimeException("멤버 없음"));
  }

  @Override
  @Transactional
  public Member register(MemberRegisterCommand command) {
    Member member = command.toEntity();
    member.getPassword().encode(passwordEncryptor);
    return memberRepository.save(member);
  }

  @Override
  public Member getByEmail(Email email) {
    return memberRepository.findByEmail(email)
        .orElseThrow(() -> new NotFoundException(Member.class));
  }

  @Override
  public Member login(Email email, Password password) {
    Member member = getByEmail(email);

    Password encodedPassword = member.getPassword();

    if (!encodedPassword.matches(passwordEncryptor, password)) {
      throw new BadCredentialsException("비밀번호가 올바르지 않습니다");
    }

    return member;
  }
}
