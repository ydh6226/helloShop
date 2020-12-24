package com.ydh.helloshop.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static javax.persistence.EnumType.*;


/**
 * primary key = id
 * alternate key = email
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
    private MemberStatus memberStatus;

    @Embedded
    private Address address;

    private LocalDateTime joinDate;

    private String auth;

    public void createInfo(String name, String email, String password, Address address, MemberStatus memberStatus) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.address = address;
        this.memberStatus = memberStatus;
        joinDate = LocalDateTime.now();
    }

    //UserDetails overide method
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> roles = new HashSet<>();
        for (String role : auth.split(",")) {
            roles.add(new SimpleGrantedAuthority(role));
        }
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
