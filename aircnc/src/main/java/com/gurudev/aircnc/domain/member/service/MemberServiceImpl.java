package com.gurudev.aircnc.domain.member.service;

import static com.gurudev.aircnc.exception.Preconditions.checkArgument;

import com.gurudev.aircnc.domain.member.entity.Email;
import com.gurudev.aircnc.domain.member.entity.Member;
import com.gurudev.aircnc.domain.member.entity.Password;
import com.gurudev.aircnc.domain.member.repository.MemberRepository;
import com.gurudev.aircnc.domain.member.service.command.MemberCommand.MemberRegisterCommand;
import com.gurudev.aircnc.exception.NotFoundException;
import com.gurudev.aircnc.infrastructure.security.PasswordEncryptor;
import lombok.RequiredArgsConstructor;
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
        .orElseThrow(() -> new NotFoundException(Member.class));
  }

  @Transactional
  @Override
  public Member register(MemberRegisterCommand command) {
    Member member = command.toEntity();
    checkArgument(!isDuplicated(member), "이미 등록된 이메일입니다");

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

    encodedPassword.verifyPassword(passwordEncryptor, password);

    return member;
  }

  private boolean isDuplicated(Member member) {
    return memberRepository.findByEmail(member.getEmail()).isPresent();
  }
}
