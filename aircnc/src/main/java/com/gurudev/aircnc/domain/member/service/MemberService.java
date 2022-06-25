package com.gurudev.aircnc.domain.member.service;

import com.gurudev.aircnc.domain.member.entity.Email;
import com.gurudev.aircnc.domain.member.entity.Member;
import com.gurudev.aircnc.domain.member.entity.Password;

public interface MemberService {

  Member getById(Long id);

  Member register(Member member);

  Member getByEmail(Email email);

  Member login(Email email, Password password);
}
