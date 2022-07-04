package com.gurudev.aircnc.controller;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gurudev.aircnc.controller.support.RestDocsTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class MemberControllerTest extends RestDocsTestSupport {

  @Test
  void 회원가입_API() throws Exception {
    //given
    ObjectNode memberRegisterRequest = objectMapper.createObjectNode();
    ObjectNode member = memberRegisterRequest.putObject("member");
    member.put("email", "seunghan@gamil.com")
        .put("password", "pass12343")
        .put("name", "seunghan")
        .put("birthDate", "1998-04-21")
        .put("phoneNumber", "010-1234-5678")
        .put("role", "GUEST");

    //when
    mockMvc.perform(post("/api/v1/members")
            .contentType(MediaType.APPLICATION_JSON)
            .content(memberRegisterRequest.toString()))

        //then
        .andExpect(status().isCreated())
        .andExpectAll(
            jsonPath("$.member.email").value("seunghan@gamil.com"),
            jsonPath("$.member.name").value("seunghan"),
            jsonPath("$.member.birthDate").value("1998-04-21"),
            jsonPath("$.member.phoneNumber").value("010-1234-5678"),
            jsonPath("$.member.role").value("GUEST")
        )
        .andDo(print())

        //docs
        .andDo(
            restDocs.document(
                requestFields(
                    fieldWithPath("member.email").type(STRING).description("이메일"),
                    fieldWithPath("member.password").type(STRING).description("비밀번호"),
                    fieldWithPath("member.name").type(STRING).description("이름"),
                    fieldWithPath("member.birthDate").type(STRING).description("생년월일"),
                    fieldWithPath("member.phoneNumber").type(STRING).description("휴대폰 번호"),
                    fieldWithPath("member.role").type(STRING).description("역할")
                ),
                responseFields(
                    fieldWithPath("member.email").type(STRING).description("이메일"),
                    fieldWithPath("member.name").type(STRING).description("이름"),
                    fieldWithPath("member.birthDate").type(STRING).description("생년월일"),
                    fieldWithPath("member.phoneNumber").type(STRING).description("휴대폰 번호"),
                    fieldWithPath("member.role").type(STRING).description("역할")
                )
            )
        );
  }

  @Test
  void 로그인_API() throws Exception {
    //given
    멤버_등록("seunghan@gamil.com", "pass12343", "seunghan", "GUEST");

    ObjectNode loginRequest = objectMapper.createObjectNode();
    ObjectNode loginMember = loginRequest.putObject("member");
    loginMember.put("email", "seunghan@gamil.com")
        .put("password", "pass12343");

    //when
    mockMvc.perform(post("/api/v1/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(loginRequest.toString()))

        //then
        .andExpect(status().isOk())
        .andExpectAll(
            jsonPath("$.member.id").exists(),
            jsonPath("$.member.email").value("seunghan@gamil.com"),
            jsonPath("$.member.name").value("seunghan"),
            jsonPath("$.member.role").value("GUEST"),
            jsonPath("$.member.token").exists()
        )
        .andDo(print())

        //docs
        .andDo(
            restDocs.document(
                requestFields(
                    fieldWithPath("member.email").type(STRING).description("이메일"),
                    fieldWithPath("member.password").type(STRING).description("비밀번호")
                ),
                responseFields(
                    fieldWithPath("member.id").type(NUMBER).description("아이디"),
                    fieldWithPath("member.email").type(STRING).description("이메일"),
                    fieldWithPath("member.name").type(STRING).description("이름"),
                    fieldWithPath("member.role").type(STRING).description("역할"),
                    fieldWithPath("member.token").type(STRING).description("인증 토큰")
                )
            )
        );
  }

  @Test
  void 정보조회_API() throws Exception {
    //given
    멤버_등록("seunghan@gamil.com", "pass12343", "seunghan", "GUEST");
    로그인("seunghan@gamil.com", "pass12343");

    //when
    mockMvc.perform(get("/api/v1/me")
            .header(AUTHORIZATION, token))

        //then
        .andExpect(status().isOk())
        .andExpectAll(
            jsonPath("$.member.email").value("seunghan@gamil.com"),
            jsonPath("$.member.name").value("seunghan"),
            jsonPath("$.member.birthDate").value("1998-04-21"),
            jsonPath("$.member.phoneNumber").value("010-1234-5678"),
            jsonPath("$.member.role").value("GUEST")
        )
        .andDo(print())
        //docs
        .andDo(
            restDocs.document(
                requestHeaders(
                    headerWithName(AUTHORIZATION).description("인증 토큰")
                ),
                responseFields(
                    fieldWithPath("member.email").type(STRING).description("이메일"),
                    fieldWithPath("member.name").type(STRING).description("이름"),
                    fieldWithPath("member.birthDate").type(STRING).description("생년월일"),
                    fieldWithPath("member.phoneNumber").type(STRING).description("휴대폰 번호"),
                    fieldWithPath("member.role").type(STRING).description("역할")
                )
            )
        );
  }
}
