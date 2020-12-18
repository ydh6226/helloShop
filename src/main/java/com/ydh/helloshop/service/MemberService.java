package com.ydh.helloshop.service;

import com.ydh.helloshop.domain.Member;
import com.ydh.helloshop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) //조회하는 곳에 readOnly 사용하면 최적화되서 성능 향상, class 레벨에 걸면 메서드에도 걸림
public class MemberService {

    private final MemberRepository memberRepository;

    //회원 가입
    @Transactional
    public Long save(Member member) {
//        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    //단일 회원 조회
    public Member findOne(Long id) {
        return memberRepository.findOne(id);
    }

    //모든 회원 조회
    public List<Member> findAll() {
        return memberRepository.findAll();
    }


    //중복 회원 검증(중복된 이메일 사용 불가)
    private void validateDuplicateMember(Member member) {
        memberRepository.findByEmail(member.getEmail());

    }
}
