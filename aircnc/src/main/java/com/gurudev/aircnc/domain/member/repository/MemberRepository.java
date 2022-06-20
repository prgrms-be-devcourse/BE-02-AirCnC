package com.gurudev.aircnc.domain.member.repository;

import com.gurudev.aircnc.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

}
