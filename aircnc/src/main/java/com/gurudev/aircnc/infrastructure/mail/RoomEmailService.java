package com.gurudev.aircnc.infrastructure.mail;

import static java.lang.String.format;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.thymeleaf.spring5.SpringTemplateEngine;

@Component
public class RoomEmailService extends AbstractEmailService {

  private static final String TEMPLATE_NAME = "room-template";

  protected RoomEmailService(@Autowired SpringTemplateEngine springTemplateEngine,
      @Autowired JavaMailSender emailSender) {
    super(springTemplateEngine, emailSender);
  }

  @Override
  public void send(String receiverEmail, Map<String, Object> contentMap, MailKind mailKind) {
    String title = format("AirCnc 숙소 %s 알림", mailKind.getStatus());
    Map<String, Object> addedMap = new HashMap<>(contentMap);
    addedMap.put("behavior", mailKind.getStatus());

    try {
      emailSender.send(createMessage(receiverEmail, addedMap, mailKind, title, TEMPLATE_NAME));
    } catch (MailException ex) {
      throw new RuntimeException("메일 전송에 실패하였습니다", ex);
    }
  }
}
