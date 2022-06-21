package com.gurudev.aircnc.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gurudev.aircnc.domain.member.entity.Email;
import com.gurudev.aircnc.domain.member.entity.Member;
import com.gurudev.aircnc.domain.member.entity.Password;
import com.gurudev.aircnc.domain.member.entity.PhoneNumber;
import com.gurudev.aircnc.domain.member.entity.Role;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.naming.factory.SendMailFactory;
import org.springframework.format.annotation.DateTimeFormat;

public class MemberDto {

  @Getter
  public static class RegistMemberDto {

    @JsonProperty("member")
    private Request request;

    public Member convert(){
      return request.convert();
    }
    @Getter
    public static class Request {

      private String email;
      private String password;
      private String name;

      @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
      private LocalDate birthDate;
      private String phoneNumber;
      private String role;

      public Member convert(){
        return new Member(new Email(email),
            new Password(password),
            name, birthDate, new PhoneNumber(phoneNumber),
            Role.valueOf(role));
      }
    }

  }


  @Getter
  @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
  public static class MemberResponse {

    @JsonProperty("member")
    private final Response response;

    public static MemberResponse convert(Member member) {
      return new MemberResponse(Response.convert(member));
    }

    @Getter
    public static class Response {

      private final String email;
      private final String name;
      private final LocalDate birthDate;
      private final String phoneNumber;
      private final String role;

      @Builder(access = AccessLevel.PRIVATE)
      private Response(String email, String name, LocalDate birthDate, String phoneNumber,
          String role) {
        this.email = email;
        this.name = name;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.role = role;
      }

      public static Response convert(Member member) {
        return Response.builder()
            .name(member.getName())
            .birthDate(member.getBirthDate())
            .email(Email.toString(member.getEmail()))
            .phoneNumber(PhoneNumber.toString(member.getPhoneNumber()))
            .role(member.getRole().name())
            .build();
      }
    }
  }
}
