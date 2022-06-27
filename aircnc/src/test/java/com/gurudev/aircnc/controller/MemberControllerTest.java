package com.gurudev.aircnc.controller;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gurudev.aircnc.controller.support.RestDocsTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class MemberControllerTest extends RestDocsTestSupport {

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
        .andExpectAll(
            jsonPath("$.member.email").value("seunghan@gamil.com"),
            jsonPath("$.member.name").value("seunghan"),
            jsonPath("$.member.birthDate").value("1998-04-21"),
            jsonPath("$.member.phoneNumber").value("010-1234-5678"),
            jsonPath("$.member.role").value("GUEST")
        );
  }

  @Test
  void 로그인_API() throws Exception {
    멤버_등록("seunghan@gamil.com", "pass12343", "seunghan", "GUEST");

    ObjectNode loginRequest = objectMapper.createObjectNode();
    ObjectNode loginMember = loginRequest.putObject("member");
    loginMember.put("email", "seunghan@gamil.com")
        .put("password", "pass12343");

    mockMvc.perform(post("/api/v1/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(loginRequest.toString()))
        .andExpect(status().isOk())
        .andExpectAll(
            jsonPath("$.member.email").value("seunghan@gamil.com"),
            jsonPath("$.member.name").value("seunghan"),
            jsonPath("$.member.role").value("GUEST"),
            jsonPath("$.member.token").exists()
        );
  }

  @Test
  void 정보조회_API() throws Exception {
    멤버_등록("seunghan@gamil.com", "pass12343", "seunghan", "GUEST");
    로그인("seunghan@gamil.com", "pass12343");

    mockMvc.perform(get("/api/v1/me")
            .header(AUTHORIZATION, token))
        .andExpect(status().isOk())
        .andExpectAll(
            jsonPath("$.member.email").value("seunghan@gamil.com"),
            jsonPath("$.member.name").value("seunghan"),
            jsonPath("$.member.birthDate").value("1998-04-21"),
            jsonPath("$.member.phoneNumber").value("010-1234-5678"),
            jsonPath("$.member.role").value("GUEST")
        );
  }

}
