package com.gurudev.aircnc.infrastructure.mail;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EmailServiceTest { // 사용방법을 위한 Temporary 테스트

  @Autowired
  @Qualifier("memberEmailService")
  private EmailService memberEmailService;

  @Autowired
  @Qualifier("roomEmailService")
  private EmailService roomEmailService;

  @Autowired
  @Qualifier("tripEmailService")
  private EmailService tripEmailService;

  @Test
  void 이메일_인증_전송_테스트() {
    memberEmailService.send("rlfrmsdh1@gmail.com", Collections.emptyMap(), MailKind.REGISTER);
  }

  @Test
  void 숙소_삭제_이메일_전송_테스트() {
    roomEmailService.send("rlfrmsdh1@gmail.com",
        Map.of("name", "숙소이름", "address", "숙소주소", "pricePerDay", 30000), MailKind.DELETE);
  }

  @Test
  void 숙소_등록_이메일_전송_테스트() {
    roomEmailService.send("rlfrmsdh1@gmail.com",
        Map.of("name", "숙소이름", "address", "숙소주소", "pricePerDay", 30000), MailKind.REGISTER);
  }

  @Test
  void 숙소_변경_이메일_전송_테스트() {
    roomEmailService.send("rlfrmsdh1@gmail.com",
        Map.of("name", "숙소이름", "address", "숙소주소", "pricePerDay", 30000), MailKind.UPDATE);
  }

  @Test
  void 여행_변경_이메일_전송_테스트() {
    tripEmailService.send("rlfrmsdh1@gmail.com",
        Map.of("checkIn", LocalDate.now().minusDays(5),
            "checkOut", LocalDate.now(),
            "totalPrice", 100000,
            "headCount", 5), MailKind.UPDATE);
  }
}

