package com.gurudev.aircnc.domain.trip.entity;

/**
 * TripStatus 의 흐름
 * <li> 예약 -> 여행 중 -> 완료</li>
 * <li> 예약 -> 취소</li>
 */
public enum TripStatus {

  /**
   * 예약
   * <li> 여행 생성시 default 값 </li>
   * <li> 여행 중 상태와 취소 상태가 될 수 있다</li>
   */
  RESERVED,

  /**
   * 여행 중
   * <li> 예약 상태의 여행이 여행의 체크인 날짜가 지나면 여행 중 상태가 된다 </li>
   */
  TRAVELLING,

  /**
   * 완료
   * <li> 여행 중 상태에서 체크아웃 날짜가 지나면 완료 상태가 된다</li>
   */
  DONE,

  /**
   * 취소
   * <li> 예약 상태가 게스트의 취소 요청에 따라 취소 상태가 된다 </li>
   */
  CANCELLED
}
