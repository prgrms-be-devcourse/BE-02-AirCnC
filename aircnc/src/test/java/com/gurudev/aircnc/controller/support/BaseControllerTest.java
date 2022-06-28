package com.gurudev.aircnc.controller.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gurudev.aircnc.domain.room.entity.Address;
import java.io.FileInputStream;
import java.io.InputStream;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
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

  protected Long 숙소_등록(String name, Address address, String description,
      String pricePerDay, String capacity) throws Exception {

    InputStream requestInputStream = new FileInputStream(
        "src/test/resources/room-photos-src/photo1.jpeg");
    MockMultipartFile requestImage = new MockMultipartFile("roomPhotosFile", "photo1.jpeg",
        IMAGE_JPEG_VALUE, requestInputStream);

    MvcResult mvcResult = mockMvc.perform(multipart("/api/v1/hosts/rooms")
            .file(requestImage)
            .param("name", name)
            .param("lotAddress", address.getLotAddress())
            .param("roadAddress", address.getRoadAddress())
            .param("detailedAddress", address.getDetailedAddress())
            .param("postCode", address.getPostCode())
            .param("description", description)
            .param("pricePerDay", pricePerDay)
            .param("capacity", capacity)
            .header(AUTHORIZATION, token))
        .andExpect(status().isCreated())
        .andReturn();

    String content = mvcResult.getResponse().getContentAsString();

    return objectMapper.readValue(content, JsonNode.class).get("room").get("id").asLong();
  }

  protected Long 여행_등록(String checkIn, String checkOut,
      int totalPrice, int headCount, long roomId) throws Exception {

    ObjectNode tripReserveRequest = objectMapper.createObjectNode();
    ObjectNode trip = tripReserveRequest.putObject("trip");
    trip.put("checkIn", checkIn)
        .put("checkOut", checkOut)
        .put("totalPrice", totalPrice)
        .put("headCount", headCount)
        .put("roomId", roomId);

    MvcResult mvcResult = mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/trips")
            .contentType(MediaType.APPLICATION_JSON)
            .content(createJson(tripReserveRequest))
            .header(AUTHORIZATION, token))
        .andExpect(status().isCreated())
        .andReturn();

    String content = mvcResult.getResponse().getContentAsString();

    return objectMapper.readValue(content, JsonNode.class).get("trip").get("id").asLong();
  }
}
