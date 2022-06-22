package com.gurudev.aircnc.domain.member.service;

import com.gurudev.aircnc.domain.member.entity.Email;
import com.gurudev.aircnc.domain.member.entity.Member;
import com.gurudev.aircnc.domain.member.repository.MemberRepository;
import com.gurudev.aircnc.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

  private final MemberRepository memberRepository;

  @Override
  @Transactional
  public Member register(Member member) {
    return memberRepository.save(member);
  }

  @Override
  public Member getByEmail(Email email) {
    return memberRepository.findByEmail(email)
        .orElseThrow(() -> new NotFoundException(Member.class));
  }
}
