package com.gurudev.aircnc.infrastructure.mail;

import java.util.Map;
import java.util.Random;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;


@Component
public class MemberEmailService extends AbstractEmailService {

  private static final String TEMPLATE_NAME = "register-member";
  public static final String authenticationKey = createKey();
  private static final String MESSAGE_TITLE = "AirCnC 회원가입 이메일 인증";

  protected MemberEmailService(@Autowired SpringTemplateEngine springTemplateEngine,
      @Autowired JavaMailSender emailSender) {
    super(springTemplateEngine, emailSender);
  }


  private MimeMessage createMessage(String receiverMail) {
    try {
      MimeMessage message = emailSender.createMimeMessage();

      message.addRecipients(RecipientType.TO, receiverMail); //보내는 대상
      message.setSubject(MESSAGE_TITLE); //제목

      Context context = new Context();
      context.setVariable("code", authenticationKey);
      String content = springTemplateEngine.process(TEMPLATE_NAME, context);

      message.setText(content, CHARSET, CONTENT_TYPE); //내용
      message.setFrom(sender); //보내는 사람
      return message;
    } catch (Exception ex) {
      throw new RuntimeException("메일 내용 생성에 실패했습니다", ex);
    }
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
  public void send(String receiverMail, Map<String, String> contentMap, MailKind mailKind) {
    try {
      MimeMessage signUpMsg = createMessage(receiverMail);
      emailSender.send(signUpMsg);
    } catch (MailException ex) {
      throw new RuntimeException("메일 전송에 실패하였습니다", ex);
    }
  }
}
