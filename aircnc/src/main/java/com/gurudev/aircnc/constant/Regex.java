package com.gurudev.aircnc.constant;

import static lombok.AccessLevel.PRIVATE;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class Regex {

  public static final String EMAIL = "[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z0-9][a-zA-Z0-9-]*[a-zA-Z0-9]";
  public static final String PHONE_NUMBER = "^01\\d{1}-\\d{3,4}-\\d{4}$";
  public static final String PASSWORD_ENCODING_PREFIX = "\\{\\w+\\}";
}
