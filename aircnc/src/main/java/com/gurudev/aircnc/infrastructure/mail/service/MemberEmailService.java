package com.gurudev.aircnc.infrastructure.mail.service;

import com.gurudev.aircnc.infrastructure.mail.entity.AuthenticationKey;
import com.gurudev.aircnc.infrastructure.mail.entity.MailType;
import com.gurudev.aircnc.infrastructure.mail.repository.AuthKeyRepository;
import java.util.Map;
import java.util.Random;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring5.SpringTemplateEngine;

@Service
public class MemberEmailService extends AbstractEmailService {

  private static final String TEMPLATE_NAME = "register-member";
  private static final String MESSAGE_TITLE = "AirCnC 회원가입 이메일 인증";

  private final AuthKeyRepository authKeyRepository;

  protected MemberEmailService(SpringTemplateEngine springTemplateEngine,
      JavaMailSender emailSender, AuthKeyRepository authKeyRepository) {
    super(springTemplateEngine, emailSender);
    this.authKeyRepository = authKeyRepository;
  }

  private static String createKey() {
    StringBuilder key = new StringBuilder();
    Random rnd = new Random();

    for (int i = 0; i < 8; i++) { // 인증코드 8자리
      int index = rnd.nextInt(3); // 0~2 까지 랜덤

      switch (index) {
        case 0:
          key.append((char) (rnd.nextInt(26) + 97));
          //  a~z  (ex. 1+97=98 => (char)98 = 'b')
          break;
        case 1:
          key.append((char) (rnd.nextInt(26) + 65));
          //  A~Z
          break;
        case 2:
          key.append((rnd.nextInt(10)));
          // 0~9
          break;
      }
    }
    return key.toString();
  }

  @Override
  public void send(String receiverMail, Map<String, Object> contentMap, MailType mailKind) {
    try {
      emailSender.send(createMessage(receiverMail, Map.of("code", createKey()),
          MESSAGE_TITLE, TEMPLATE_NAME));
      authKeyRepository.save(new AuthenticationKey(createKey(), receiverMail));
    } catch (MailException ex) {
      throw new RuntimeException("메일 전송에 실패하였습니다", ex);
    }
  }

  public boolean validateKey(String AuthKey, String email) {
    AuthenticationKey key = authKeyRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("해당 인증키가 존재하지 않습니다"));
    return key.validateKey(AuthKey);
  }
}
