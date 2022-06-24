package com.gurudev.aircnc.domain.member.entity;

public interface PasswordEncryptor {
    String encrypt(String rawPassword);
}
