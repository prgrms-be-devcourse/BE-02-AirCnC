package com.gurudev.aircnc.controller;

import static com.gurudev.aircnc.controller.dto.MemberDto.MemberResponse;
import static com.gurudev.aircnc.controller.dto.MemberDto.MemberRegisterRequest;

import com.gurudev.aircnc.domain.member.entity.Member;
import com.gurudev.aircnc.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class MemberController {

  private final MemberService memberService;

  @PostMapping("/members")
  public ResponseEntity<MemberResponse> registerMember(@RequestBody MemberRegisterRequest memberDto) {
    Member registeredMember = memberService.register(memberDto.toEntity());

    return new ResponseEntity<>(MemberResponse.of(registeredMember), HttpStatus.CREATED);
  }

}
