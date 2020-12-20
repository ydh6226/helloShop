package com.ydh.helloshop.controller;

import com.ydh.helloshop.domain.MemberStatus;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class MemberForm {

    @NotEmpty(message = "회원 이름은 필수 입니다.")
    private String name;

    @NotEmpty(message = "이메일 등록은 필수 입니다.")
    @Email(message = "올바른 이메일을 입력하세요.")
    private String email;

    @NotNull(message = "회원종류 선택은 필수입니다.")
    private MemberStatus memberStatus;

    private String city;
    private String street;
    private String zipcode;
}
