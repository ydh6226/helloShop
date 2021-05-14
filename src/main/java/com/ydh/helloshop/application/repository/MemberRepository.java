package com.ydh.helloshop.application.repository;

import com.ydh.helloshop.application.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByEmail(String email);
}
