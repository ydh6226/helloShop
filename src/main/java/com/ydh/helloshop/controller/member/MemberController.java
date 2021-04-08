package com.ydh.helloshop.controller.member;

import com.ydh.helloshop.controller.member.form.MemberForm;
import com.ydh.helloshop.controller.member.form.MemberLoginForm;
import com.ydh.helloshop.domain.Address;
import com.ydh.helloshop.domain.member.Member;
import com.ydh.helloshop.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    /**
     * control uri 최대한 사용 자제
     */

    @GetMapping("/members/welcome")
    public String welcome() {
        return "members/welcome";
    }

    @GetMapping("/members/login")
    public String loginForm(Model model) {
        model.addAttribute("memberLoginForm", MemberLoginForm.builder().build());
        return "members/login";
    }

    //로그인 실패 시 호출
    @PostMapping("/members/login")
    public String loginForm(Model model, HttpServletRequest request) {
        model.addAttribute("errorMessage", request.getAttribute("errorMessage").toString());
        model.addAttribute("memberLoginForm",
                MemberLoginForm.builder()
                .email(request.getParameter("email"))
                .build());
        return "members/login";
    }

    //사용x
    @GetMapping("/members/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/";
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
