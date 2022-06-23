package com.gurudev.aircnc.controller;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureMockMvc
public class BasicControllerTest {

  protected static String token;
  @Autowired
  protected MockMvc mockMvc;

  protected ObjectMapper objectMapper = new ObjectMapper();

  @BeforeEach
  void setUp() {
    유저_등록("guest@naver.com", "guest1234!", "guest", "GUEST");
    유저_등록("host@naver.com", "host1234!", "host", "HOST");
  }

  protected void 유저_등록(String email, String password, String name, String role) {
    try {
      ObjectNode objectNode = objectMapper.createObjectNode();
      ObjectNode member = objectNode.putObject("member");
      member.put("email", email)
          .put("password", password)
          .put("name", name)
          .put("birthDate", "1998-04-21") // random date
          .put("phoneNumber", "010-1234-5678") // random number
          .put("role", role);

      mockMvc.perform(post("/api/v1/members")
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectNode.toString()))
          .andExpect(status().isCreated());
    } catch (Exception e) {
      throw new RuntimeException("테스트 멤버 등록 실패입니다.");
    }
  }


  protected void 로그인(String email, String password) {
    try {
      ObjectNode objectNode = objectMapper.createObjectNode();
      ObjectNode member = objectNode.putObject("member");
      member.put("email", email)
          .put("password", password);

      MockHttpServletResponse response = mockMvc.perform(post("/api/v1/login")
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectNode.toString()))
          .andExpect(status().isOk())
          .andReturn().getResponse();

      token = objectMapper.readValue(response.getContentAsString(),
          JsonNode.class).get("member").get("token").asText();

      assertThat(token).isNotNull();

    } catch (Exception e) {
      throw new RuntimeException("테스트 멤버 로그인 실패입니다.");
    }
  }
}
