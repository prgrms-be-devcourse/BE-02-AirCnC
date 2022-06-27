package com.gurudev.aircnc.infrastructure.mail;

import java.util.Random;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;


@Component
public class EmailServiceImpl implements EmailService {

  @Autowired
  JavaMailSender emailSender;

  @Autowired
  private SpringTemplateEngine springTemplateEngine;

  public static final String authenticationKey = createKey();

  @Override
  public MimeMessage createSignUpMsg(String to) throws Exception {
    MimeMessage message = emailSender.createMimeMessage();

    message.addRecipients(RecipientType.TO, to); //보내는 대상
    message.setSubject("AirCnC 회원가입 이메일 인증"); //제목

    Context context = new Context();
    context.setVariable("code", authenticationKey);
    String msg = springTemplateEngine.process("register-member", context);

    message.setText(msg, "utf-8", "html"); //내용
    message.setFrom(new InternetAddress("aircnc@gmail.com", "AirCnC")); //보내는 사람

    return message;
  }

  public static String createKey() {
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
  public void sendMessage(MimeMessage message) {
    try { //예외처리
      emailSender.send(message);
    } catch (MailException es) {
      es.printStackTrace();
      throw new IllegalArgumentException();
    }
  }
}
