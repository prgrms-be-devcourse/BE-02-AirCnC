package com.gurudev.aircnc.domain.member.service;

import com.gurudev.aircnc.domain.member.entity.Email;
import com.gurudev.aircnc.domain.member.entity.Member;
import java.util.Optional;

public interface MemberService {

  Member registerMember(Member member);

  Optional<Member> getMemberByEmail(Email email);

}
