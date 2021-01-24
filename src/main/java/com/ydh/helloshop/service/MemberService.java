package com.ydh.helloshop.service;

import com.ydh.helloshop.domain.Member;
import com.ydh.helloshop.exception.NoSuchMember;
import com.ydh.helloshop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) //조회하는 곳에 readOnly 사용하면 최적화되서 성능 향상, class 레벨에 걸면 메서드에도 걸림
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    //회원 가입
    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    //단일 회원 조회
    public Member findOne(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new NoSuchMember("The member could not be found."));
    }

    //모든 회원 조회
    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    //중복 회원 검증(중복된 이메일 사용 불가)
    //[미해결] 예외 처리
    private void validateDuplicateMember(Member member) {
        List<Member> findMember = memberRepository.findByEmail(member.getEmail());
        if(findMember.size() == 1){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    //UserDetailsService overide method
    @Override
    public Member loadUserByUsername(String email) throws UsernameNotFoundException {
        List<Member> findMember = memberRepository.findByEmail(email);
        if(findMember.size() == 1) {
            return findMember.get(0);
        }
        else {
            throw new UsernameNotFoundException(email);
        }
    }



}
