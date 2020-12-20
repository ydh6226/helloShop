package com.ydh.helloshop.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Optional;

import static javax.persistence.EnumType.*;


/**
 * primary key = id
 * alternate key = email
 */
@Entity
@Getter
public class Member {

    @Id
    @GeneratedValue(generator = "member_id")
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    @Enumerated(STRING)
    private MemberStatus memberStatus;

    @Embedded
    private Address address;

    private LocalDateTime joinDate;

    public void createInfo(String name, String email, Address address, MemberStatus memberStatus) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.memberStatus = memberStatus;
        joinDate = LocalDateTime.now();
    }
}
