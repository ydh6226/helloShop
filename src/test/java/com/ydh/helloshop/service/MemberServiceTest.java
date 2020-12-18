package com.ydh.helloshop.service;

import com.ydh.helloshop.domain.Address;
import com.ydh.helloshop.domain.Member;
import com.ydh.helloshop.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    public void signUp() throws Exception {
        //given
        Member member1 = new Member();
        member1.createInfo("userA", "abc123@naver.com", new Address());

        //when
        Long id = memberService.save(member1);

        //then
        assertEquals(id, member1.getId());
    }
}