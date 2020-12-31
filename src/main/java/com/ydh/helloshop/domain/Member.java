package com.ydh.helloshop.domain;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.EnumType.STRING;


/**
 * primary key = member_id
 * unique key = email
 */
@Entity
@Getter
public class Member implements UserDetails {

    @Id
    @GeneratedValue(generator = "member_id")
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    @Enumerated(STRING)
    private MemberStatus status;

    @Embedded
    private Address address;

    private LocalDateTime joinDate;

    public void createInfo(String name, String email, String password, Address address, MemberStatus memberStatus) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.address = address;
        this.status = memberStatus;
        joinDate = LocalDateTime.now();
    }

    //UserDetails overide method
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> roles = new HashSet<>();
        roles.add(new SimpleGrantedAuthority(status.toString()));
        return roles;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
