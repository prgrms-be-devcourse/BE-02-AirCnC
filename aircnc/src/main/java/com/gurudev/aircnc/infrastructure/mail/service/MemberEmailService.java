package com.gurudev.aircnc.infrastructure.mail.service;

import com.gurudev.aircnc.infrastructure.mail.entity.EmailAuthKey;
import com.gurudev.aircnc.infrastructure.mail.entity.MailType;
import com.gurudev.aircnc.infrastructure.mail.repository.EmailAuthKeyRepository;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring5.SpringTemplateEngine;

@Service
public class MemberEmailService extends AbstractEmailService {

  private static final String TEMPLATE_NAME = "register-member";
  private static final String MESSAGE_TITLE = "AirCnC 회원가입 이메일 인증";

  private final EmailAuthKeyRepository emailAuthKeyRepository;

  protected MemberEmailService(SpringTemplateEngine springTemplateEngine,
      JavaMailSender emailSender, EmailAuthKeyRepository emailAuthKeyRepository) {
    super(springTemplateEngine, emailSender);
    this.emailAuthKeyRepository = emailAuthKeyRepository;
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
      emailAuthKeyRepository.save(new EmailAuthKey(createKey(), receiverMail));
    } catch (MailException ex) {
      throw new RuntimeException("메일 전송에 실패하였습니다", ex);
    }
  }

  public boolean validateKey(String AuthKey, String email) {
    Optional<EmailAuthKey> keyOptional = emailAuthKeyRepository.findByEmail(email);
    if (keyOptional.isEmpty()) {
      return false;
    }
    return keyOptional.get().validateKey(AuthKey);
  }
}
