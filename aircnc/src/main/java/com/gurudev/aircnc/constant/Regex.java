package com.gurudev.aircnc.constant;

public interface Regex {

  String EMAIL = "[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z0-9][a-zA-Z0-9-]*[a-zA-Z0-9]";
  String PHONE_NUMBER = "^01\\d{1}-\\d{3,4}-\\d{4}$";

}
