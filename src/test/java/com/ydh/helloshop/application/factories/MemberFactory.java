package com.ydh.helloshop.application.factories;

import com.ydh.helloshop.application.domain.Address;
import com.ydh.helloshop.application.domain.member.CreateMemberParam;
import com.ydh.helloshop.application.domain.member.Member;
import com.ydh.helloshop.application.domain.member.MemberStatus;
import com.ydh.helloshop.application.service.MemberService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

import static com.ydh.helloshop.application.domain.member.MemberStatus.ADMIN;

@Component
public class MemberFactory {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    MemberService memberService;

    public Member createMember(MemberInfo memberInfo) {
        Member member = Member.createMember(createMemberParam(memberInfo));
        memberService.join(member);
        return member;
    }

    private CreateMemberParam createMemberParam(MemberInfo memberInfo) {
        return new CreateMemberParam(memberInfo.getName(), memberInfo.getEmail(),
                passwordEncoder.encode(memberInfo.getPassword()), memberInfo.getMemberStatus(), memberInfo.getAddress());
    }
}
