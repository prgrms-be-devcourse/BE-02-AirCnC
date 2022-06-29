package com.gurudev.aircnc.infrastructure.mail;

public enum MailKind {
  REGISTER("등록"),
  UPDATE("변경"),
  DELETE("삭제");

  private final String status;

  MailKind(String status) {
    this.status = status;
  }

  public String getStatus() {
    return this.status;
  }
}
