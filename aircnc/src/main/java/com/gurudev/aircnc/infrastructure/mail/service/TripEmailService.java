package com.gurudev.aircnc.infrastructure.mail.service;

import static java.lang.String.format;

import com.gurudev.aircnc.infrastructure.mail.entity.MailType;
import java.util.Map;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring5.SpringTemplateEngine;

@Service
public class TripEmailService extends AbstractEmailService {

  private static final String TEMPLATE_NAME = "trip-template";

  protected TripEmailService(SpringTemplateEngine springTemplateEngine,
      JavaMailSender emailSender) {
    super(springTemplateEngine, emailSender);
  }

  @Override
  public void send(String receiverEmail, Map<String, Object> contentMap, MailType mailKind) {
    String title = format("AirCnc 여행 %s 알림", mailKind.getStatus());

    Map<String, Object> contents = putMailKind(contentMap, mailKind);

    try {
      emailSender.send(createMessage(receiverEmail, contents, title, TEMPLATE_NAME));
    } catch (MailException ex) {
      throw new RuntimeException("메일 전송에 실패하였습니다", ex);
    }


  }
}
