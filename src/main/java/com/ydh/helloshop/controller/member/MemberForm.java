package com.ydh.helloshop.controller.member;

import com.ydh.helloshop.domain.MemberStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberForm {

    @NotEmpty(message = "회원 이름은 필수입니다.")
    private String name;

    @NotEmpty(message = "이메일 등록은 필수입니다.")
    @Email(message = "올바른 이메일을 입력하세요.")
    private String email;

    @Min(value = 5, message = "비밀번호는 다섯 글자 이상이어야 합니다.")
    private String password;

    @NotNull(message = "회원종류 선택은 필수입니다.")
    private MemberStatus status;

    private Long id;

    private String city;
    private String street;
    private String zipcode;
}
