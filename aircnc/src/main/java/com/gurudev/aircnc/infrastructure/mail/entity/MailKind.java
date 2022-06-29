package com.gurudev.aircnc.infrastructure.mail.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MailKind {
  REGISTER("등록"),
  UPDATE("변경"),
  DELETE("삭제");

  private final String status;
}
