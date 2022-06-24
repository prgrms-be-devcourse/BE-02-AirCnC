package com.gurudev.aircnc.domain.member.entity;

/**
 * 회원의 역할
 * <li> 게스트와 호스트 두가지가 있다.
 * <li> 호스트는 게스트의 권한에 추가로 숙소를 가질 수 있다.
 */
public enum Role {

  /**
   * 게스트의 권한
   * <li> 여행 등록/삭제 </li>
   * <li> 위시리스트에 숙소 등록/삭제 </li>
   * <li> 숙소의 리뷰 등록/삭제 </li>
   */
  GUEST,

  /**
   * 호스트의 권한
   * <li> 숙소 등록/변경/삭제</li>
   */
  HOST
}
