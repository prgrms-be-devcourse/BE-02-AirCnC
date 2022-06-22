package com.gurudev.aircnc.domain.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.gurudev.aircnc.domain.member.entity.Member;
import com.gurudev.aircnc.domain.util.Fixture;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class LoginServiceTest {

    @Autowired
    private LoginService loginService;

    @Autowired
    private MemberService memberService;

    @Test
    void 로그인_성공() {
        Member member = Fixture.createGuest();
        memberService.register(member);

        Member loginMember = loginService.login(member.getEmail(), member.getPassword());

        assertThat(loginMember).extracting(Member::getEmail, Member::getPassword)
            .isEqualTo(List.of(member.getEmail(), member.getPassword()));
    }
}