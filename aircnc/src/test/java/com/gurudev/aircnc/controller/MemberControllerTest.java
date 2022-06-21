package com.gurudev.aircnc.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.UriComponentsBuilder;

@SpringBootTest
@AutoConfigureMockMvc
class MemberControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void 회원가입_API() throws Exception {
    final ObjectNode objectNode = new ObjectMapper().createObjectNode();
    final ObjectNode member = objectNode.putObject("member");
    member.put("email", "seunghan@gamil.com")
        .put("password", "pass12343")
        .put("name", "seunghan")
        .put("birthDate", "1998-04-21")
        .put("phoneNumber", "010-1234-5678")
        .put("role", "GUEST");

    mockMvc.perform(post(UriComponentsBuilder.fromUriString("/api/v1/members")
            .build()
            .toUri())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectNode.toString()))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.member.email").value("seunghan@gamil.com"))
        .andExpect(jsonPath("$.member.name").value("seunghan"))
        .andExpect(jsonPath("$.member.birthDate").value("1998-04-21"))
        .andExpect(jsonPath("$.member.phoneNumber").value("010-1234-5678"))
        .andExpect(jsonPath("$.member.role").value("GUEST"));
  }
}
