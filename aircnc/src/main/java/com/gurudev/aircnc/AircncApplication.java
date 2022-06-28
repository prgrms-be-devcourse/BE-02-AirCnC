package com.gurudev.aircnc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class AircncApplication {

  public static void main(String[] args) {
    SpringApplication.run(AircncApplication.class, args);
  }

}
