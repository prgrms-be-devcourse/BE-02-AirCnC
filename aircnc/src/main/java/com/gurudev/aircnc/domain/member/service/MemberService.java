package com.gurudev.aircnc.domain.member.service;

import com.gurudev.aircnc.domain.member.entity.Email;
import com.gurudev.aircnc.domain.member.entity.Member;
import com.gurudev.aircnc.domain.member.entity.Password;
import com.gurudev.aircnc.domain.member.service.command.MemberCommand.MemberRegisterCommand;

public interface MemberService {

  Member getById(Long id);

  Member register(MemberRegisterCommand command);

  Member getByEmail(Email email);

  Member login(Email email, Password password);
}
