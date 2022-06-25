package com.gurudev.aircnc.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.gurudev.aircnc.controller.support.RestDocsTestSupport;
import com.gurudev.aircnc.domain.room.entity.Address;
import java.io.FileInputStream;
import java.io.InputStream;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

class RoomControllerTest extends RestDocsTestSupport {

  String name = "나의 숙소";
  String lotAddress = "달나라 1번지";
  String roadAddress = "달나라 1길";
  String detailedAddress = "100호";
  String postCode = "1234";
  String description = "달 토끼가 사는 나의 숙소";
  String pricePerDay = "100000";
  String capacity = "2";

  String address = String.join("", roadAddress, " ", detailedAddress);

  @Test
  void 숙소_등록() throws Exception {
    InputStream requestInputStream =  new FileInputStream("src/test/resources/room-photos-src/photo1.jpeg");
    MockMultipartFile requestImage = new MockMultipartFile("roomPhotosFile", "photo1.jpeg", IMAGE_JPEG_VALUE, requestInputStream);

    로그인("host@naver.com", "host1234!");

    mockMvc.perform(multipart("/api/v1/rooms")
            .file(requestImage)
            .param("name",name)
            .param("lotAddress",lotAddress)
            .param("roadAddress",roadAddress)
            .param("detailedAddress",detailedAddress)
            .param("postCode",postCode)
            .param("description",description)
            .param("pricePerDay",pricePerDay)
            .param("capacity",capacity)
        .header(AUTHORIZATION, token))
        .andExpect(status().isCreated())
        .andExpectAll(
            jsonPath("$.room.id").exists(),
            jsonPath("$.room.name").value(name),
            jsonPath("$.room.address").value(address),
            jsonPath("$.room.description").value(description),
            jsonPath("$.room.pricePerDay").value(pricePerDay),
            jsonPath("$.room.capacity").value(capacity),
            jsonPath("$.room.fileNames", hasSize(1))
        )
        .andDo(
            restDocs.document(
                requestHeaders(
                    headerWithName(AUTHORIZATION).description("인증 토큰")
                ),
                requestParameters(
                    parameterWithName("name").description("이름")
                    ,parameterWithName("lotAddress").description("지번 주소")
                    ,parameterWithName("roadAddress").description("도로명 주소")
                    ,parameterWithName("detailedAddress").description("상세 주소")
                    ,parameterWithName("postCode").description("우편 번호")
                    ,parameterWithName("description").description("설명")
                    ,parameterWithName("pricePerDay").description("1박당 가격")
                    ,parameterWithName("capacity").description("인원 수")
                ),
                requestParts(
                    partWithName("roomPhotosFile").description("숙소 사진")
                ),
                responseFields(
                    fieldWithPath("room.id").type(NUMBER).description("숙소 아이디"),
                    fieldWithPath("room.name").type(STRING).description("이름"),
                    fieldWithPath("room.address").type(STRING).description("주소"),
                    fieldWithPath("room.description").type(STRING).description("설명"),
                    fieldWithPath("room.pricePerDay").type(NUMBER).description("1박당 가격"),
                    fieldWithPath("room.capacity").type(NUMBER).description("인원 수"),
                    fieldWithPath("room.fileNames").type(ARRAY).description("등록한 파일의 이름")
                )
            )
        );
  }

  @Test
  void 속소_리스트_조회_API() throws Exception {
    숙소_등록(name + 1);
    숙소_등록(name + 2);
    숙소_등록(name + 3);

    mockMvc.perform(get("/api/v1/rooms"))
        .andExpect(status().isOk())
        .andExpectAll(
            jsonPath("$.rooms[*].id").exists(),
            jsonPath("$.rooms[*].address").exists(),
            jsonPath("$.rooms[*].pricePerDay").exists(),
            jsonPath("$.rooms[*].photoUrl").exists()
        );

  }

  private void 숙소_등록(String name) throws Exception {
    InputStream requestInputStream = new FileInputStream(
        "src/test/resources/room-photos-src/photo1.jpeg");
    MockMultipartFile requestImage = new MockMultipartFile("roomPhotosFile", "photo1.jpeg",
        IMAGE_JPEG_VALUE, requestInputStream);

    로그인("host@naver.com", "host1234!");

    mockMvc.perform(multipart("/api/v1/rooms")
            .file(requestImage)
            .param("name", name)
            .param("lotAddress", lotAddress)
            .param("roadAddress", roadAddress)
            .param("detailedAddress", detailedAddress)
            .param("postCode", postCode)
            .param("description", description)
            .param("pricePerDay", pricePerDay)
            .param("capacity", capacity)
            .header(AUTHORIZATION, token))
        .andExpect(status().isCreated());

  }

}
