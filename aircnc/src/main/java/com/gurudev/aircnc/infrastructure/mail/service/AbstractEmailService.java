package com.gurudev.aircnc.infrastructure.mail.service;

import com.gurudev.aircnc.infrastructure.mail.entity.MailType;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Map.Entry;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.context.Context;
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

  protected MimeMessage createMessage(
      String receiverMail, Map<String, Object> contentMap,
      String title, String templateName) {
    try {
      MimeMessage message = emailSender.createMimeMessage();

      message.addRecipients(RecipientType.TO, receiverMail); //보내는 대상
      message.setFrom(sender); //보내는 사람
      message.setSubject(title); //제목
      message.setText(getContent(contentMap, templateName), CHARSET, CONTENT_TYPE); //내용

      return message;
    } catch (Exception ex) {
      throw new RuntimeException("메일 내용 생성에 실패했습니다", ex);
    }
  }

  protected String getTemplateName(MailType mailType, String templatePrefix) {
    return templatePrefix + mailType.name().toLowerCase();
  }

  private String getContent(Map<String, Object> contentMap, String templateName) {
    Context context = new Context();
    for (Entry<String, Object> entry : contentMap.entrySet()) {
      context.setVariable(entry.getKey(), entry.getValue());
    }
    return springTemplateEngine.process(templateName, context);
  }
}
