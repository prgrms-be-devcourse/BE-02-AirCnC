package com.gurudev.aircnc.infrastructure.mail;

import java.util.Map;
import org.springframework.scheduling.annotation.Async;

public interface EmailService {

  @Async
  void send(String receiverEmail, Map<String, Object> contentMap, MailKind mailKind);
}
