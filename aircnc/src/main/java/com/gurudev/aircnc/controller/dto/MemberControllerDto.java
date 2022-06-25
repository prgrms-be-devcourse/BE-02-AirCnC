package com.gurudev.aircnc.controller.dto;

import static lombok.AccessLevel.PRIVATE;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gurudev.aircnc.domain.member.dto.MemberDto;
import com.gurudev.aircnc.domain.member.entity.Email;
import com.gurudev.aircnc.domain.member.entity.Member;
import com.gurudev.aircnc.domain.member.entity.PhoneNumber;
import com.gurudev.aircnc.domain.member.entity.Role;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class MemberControllerDto {

  @Getter
  public static class MemberRegisterRequest {

    @JsonProperty("member")
    private Request request;

    public MemberDto toDto() {
      return request.toDto();
    }

    @Getter
    public static class Request {

      private String email;
      private String password;
      private String name;
      private LocalDate birthDate;
      private String phoneNumber;
      private String role;

      public MemberDto toDto() {
        return MemberDto.builder()
            .email(email)
            .password(password)
            .name(name)
            .birthDate(birthDate)
            .phoneNumber(phoneNumber)
            .role(role)
            .build();
      }
    }
  }

  @Getter
  public static class LoginRequest {

    @JsonProperty("member")
    private Request request;

    @Getter
    public static class Request {

      private String email;
      private String password;
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

  @Getter
  @RequiredArgsConstructor(access = PRIVATE)
  public static class LoginResponse {

    @JsonProperty("member")
    private final Response response;

    public static LoginResponse of(Member member, String token) {
      return new LoginResponse(Response.of(member, token));
    }

    @Getter
    public static class Response {

      private final String email;
      private final String name;
      private final String role;
      private final String token;

      @Builder(access = PRIVATE)
      private Response(String email, String name, Role role, String token) {
        this.email = email;
        this.name = name;
        this.role = role.name();
        this.token = token;
      }

      public static Response of(Member member, String token) {
        return Response.builder()
            .email(Email.toString(member.getEmail()))
            .name(member.getName())
            .role(member.getRole())
            .token(token)
            .build();
      }
    }
  }
}
