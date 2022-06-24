package com.gurudev.aircnc.domain.member.entity;

public interface PasswordMatcher {

    boolean match(String rawPassword, String encrtypedPassword);
}
