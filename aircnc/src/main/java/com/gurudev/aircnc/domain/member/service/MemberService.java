package com.gurudev.aircnc.domain.member.service;

import com.gurudev.aircnc.domain.member.dto.MemberDto;
import com.gurudev.aircnc.domain.member.entity.Email;
import com.gurudev.aircnc.domain.member.entity.Member;
import com.gurudev.aircnc.domain.member.entity.Password;

public interface MemberService {

  Member getById(Long id);

  Member register(MemberDto member);
  
  Member getByEmail(Email email);

  Member login(Email email, Password password);
}
