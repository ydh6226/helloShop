package com.ydh.helloshop.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
//[미해결] 테스트작성을 위해 열어 두었음. 테스트코드 다른 방법으로 수행 필요
//@NoArgsConstructor(access = AccessLevel.PROTECTED) //무분별한 사용을 막기 위해 protected 로 설정
@Getter //값 타입은 변하지 않게 하기위해 setter 빼고 생성자로 값 설정
public class Address {

    private String city;
    private String street;
    private String zipcode;
}
