package com.gurudev.aircnc.infrastructure.mail;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

import javax.mail.internet.MimeMessage;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Disabled
@SpringBootTest(webEnvironment = NONE)
class EmailSendTest { // 사용방법을 위한 Temporary 테스트

  @Autowired
  private EmailService service;

  @Test
  void test() throws Exception {
    MimeMessage mimeMessage = service.createSignUpMsg("yourEmail@gmail.com");
    service.sendMessage(mimeMessage);
  }
}

