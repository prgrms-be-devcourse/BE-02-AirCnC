package com.gurudev.aircnc.controller;

import static java.time.LocalDate.now;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
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
    String roomId = 숙소_등록("나의 숙소", new Address("달나라 1번지", "달나라 1길", "100호", "1234"),
        "달토끼가 사는 나의 숙소", "100000", "2");

    로그인("guest@naver.com", "guest1234!");

    ObjectNode tripReserveRequest = objectMapper.createObjectNode();
    ObjectNode trip = tripReserveRequest.putObject("trip");
    trip.put("checkIn", checkIn)
        .put("checkOut", checkOut)
        .put("totalPrice", "100000")
        .put("headCount", "2")
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
        );
  }
}