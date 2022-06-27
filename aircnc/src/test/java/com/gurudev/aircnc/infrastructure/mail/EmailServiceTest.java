package com.gurudev.aircnc.infrastructure.mail;

import javax.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EmailSendTest { // 사용방법을 위한 Temporary 테스트

  @Autowired
  private EmailService service;

  @Test
  void test() throws Exception {
    MimeMessage mimeMessage = service.createSignUpMsg("rlfrmsdh1@gmail.com");
    service.sendMessage(mimeMessage);
  }
}