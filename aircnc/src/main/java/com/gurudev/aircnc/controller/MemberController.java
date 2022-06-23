package com.gurudev.aircnc.controller;

import static com.gurudev.aircnc.controller.dto.MemberDto.MemberResponse;
import static com.gurudev.aircnc.controller.dto.MemberDto.MemberRegisterRequest;

import com.gurudev.aircnc.configuration.jwt.JwtAuthentication;
import com.gurudev.aircnc.configuration.jwt.JwtAuthenticationToken;
import com.gurudev.aircnc.controller.dto.MemberDto.MemberLoginRequest;
import com.gurudev.aircnc.controller.dto.MemberDto.MemberLoginRequest.Request;
import com.gurudev.aircnc.controller.dto.MemberDto.MemberTokenResponse;
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
  public ResponseEntity<MemberTokenResponse> login(@RequestBody MemberLoginRequest request) {
    final Request loginRequest = request.getRequest();
    System.out.println(loginRequest.getEmail());
    System.out.println(loginRequest.getPassword());

    final JwtAuthenticationToken authToken =
        new JwtAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
    final Authentication resultToken = authenticationManager.authenticate(authToken);
    final JwtAuthenticationToken authenticated = (JwtAuthenticationToken) resultToken;
    final JwtAuthentication principal = (JwtAuthentication) authenticated.getPrincipal();
    final Member member = (Member) authenticated.getDetails();

    return new ResponseEntity<>(MemberTokenResponse.of(member, principal.token), HttpStatus.OK);
  }

}
