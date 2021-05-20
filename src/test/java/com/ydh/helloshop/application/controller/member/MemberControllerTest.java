package com.ydh.helloshop.application.controller.member;

import com.ydh.helloshop.application.domain.member.Member;
import com.ydh.helloshop.application.factories.MemberFactory;
import com.ydh.helloshop.application.factories.MemberInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@AutoConfigureMockMvc
@Transactional
@SpringBootTest
class MemberControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    MemberFactory memberFactory;

    @Test
    @DisplayName("로그인")
    void login() throws Exception {
        String password = "password";
        MemberInfo memberInfo = new MemberInfo();
        memberInfo.setPassword(password);
        Member member = memberFactory.createMember(memberInfo);

        mockMvc.perform(post("/members/login")
                .param("email", member.getEmail())
                .param("password", password)
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(authenticated().withUsername(member.getEmail()));
    }

    @Test
    @DisplayName("로그인 - 실패")
    void loginWithError() throws Exception {
        String password = "password";
        MemberInfo memberInfo = new MemberInfo();
        memberInfo.setPassword(password);
        Member member = memberFactory.createMember(memberInfo);

        mockMvc.perform(post("/members/login")
                .param("email", member.getEmail())
                .param("password", "wrong password")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/members/login?error"))
                .andExpect(unauthenticated());
    }
}