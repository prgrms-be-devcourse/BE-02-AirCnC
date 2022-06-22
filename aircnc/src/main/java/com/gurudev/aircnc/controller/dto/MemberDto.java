package com.gurudev.aircnc.controller.dto;

import static lombok.AccessLevel.PRIVATE;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gurudev.aircnc.domain.member.entity.Email;
import com.gurudev.aircnc.domain.member.entity.Member;
import com.gurudev.aircnc.domain.member.entity.Password;
import com.gurudev.aircnc.domain.member.entity.PhoneNumber;
import com.gurudev.aircnc.domain.member.entity.Role;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class MemberDto {

  @Getter
  public static class MemberRegisterRequest {

    @JsonProperty("member")
    private Request request;

    public Member toEntity() {
      return request.toEntity();
    }

    @Getter
    public static class Request {

      private String email;
      private String password;
      private String name;
      private LocalDate birthDate;
      private String phoneNumber;
      private String role;

      public Member toEntity() {
        return new Member(new Email(email),
            new Password(password),
            name, birthDate, new PhoneNumber(phoneNumber),
            Role.valueOf(role));
      }
    }
  }


  @Getter
  @RequiredArgsConstructor(access = PRIVATE)
  public static class MemberResponse {

    @JsonProperty("member")
    private final Response response;

    public static MemberResponse of(Member member) {
      return new MemberResponse(Response.of(member));
    }

    @Getter
    public static class Response {

      private final String email;
      private final String name;
      private final LocalDate birthDate;
      private final String phoneNumber;
      private final String role;

      @Builder(access = PRIVATE)
      private Response(String email, String name, LocalDate birthDate, String phoneNumber,
          Role role) {
        this.email = email;
        this.name = name;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.role = role.name();
      }

      public static Response of(Member member) {
        return Response.builder()
            .name(member.getName())
            .birthDate(member.getBirthDate())
            .email(Email.toString(member.getEmail()))
            .phoneNumber(PhoneNumber.toString(member.getPhoneNumber()))
            .role(member.getRole())
            .build();
      }
    }
  }
}
