package com.gurudev.aircnc.infrastructure.mail;

import java.util.Properties;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@ConfigurationProperties(prefix = "spring.mail")
@Setter
public class MailSender {

  private String defaultEncoding;
  private int port;

  private String protocol;

  private String host;

  private String username;

  private String password;

  private Properties properties;

  @Bean
  public JavaMailSender javaMailSender() {
    JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
    javaMailSender.setHost(host);
    javaMailSender.setUsername(username);
    javaMailSender.setPassword(password);
    javaMailSender.setPort(port);
    javaMailSender.setJavaMailProperties(properties);
    javaMailSender.setDefaultEncoding(defaultEncoding);
    return javaMailSender;
  }
}
