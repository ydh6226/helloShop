package com.ydh.helloshop.controller;

import com.ydh.helloshop.controller.member.MemberForm;
import com.ydh.helloshop.domain.Member;
import com.ydh.helloshop.service.CategoryService;
import com.ydh.helloshop.service.item.ItemService;
import com.ydh.helloshop.service.item.ItemServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

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

        return "/home";
    }
}
