package com.gurudev.aircnc.controller;

import static com.google.common.net.HttpHeaders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.net.HttpHeaders;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;


class MemberControllerTest extends BasicControllerTest {

  @Test
  void 회원가입_API() throws Exception {
    ObjectNode memberRegisterRequest = objectMapper.createObjectNode();
    ObjectNode member = memberRegisterRequest.putObject("member");
    member.put("email", "seunghan@gamil.com")
        .put("password", "pass12343")
        .put("name", "seunghan")
        .put("birthDate", "1998-04-21")
        .put("phoneNumber", "010-1234-5678")
        .put("role", "GUEST");

    mockMvc.perform(post("/api/v1/members")
            .contentType(MediaType.APPLICATION_JSON)
            .content(memberRegisterRequest.toString()))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.member.email").value("seunghan@gamil.com"))
        .andExpect(jsonPath("$.member.name").value("seunghan"))
        .andExpect(jsonPath("$.member.birthDate").value("1998-04-21"))
        .andExpect(jsonPath("$.member.phoneNumber").value("010-1234-5678"))
        .andExpect(jsonPath("$.member.role").value("GUEST"));
  }

  @Test
  void 로그인_API() throws Exception {
    String email = "seunghan@gamil.com";
    String password = "pass12343";
    String name = "seunghan";
    String role = "GUEST";

    멤버_등록(email,password,name,role);

    ObjectNode loginRequest = objectMapper.createObjectNode();
    ObjectNode loginMember = loginRequest.putObject("member");
    loginMember.put("email", email)
        .put("password", password);

    mockMvc.perform(post("/api/v1/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(loginRequest.toString()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.member.email").value(email))
        .andExpect(jsonPath("$.member.name").value(name))
        .andExpect(jsonPath("$.member.role").value("GUEST"))
        .andExpect(jsonPath("$.member.token").exists());
  }


  @Test
  void 정보조회_API() throws Exception {
    String email = "seunghan@gamil.com";
    String password = "pass12343";
    String name = "seunghan";
    String role = "GUEST";

    멤버_등록(email,password,name,role);
    로그인(email, password);

    mockMvc.perform(get("/api/v1/me")
            .header(AUTHORIZATION,token))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.member.email").value(email))
        .andExpect(jsonPath("$.member.name").value(name))
        .andExpect(jsonPath("$.member.birthDate").value("1998-04-21"))
        .andExpect(jsonPath("$.member.phoneNumber").value("010-1234-5678"))
        .andExpect(jsonPath("$.member.role").value(role));
  }


}
