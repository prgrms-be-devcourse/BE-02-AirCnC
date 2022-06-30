package com.gurudev.aircnc.infrastructure.mail.service;

import com.gurudev.aircnc.infrastructure.mail.entity.MailType;
import java.util.Map;
import org.springframework.scheduling.annotation.Async;

public interface EmailService {

  @Async
  void send(String receiverEmail, Map<String, Object> contentMap, MailType mailKind);

  default boolean validateKey(String code, String email) {
    return false;
  }
}
