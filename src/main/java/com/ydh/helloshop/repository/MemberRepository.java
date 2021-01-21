package com.ydh.helloshop.repository;

import com.ydh.helloshop.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByEmail(String email);
}
