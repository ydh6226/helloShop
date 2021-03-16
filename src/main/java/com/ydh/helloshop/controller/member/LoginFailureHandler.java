package com.ydh.helloshop.controller.member;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
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

        if(exception instanceof BadCredentialsException) {
            String errorMessage = "가입하지 않은 아이디이거나, 잘못된 비밀번호입니다.";
            request.setAttribute("errorMessage", errorMessage);
            request.setAttribute("email", request.getParameter("email"));
            request.getRequestDispatcher("members/login").forward(request, response);
        }
    }
}
