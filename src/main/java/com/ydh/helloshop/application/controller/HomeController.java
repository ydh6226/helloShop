package com.ydh.helloshop.application.controller;

import com.ydh.helloshop.application.controller.item.ItemSearch;
import com.ydh.helloshop.application.controller.member.form.MemberForm;
import com.ydh.helloshop.application.domain.member.Member;
import com.ydh.helloshop.application.service.CategoryService;
import com.ydh.helloshop.application.service.item.ItemServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ItemServiceImpl itemService;
    private final CategoryService categoryService;

    @RequestMapping("/")
    public String home(Model model, @AuthenticationPrincipal Member member) {
        if(member != null) {
            model.addAttribute("memberInfo",
                    MemberForm.builder()
                            .name(member.getName())
                            .id(member.getId()).build());
        }

        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "createTime"));
        model.addAttribute("items", itemService.findItemsWithPaging(pageRequest));
        model.addAttribute("itemSearch", new ItemSearch());
        model.addAttribute("leafCategories", categoryService.findLeafCategories());

        return "home";
    }
}
