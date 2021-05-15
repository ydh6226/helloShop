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

    /**
     * memberStatus default: CUSTOMER
     * password default: randomString
     */
    public Member createMember() {
        Member member = Member.createMember(createMemberParam(MemberStatus.CUSTOMER, getRandomString()));
        memberService.join(member);
        return member;
    }

    public Member createMember(String password) {
        Member member = Member.createMember(createMemberParam(MemberStatus.CUSTOMER, password));
        memberService.join(member);
        return member;
    }

    public Member createMember(MemberStatus memberStatus) {
        Member member = Member.createMember(createMemberParam(memberStatus, getRandomString()));
        memberService.join(member);
        return member;
    }

    private CreateMemberParam createMemberParam(MemberStatus memberStatus, String password) {
        return new CreateMemberParam(getRandomString(), getRandomString(), passwordEncoder.encode(password),
                memberStatus, createAddress());
    }

    private Address createAddress() {
        return new Address(getRandomString(), getRandomString(), getRandomString());
    }

    private String getRandomString() {
        return RandomStringUtils.randomAlphanumeric(10);
    }
}
