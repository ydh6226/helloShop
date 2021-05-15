package com.ydh.helloshop.infra.config;

import com.ydh.helloshop.application.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final MemberService memberService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                    .mvcMatchers("/admin").hasAnyAuthority("ADMIN")
                    .mvcMatchers("/seller").hasAnyAuthority("SELLER")
                    .mvcMatchers("/order").authenticated()
                    .mvcMatchers("/cart").authenticated()
                    .anyRequest()
                        .permitAll()
                    .and()

                .formLogin()
                    .loginPage("/members/login")
                    .usernameParameter("email")
                    .passwordParameter("password")
                    .permitAll()
                    .and()
                .logout()
                    .logoutSuccessUrl("/");
    }
}
