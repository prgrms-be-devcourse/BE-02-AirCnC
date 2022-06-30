package com.gurudev.aircnc.infrastructure.mail;

import java.util.Map;

public interface EmailService {

  //TODO : 숙소 등록, 변경, 삭제, 여행 등록, 삭제 메시지 작성

  void send(String receiverEmail, Map<String, Object> contentMap, MailKind mailKind);
}
