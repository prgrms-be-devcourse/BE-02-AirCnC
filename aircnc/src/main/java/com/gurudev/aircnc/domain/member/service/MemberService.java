package com.gurudev.aircnc.domain.member.service;

import com.gurudev.aircnc.domain.member.entity.Email;
import com.gurudev.aircnc.domain.member.entity.Member;

public interface MemberService {

  Member register(Member member);
  
  Member getByEmail(Email email);

}
