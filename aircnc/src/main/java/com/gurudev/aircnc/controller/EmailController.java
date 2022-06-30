package com.gurudev.aircnc.controller;

import static com.gurudev.aircnc.controller.ApiResponse.noContent;

import com.gurudev.aircnc.exception.AircncRuntimeException;
import com.gurudev.aircnc.infrastructure.mail.entity.MailType;
import com.gurudev.aircnc.infrastructure.mail.service.EmailService;
import java.util.Collections;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/emails")
@RequiredArgsConstructor
public class EmailController {

  private final EmailService memberEmailService;

  @PostMapping
  public ResponseEntity<?> emailSend(@RequestBody Map<String, String> email) {
    memberEmailService.send(email.get("email"), Collections.emptyMap(), MailType.REGISTER);
    return noContent();
  }

  @PostMapping("/verify")
  public ResponseEntity<?> verifyCode(@RequestBody Map<String, String> codeAndEmail) {
    if (memberEmailService.validateKey(codeAndEmail.get("code"), codeAndEmail.get("email"))) {
      return noContent();
    }
    throw new AircncRuntimeException("인증키가 올바르지 않습니다");
  }

}
