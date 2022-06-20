package com.gurudev.aircnc.constant;

public interface Regex {

  String EMAIL = "[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z0-9][a-zA-Z0-9-]*[a-zA-Z0-9]";
  String PHONE_NUMBER = "([0-9]{2,3}-([0-9]{3,4}-[0-9]{4}))";

}
