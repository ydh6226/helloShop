package com.ydh.helloshop.application.domain.member;

import com.ydh.helloshop.application.domain.Address;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateMemberParam {

    private String name;
    private String email;
    private String password;
    private MemberStatus memberStatus;
    private Address address;

    public CreateMemberParam(String name, String email, String password, MemberStatus memberStatus, Address address) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.memberStatus = memberStatus;
        this.address = address;
    }
}
