package com.gurudev.aircnc.intrastructure.service;

import com.gurudev.aircnc.domain.member.entity.PasswordEncryptor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SpringPasswordEncryptor implements PasswordEncryptor {

    private final PasswordEncoder passwordEncoder;

    @Override
    public String encrypt(final String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
}
