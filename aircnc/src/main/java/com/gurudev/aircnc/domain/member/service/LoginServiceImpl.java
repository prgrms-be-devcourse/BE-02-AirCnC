package com.gurudev.aircnc.domain.member.service;

import static java.lang.String.format;

import com.gurudev.aircnc.domain.member.entity.Email;
import com.gurudev.aircnc.domain.member.entity.Member;
import com.gurudev.aircnc.domain.member.entity.Password;
import com.gurudev.aircnc.domain.member.repository.MemberRepository;
import com.gurudev.aircnc.exception.NoSuchMemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService{

    private final MemberRepository memberRepository;

    @Override
    public Member login(Email email, Password password) {

        Member member = memberRepository.findByEmail(email)
            .orElseThrow(
                () -> new NoSuchMemberException(format("해당하는 이메일(%s) 가진 멤버가 존재하지 않습니다", email)));

        member.verifyPassword(password);
        return null;
    }
}
