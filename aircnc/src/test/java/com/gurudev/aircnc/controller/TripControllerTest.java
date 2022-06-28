package com.gurudev.aircnc.controller;

import static java.time.LocalDate.now;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gurudev.aircnc.controller.support.RestDocsTestSupport;
import com.gurudev.aircnc.domain.room.entity.Address;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class TripControllerTest extends RestDocsTestSupport {

  @Test
  void 여행_예약_성공() throws Exception {
    //given
    String checkIn = now().toString();
    String checkOut = now().plusDays(1).toString();

    로그인("host@naver.com", "host1234!");
    Long roomId = 숙소_등록("나의 숙소", new Address("달나라 1번지", "달나라 1길", "100호", "1234"),
        "달토끼가 사는 나의 숙소", "100000", "2");

    로그인("guest@naver.com", "guest1234!");

    ObjectNode tripReserveRequest = objectMapper.createObjectNode();
    ObjectNode trip = tripReserveRequest.putObject("trip");
    trip.put("checkIn", checkIn)
        .put("checkOut", checkOut)
        .put("totalPrice", 100000)
        .put("headCount", 2)
        .put("roomId", roomId);

    //when
    mockMvc.perform(post("/api/v1/trips")
            .contentType(MediaType.APPLICATION_JSON)
            .content(createJson(tripReserveRequest))
            .header(AUTHORIZATION, token))

        //then
        .andExpect(status().isCreated())
        .andExpectAll(
            jsonPath("$.trip.id").exists(),
            jsonPath("$.trip.checkIn").value(checkIn),
            jsonPath("$.trip.checkOut").value(checkOut),
            jsonPath("$.trip.totalPrice").value("100000"),
            jsonPath("$.trip.headCount").value("2"),
            jsonPath("$.trip.status").value("RESERVED")
        )

        //docs
        .andDo(
            restDocs.document(
                requestHeaders(
                    headerWithName(AUTHORIZATION).description("인증 토큰")
                ),
                requestFields(
                    fieldWithPath("trip.roomId").type(NUMBER).description("숙소 아이디"),
                    fieldWithPath("trip.checkIn").type(STRING).description("체크인 날짜"),
                    fieldWithPath("trip.checkOut").type(STRING).description("체크아웃 날짜"),
                    fieldWithPath("trip.totalPrice").type(NUMBER).description("총 가격"),
                    fieldWithPath("trip.headCount").type(NUMBER).description("인원 수")
                ),
                responseFields(
                    fieldWithPath("trip.id").type(NUMBER).description("여행 아이디"),
                    fieldWithPath("trip.checkIn").type(STRING).description("체크인 날짜"),
                    fieldWithPath("trip.checkOut").type(STRING).description("체크아웃 날짜"),
                    fieldWithPath("trip.totalPrice").type(NUMBER).description("총 가격"),
                    fieldWithPath("trip.headCount").type(NUMBER).description("인원 수"),
                    fieldWithPath("trip.roomId").type(NUMBER).description("숙소 아이디"),
                    fieldWithPath("trip.status").type(STRING).description("여행 상태 (항상 RESERVED)")
                )
            )
        );
  }


  @Test
  void 여행_목록_조회_성공() throws Exception {
    //given
    //여행 필드
    String checkIn = now().toString();
    String checkOut = now().plusDays(1).toString();

    //숙소 세팅
    로그인("host@naver.com", "host1234!");
    Long roomId = 숙소_등록("나의 숙소", new Address("달나라 1번지", "달나라 1길", "100호", "1234"),
        "달토끼가 사는 나의 숙소", "100000", "2");

    //조회할 여행 등록
    로그인("guest@naver.com", "guest1234!");
    Long tripId = 여행_등록(checkIn, checkOut, 100000, 2, roomId);

    //when
    mockMvc.perform(get("/api/v1/trips")
            .contentType(MediaType.APPLICATION_JSON)
            .header(AUTHORIZATION, token))
        //then
        .andExpect(status().isOk())
        .andExpectAll(
            jsonPath("$.trips", hasSize(1)),
            jsonPath("$.trips[0].id").value(tripId),
            jsonPath("$.trips[0].checkIn").value(checkIn),
            jsonPath("$.trips[0].checkOut").value(checkOut),
            jsonPath("$.trips[0].totalPrice").value("100000"),
            jsonPath("$.trips[0].headCount").value("2"),
            jsonPath("$.trips[0].status").exists()
        )
        .andDo(
            restDocs.document(
                requestHeaders(
                    headerWithName(AUTHORIZATION).description("인증 토큰")
                ),
                responseFields(
                    fieldWithPath("trips").type(ARRAY).description("여행 목록"),
                    fieldWithPath("trips[].id").type(NUMBER).description("여행 아이디"),
                    fieldWithPath("trips[].checkIn").type(STRING).description("체크인 날짜"),
                    fieldWithPath("trips[].checkOut").type(STRING).description("체크아웃 날짜"),
                    fieldWithPath("trips[].totalPrice").type(NUMBER).description("총 가격"),
                    fieldWithPath("trips[].headCount").type(NUMBER).description("인원 수"),
                    fieldWithPath("trips[].roomId").type(NUMBER).description("숙소 아이디"),
                    fieldWithPath("trips[].status").type(STRING).description("여행 상태")
                )
            )
        );
  }

  @Test
  void 여행_상세_조회_성공() throws Exception {
    //given
    //여행 필드
    String checkIn = now().toString();
    String checkOut = now().plusDays(1).toString();

    //숙소 세팅
    로그인("host@naver.com", "host1234!");
    Long roomId = 숙소_등록("나의 숙소", new Address("달나라 1번지", "달나라 1길", "100호", "1234"),
        "달토끼가 사는 나의 숙소", "100000", "2");

    //상세 조회할 여행 등록
    로그인("guest@naver.com", "guest1234!");
    Long tripId = 여행_등록(checkIn, checkOut, 100000, 2, roomId);

    //when
    mockMvc.perform(get("/api/v1/trips/{tripId}", tripId)
            .contentType(MediaType.APPLICATION_JSON)
            .header(AUTHORIZATION, token))
        //then
        .andExpect(status().isOk())
        .andExpectAll(
            jsonPath("$.trip.id").value(tripId),
            jsonPath("$.trip.checkIn").value(checkIn),
            jsonPath("$.trip.checkOut").value(checkOut),
            jsonPath("$.trip.totalPrice").value("100000"),
            jsonPath("$.trip.headCount").value("2"),
            jsonPath("$.trip.status").value("RESERVED"),
            jsonPath("$.trip.room.id").value(roomId),
            jsonPath("$.trip.room.name").value("나의 숙소"),
            jsonPath("$.trip.room.fileNames", hasSize(1)),
            jsonPath("$.trip.room.hostName").value("호스트"),
            jsonPath("$.trip.room.address").value("달나라 1길 100호")
        )
        //docs
        .andDo(
            restDocs.document(
                requestHeaders(
                    headerWithName(AUTHORIZATION).description("인증 토큰")
                ),
                responseFields(
                    fieldWithPath("trip.id").type(NUMBER).description("여행 아이디"),
                    fieldWithPath("trip.checkIn").type(STRING).description("체크인 날짜"),
                    fieldWithPath("trip.checkOut").type(STRING).description("체크아웃 날짜"),
                    fieldWithPath("trip.totalPrice").type(NUMBER).description("총 가격"),
                    fieldWithPath("trip.headCount").type(NUMBER).description("인원 수"),
                    fieldWithPath("trip.status").type(STRING).description("여행 상태"),
                    fieldWithPath("trip.room.id").type(NUMBER).description("숙소 아이디"),
                    fieldWithPath("trip.room.name").type(STRING).description("숙소 이름"),
                    fieldWithPath("trip.room.fileNames").type(ARRAY).description("숙소 사진의 파일 이름 목록"),
                    fieldWithPath("trip.room.hostName").type(STRING).description("숙소 호스트 이름"),
                    fieldWithPath("trip.room.address").type(STRING).description("숙소 주소")
                )
            )
        );
  }

  @Test
  void 여행_취소_성공() throws Exception {
    //given
    //여행 필드
    String checkIn = now().toString();
    String checkOut = now().plusDays(1).toString();

    //숙소 세팅
    로그인("host@naver.com", "host1234!");
    Long roomId = 숙소_등록("나의 숙소", new Address("달나라 1번지", "달나라 1길", "100호", "1234"),
        "달토끼가 사는 나의 숙소", "100000", "2");

    //취소할 여행 등록
    로그인("guest@naver.com", "guest1234!");
    Long tripId = 여행_등록(checkIn, checkOut, 100000, 2, roomId);

    //when
    mockMvc.perform(post("/api/v1/trips/{tripId}/cancel", tripId)
            .contentType(MediaType.APPLICATION_JSON)
            .header(AUTHORIZATION, token))
        //then
        .andExpect(status().isOk())
        .andExpectAll(
            jsonPath("$.trip.id").value(tripId),
            jsonPath("$.trip.checkIn").value(checkIn),
            jsonPath("$.trip.checkOut").value(checkOut),
            jsonPath("$.trip.totalPrice").value("100000"),
            jsonPath("$.trip.headCount").value("2"),
            jsonPath("$.trip.status").value("CANCELLED"),
            jsonPath("$.trip.roomId").value(roomId)
        )
        //docs
        .andDo(
            restDocs.document(
                requestHeaders(
                    headerWithName(AUTHORIZATION).description("인증 토큰")
                ),
                responseFields(
                    fieldWithPath("trip.id").type(NUMBER).description("여행 아이디"),
                    fieldWithPath("trip.checkIn").type(STRING).description("체크인 날짜"),
                    fieldWithPath("trip.checkOut").type(STRING).description("체크아웃 날짜"),
                    fieldWithPath("trip.totalPrice").type(NUMBER).description("총 가격"),
                    fieldWithPath("trip.headCount").type(NUMBER).description("인원 수"),
                    fieldWithPath("trip.status").type(STRING).description("여행 상태 (항상 CANCELLED)"),
                    fieldWithPath("trip.roomId").type(NUMBER).description("숙소 아이디")
                )
            )
        );
  }
}