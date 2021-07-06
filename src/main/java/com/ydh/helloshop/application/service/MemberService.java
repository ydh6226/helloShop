package com.ydh.helloshop.application.service;

import com.ydh.helloshop.application.domain.member.Member;
import com.ydh.helloshop.application.domain.member.UserMember;
import com.ydh.helloshop.application.exception.noSuchThat.NoSuchMember;
import com.ydh.helloshop.application.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) //조회하는 곳에 readOnly 사용하면 최적화되서 성능 향상, class 레벨에 걸면 메서드에도 걸림
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    //회원 가입
    @Transactional
    public Long join(Member member) {
        validateDuplicateEmail(member.getEmail());
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
    private void validateDuplicateEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if(member.isPresent()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Member> member = memberRepository.findByEmail(email);
        if(member.isEmpty()) {
            throw new UsernameNotFoundException(email);
        }
        return new UserMember(member.get());
    }

    public void login(Long accountId) {
        Member member = memberRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        new UserMember(member);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(new UserMember(member), member.getPassword(),
                List.of(new SimpleGrantedAuthority(member.getStatus().toString())));

        SecurityContextHolder.getContext().setAuthentication(token);
        System.out.println();
    }
}
