package com.ydh.helloshop.application.controller.member;

import com.ydh.helloshop.application.controller.member.form.MemberForm;
import com.ydh.helloshop.application.controller.member.form.LoginForm;
import com.ydh.helloshop.application.domain.Address;
import com.ydh.helloshop.application.domain.member.Member;
import com.ydh.helloshop.application.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@Slf4j
@RequiredArgsConstructor
public class  MemberController {

    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(@Valid MemberForm form, BindingResult result){
        if (result.hasErrors()){
            return "members/createMemberForm";
        }

        Member member = new Member();
        member.createInfo(form.getName(), form.getEmail(),
                new BCryptPasswordEncoder().encode(form.getPassword()),
                new Address(form.getCity(), form.getStreet(), form.getZipcode()),
                form.getStatus());
        memberService.join(member);

        return "redirect:/members/welcome";
    }

    @GetMapping("/members/welcome")
    public String welcome() {
        return "members/welcome";
    }

    @GetMapping("/members/login")
    public String loginForm(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "members/login";
    }
    @GetMapping("/members/findId")
    public String findId() {
        return "members/welcome";
    }

    @GetMapping("/members/findPwd")
    public String findPwd() {
        return "members/welcome";
    }

}
