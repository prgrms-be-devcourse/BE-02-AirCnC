package com.gurudev.aircnc.domain.member.service;

import com.gurudev.aircnc.domain.member.entity.Email;
import com.gurudev.aircnc.domain.member.entity.Member;
import com.gurudev.aircnc.domain.member.repository.MemberRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

  private final MemberRepository memberRepository;

  @Override
  public Member registerMember(Member member) {
    return memberRepository.save(member);
  }

  @Override
  public Optional<Member> getMemberByEmail(Email email) {
    return memberRepository.findByEmail(email);
  }
}
