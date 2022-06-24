package com.gurudev.aircnc.controller.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gurudev.aircnc.exception.AircncRuntimeException;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class BaseControllerTest {

  @Autowired
  protected MockMvc mockMvc;

  @Autowired
  protected ObjectMapper objectMapper;

  protected static String token;

  protected String createJson(Object dto) throws JsonProcessingException {
    return objectMapper.writeValueAsString(dto);
  }

  @BeforeEach
  void setUp() throws Exception {
    멤버_등록("guest@naver.com", "guest1234!", "guest", "GUEST");
    멤버_등록("host@naver.com", "host1234!", "host", "HOST");
  }

  protected void 멤버_등록(String email, String password, String name, String role) throws Exception {
    ObjectNode memberRegisterRequest = objectMapper.createObjectNode();
    ObjectNode member = memberRegisterRequest.putObject("member");
    member.put("email", email)
        .put("password", password)
        .put("name", name)
        .put("birthDate", "1998-04-21") // random date
        .put("phoneNumber", "010-1234-5678") // random number
        .put("role", role);

    mockMvc.perform(post("/api/v1/members")
            .contentType(MediaType.APPLICATION_JSON)
            .content(memberRegisterRequest.toString()))
        .andExpect(status().isCreated());
  }

  protected void 로그인(String email, String password) throws Exception {
    ObjectNode loginRequest = objectMapper.createObjectNode();
    ObjectNode member = loginRequest.putObject("member");
    member.put("email", email)
        .put("password", password);

    MockHttpServletResponse response = mockMvc.perform(post("/api/v1/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(loginRequest.toString()))
        .andExpect(status().isOk())
        .andReturn().getResponse();

    token = objectMapper.readValue(response.getContentAsString(),
        JsonNode.class).get("member").get("token").asText();

    assertThat(token).isNotNull();

  }
}
