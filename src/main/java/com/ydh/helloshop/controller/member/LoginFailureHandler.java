package com.ydh.helloshop.controller.member;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public class LoginFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        String error;
        if (exception instanceof BadCredentialsException) {
            error = "존재하지 않는 계정이거나, 비밀번호가 일치하지 않습니다.";
        } else {
            error = "로그인 할 수 없습니다. 관리자에게 문의하세요.";
        }
        request.setAttribute("error", error);
        request.getRequestDispatcher("/members/login").forward(request, response);
    }
}
