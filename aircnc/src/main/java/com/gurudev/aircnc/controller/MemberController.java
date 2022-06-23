package com.gurudev.aircnc.controller;

import static com.gurudev.aircnc.controller.dto.MemberDto.MemberResponse;
import static com.gurudev.aircnc.controller.dto.MemberDto.MemberRegisterRequest;

import com.gurudev.aircnc.configuration.jwt.JwtAuthentication;
import com.gurudev.aircnc.configuration.jwt.JwtAuthenticationToken;
import com.gurudev.aircnc.controller.dto.MemberDto.LoginRequest;
import com.gurudev.aircnc.controller.dto.MemberDto.LoginRequest.Request;
import com.gurudev.aircnc.controller.dto.MemberDto.LoginResponse;
import com.gurudev.aircnc.domain.member.entity.Member;
import com.gurudev.aircnc.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class MemberController {

  private final MemberService memberService;
  private final AuthenticationManager authenticationManager;

  @PostMapping("/members")
  public ResponseEntity<MemberResponse> registerMember(
      @RequestBody MemberRegisterRequest memberDto) {
    Member registeredMember = memberService.register(memberDto.toEntity());

    return new ResponseEntity<>(MemberResponse.of(registeredMember), HttpStatus.CREATED);
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
    Request loginRequest = request.getRequest();

    JwtAuthenticationToken authToken =
        new JwtAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
    Authentication resultToken = authenticationManager.authenticate(authToken);
    JwtAuthenticationToken authenticated = (JwtAuthenticationToken) resultToken;
    JwtAuthentication principal = (JwtAuthentication) authenticated.getPrincipal();
    Member member = (Member) authenticated.getDetails();

    return ResponseEntity.ok(LoginResponse.of(member, principal.token));
  }

}
