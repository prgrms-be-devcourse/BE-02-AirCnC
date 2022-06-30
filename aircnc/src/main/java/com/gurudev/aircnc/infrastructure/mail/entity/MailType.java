package com.gurudev.aircnc.infrastructure.mail.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MailType {
  REGISTER("등록"),
  UPDATE("변경"),
  DELETE("삭제");

  private final String status;
}
