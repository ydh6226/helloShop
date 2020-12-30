package com.ydh.helloshop.controller;

import com.ydh.helloshop.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @RequestMapping("/")
    public String home(Model model, @AuthenticationPrincipal Member member) {
        if(member != null) {
            model.addAttribute("memberInfo", new memberSimpleInfo(member.getName(), member.getEmail()));
        }
        return "home";
    }

    @AllArgsConstructor
    @Getter
    private static class memberSimpleInfo {
        private final String name;
        private final String email;
    }
}
