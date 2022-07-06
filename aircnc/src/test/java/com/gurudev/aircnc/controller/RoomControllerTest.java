package com.gurudev.aircnc.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.gurudev.aircnc.controller.support.RestDocsTestSupport;
import com.gurudev.aircnc.domain.room.entity.Address;
import com.gurudev.aircnc.domain.trip.entity.Trip;
import com.gurudev.aircnc.domain.trip.service.TripService;
import com.gurudev.aircnc.infrastructure.event.TripEvent;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

class RoomControllerTest extends RestDocsTestSupport {

  @Autowired
  private TripService tripService;

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
        .andExpectAll(
            jsonPath("$.rooms[0].id").exists(),
            jsonPath("$.rooms[0].name").value("나의 숙소"),
            jsonPath("$.rooms[0].address").value("달나라 1길 100호"),
            jsonPath("$.rooms[0].description").value("달토끼가 사는 나의 숙소"),
            jsonPath("$.rooms[0].pricePerDay").value("100000"),
            jsonPath("$.rooms[0].capacity").value("2"),
            jsonPath("$.rooms[1].name").value("나의 숙소2"),
            jsonPath("$.rooms", hasSize(2))
        )

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


  @Test
  void 숙소_상세_조회() throws Exception {
    //given
    //숙소 등록
    로그인("host@naver.com", "host1234!");
    Long roomId = 숙소_등록("나의 숙소", new Address("달나라 1번지", "달나라 1길", "100호", "1234"),
        "달토끼가 사는 나의 숙소", "100000", "2");

    //여행 등록
    Long guestId = 로그인("guest@naver.com", "guest1234!");
    여행_등록(LocalDate.now().plusDays(0), LocalDate.now().plusDays(1),100000,2,roomId,guestId);
    여행_등록(LocalDate.now().plusDays(2), LocalDate.now().plusDays(4),200000,2,roomId,guestId);

    //when
    mockMvc.perform(get("/api/v1/rooms/{roomId}",roomId))

      //then
      .andExpect(status().isOk())
      .andExpectAll(
        jsonPath("$.room.name").value("나의 숙소"),
        jsonPath("$.room.address").value("달나라 1길 100호"),
        jsonPath("$.room.pricePerDay").value("100000"),
        jsonPath("$.room.capacity").value("2"),
        jsonPath("$.room.unAvailableDays",hasSize(4)),
        jsonPath("$.room.photoUrls",hasSize(1)),
        jsonPath("$.room.description").value("달토끼가 사는 나의 숙소"))
      .andDo(print())

      //docs
      .andDo(
        restDocs.document(
//            pathParameters(
//                parameterWithName("roomId").description("숙소 아이디")
//            ),
            responseFields(
                fieldWithPath("room.name").type(STRING).description("숙소 이름"),
                fieldWithPath("room.address").type(STRING).description("숙소 주소"),
                fieldWithPath("room.pricePerDay").type(NUMBER).description("1박당 가격"),
                fieldWithPath("room.capacity").type(NUMBER).description("허용 인원 수"),
                fieldWithPath("room.unAvailableDays").type(ARRAY).description("이용 불가능 일자 목록"),
                fieldWithPath("room.description").type(STRING).description("설명"),
                fieldWithPath("room.photoUrls").type(ARRAY).description("숙소 사진의 파일 이름 목록")
            )
        )
    );
  }

  private Long 여행_등록(LocalDate checkIn, LocalDate checkOut,
      int totalPrice, int headCount, Long roomId, Long guestId) {

    Trip reservedTrip = tripService.reserve(
        new TripEvent(guestId, roomId, checkIn, checkOut, headCount, totalPrice));

    return reservedTrip.getId();

  }
}