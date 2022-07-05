package com.gurudev.aircnc.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.gurudev.aircnc.controller.support.RestDocsTestSupport;
import com.gurudev.aircnc.domain.room.entity.Address;
import org.junit.jupiter.api.Test;

class RoomControllerTest extends RestDocsTestSupport {

  @Test
  void 모든_숙소_조회() throws Exception {
    //given
    로그인("host@naver.com", "host1234!");

    숙소_등록("나의 숙소", new Address("달나라 1번지", "달나라 1길", "100호", "1234"),
        "달토끼가 사는 나의 숙소", "100000", "2");
    숙소_등록("나의 숙소2", new Address("달나라 1번지", "달나라 1길", "103호", "1234"),
        "달토끼가 사는 나의 숙소", "100000", "2");

    //when
    mockMvc.perform(get("/api/v1/rooms"))

        //then
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.rooms[0].id").exists())
        .andExpect(jsonPath("$.rooms[0].name").value("나의 숙소"))
        .andExpect(jsonPath("$.rooms[0].address").value("달나라 1길 100호"))
        .andExpect(jsonPath("$.rooms[0].description").value("달토끼가 사는 나의 숙소"))
        .andExpect(jsonPath("$.rooms[0].pricePerDay").value("100000"))
        .andExpect(jsonPath("$.rooms[0].capacity").value("2"))
        .andExpect(jsonPath("$.rooms[1].name").value("나의 숙소2"))
        .andExpect(jsonPath("$.rooms", hasSize(2)))

        //docs
        .andDo(restDocs.document(
            responseFields(
                fieldWithPath("rooms[]").type(ARRAY).description("숙소 목록"),
                fieldWithPath("rooms[].id").type(NUMBER).description("숙소 아이디"),
                fieldWithPath("rooms[].name").type(STRING).description("이름"),
                fieldWithPath("rooms[].address").type(STRING).description("주소"),
                fieldWithPath("rooms[].description").type(STRING).description("설명"),
                fieldWithPath("rooms[].pricePerDay").type(NUMBER).description("1박당 가격"),
                fieldWithPath("rooms[].capacity").type(NUMBER).description("허용 인원 수"),
                fieldWithPath("rooms[].fileNames").type(ARRAY).description("등록된 파일의 이름")
            ))
        );
  }
}