package com.gurudev.aircnc.controller;

import static com.gurudev.aircnc.controller.dto.MemberDto.MemberResponse;
import static com.gurudev.aircnc.controller.dto.MemberDto.RegistMemberDto;

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
  public ResponseEntity<MemberResponse> registMember(@RequestBody RegistMemberDto memberDto) {
    final Member registedMember = memberService.register(memberDto.convert());

    return new ResponseEntity<>(MemberResponse.convert(registedMember), HttpStatus.CREATED);
  }

}
