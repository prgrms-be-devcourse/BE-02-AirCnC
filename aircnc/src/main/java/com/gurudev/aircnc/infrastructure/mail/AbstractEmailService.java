package com.gurudev.aircnc.infrastructure.mail;

import java.io.UnsupportedEncodingException;
import javax.mail.internet.InternetAddress;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.spring5.SpringTemplateEngine;

public abstract class AbstractEmailService implements EmailService {

  protected final InternetAddress sender;

  protected final SpringTemplateEngine springTemplateEngine;

  protected final JavaMailSender emailSender;

  protected final String CHARSET = "utf-8";

  protected final String CONTENT_TYPE = "html";

  protected AbstractEmailService(SpringTemplateEngine springTemplateEngine,
      JavaMailSender emailSender) {
    try {
      this.sender = new InternetAddress("aircnc@gmail.com", "AirCnC");
      this.emailSender = emailSender;
      this.springTemplateEngine = springTemplateEngine;
    } catch (UnsupportedEncodingException ex) {
      throw new RuntimeException("이메일 송신자 설정에 문제가 발생했습니다", ex);
    }
  }
}
