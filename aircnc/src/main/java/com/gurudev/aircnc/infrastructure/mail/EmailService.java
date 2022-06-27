package com.gurudev.aircnc.infrastructure.mail;

public interface EmailService {

  String sendSimpleMessage(String to) throws Exception;
}
