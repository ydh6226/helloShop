package com.ydh.helloshop.application.domain.member;

import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

@Getter
public class UserMember extends User {

    private Member member;

    public UserMember(Member member) {
        super(member.getEmail(), member.getPassword(),
                List.of(new SimpleGrantedAuthority(member.getStatus().toString())));
        this.member = member;
    }
}
