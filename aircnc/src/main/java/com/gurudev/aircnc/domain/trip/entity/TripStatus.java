package com.gurudev.aircnc.domain.trip.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/* 여행 상태 */
@Getter
@RequiredArgsConstructor
public enum TripStatus {

  /* 예약 */
  RESERVED("예약"),

  /* 여행 중 */
  TRAVELLING("여행 중"),

  /* 완료 */
  DONE("완료"),

  /* 취소 */
  CANCELLED("취소");

  private final String status;
}
