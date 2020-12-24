package com.ydh.helloshop.controller;

import com.ydh.helloshop.domain.Address;
import com.ydh.helloshop.domain.Member;
import com.ydh.helloshop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class MemberController {

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

        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());

        Member member = new Member();
        member.createInfo(form.getName(), form.getEmail(), form.getPassword(), address, form.getMemberStatus());
        memberService.join(member);
        return "redirect:/members/welcome";
    }

    //control uri 최대한 사용 자제
    @GetMapping("/members/welcome")
    public String welcome() {
        return "members/welcome";
    }

    @GetMapping("members/login")
    public String login() {
        return "members/welcome";
    }

    @GetMapping("members/findId")
    public String findId() {
        return "members/welcome";
    }

    @GetMapping("members/findPwd")
    public String findPwd() {
        return "members/welcome";
    }

}
