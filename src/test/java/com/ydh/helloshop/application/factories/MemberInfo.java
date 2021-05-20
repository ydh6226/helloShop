package com.ydh.helloshop.application.factories;

import com.ydh.helloshop.application.domain.Address;
import com.ydh.helloshop.application.domain.member.MemberStatus;
import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;

@Data
public class MemberInfo {

    private String name;
    private String email;
    private String password;
    private MemberStatus memberStatus;
    private Address address;

    public MemberInfo() {
        name = createRandomString();
        email = createRandomString();
        password = createRandomString();
        memberStatus = MemberStatus.CUSTOMER;
        address = createAddress();
    }

    private String createRandomString() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    private Address createAddress() {
        return new Address(createRandomString(), createRandomString(), createRandomString());
    }
}
