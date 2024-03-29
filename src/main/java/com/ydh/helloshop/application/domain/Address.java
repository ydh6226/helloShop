package com.ydh.helloshop.application.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;

import static lombok.AccessLevel.PROTECTED;

@Embeddable
@Access(AccessType.FIELD)
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED) //무분별한 사용을 막기 위해 protected 로 설정
@Getter //값 타입은 변하지 않게 하기위해 setter 빼고 생성자로 값 설정
public class Address {

    private String city;
    private String street;
    private String zipcode;

    @JsonIgnore
    public String getFullAddress() {
        return String.format("%s %s %s", city, street, zipcode);
    }
}
