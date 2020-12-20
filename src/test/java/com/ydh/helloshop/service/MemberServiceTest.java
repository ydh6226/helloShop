package com.ydh.helloshop.service;

import com.ydh.helloshop.domain.Address;
import com.ydh.helloshop.domain.Member;
import com.ydh.helloshop.domain.MemberStatus;
import com.ydh.helloshop.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional //데이터 변경, 롤백하기 위해
class MemberServiceTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MemberService memberService;


    @Test
    @DisplayName("회원가입")
//    @Rollback(value = false)
    public void signUp() throws Exception {
        //given
        Member member1 = createMember("userA", "abc123@naver.com");
        Member member2 = createMember("userB", "abc456@naver.com");

        //when
        Long id1 = memberService.join(member1);
        Long id2 = memberService.join(member2);

        //then
        assertEquals(id1, member1.getId());
        assertEquals(id2, member2.getId());
    }

    @Test
    @DisplayName("중복 회원 검증")
    public void duplicateSingUp() throws Exception {
        //given
        Member member1 = createMember("userA", "abc123@naver.com");
        Member member2 = createMember("userB", "abc123@naver.com");

        //when
        memberService.join(member1);

        //then
        IllegalStateException exception1 = assertThrows(IllegalStateException.class, () -> memberService.join(member2));
        assertEquals("이미 존재하는 회원입니다.", exception1.getMessage());
    }

    private Member createMember(String userA, String s) {
        Member member1 = new Member();
        member1.createInfo(userA, s, new Address(), MemberStatus.CUSTOMER);
        return member1;
    }
}