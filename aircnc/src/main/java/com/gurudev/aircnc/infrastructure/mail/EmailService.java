package com.gurudev.aircnc.infrastructure.mail;

import javax.mail.internet.MimeMessage;

public interface EmailService {

  MimeMessage createSignUpMsg(String to) throws Exception;

  //TODO : 숙소 등록, 변경, 삭제, 여행 등록, 삭제 메시지 작성

  void sendMessage(MimeMessage message);
}
