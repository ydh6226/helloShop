package com.ydh.helloshop.application.controller;

import com.ydh.helloshop.application.controller.item.ItemSearch;
import com.ydh.helloshop.application.domain.member.CurrentMember;
import com.ydh.helloshop.application.domain.member.Member;
import com.ydh.helloshop.application.service.CategoryService;
import com.ydh.helloshop.application.service.item.ItemServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ItemServiceImpl itemService;

    @RequestMapping("/")
    public String home(Model model, @CurrentMember Member member) {
        model.addAttribute("member", member);

        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "createTime"));
        model.addAttribute("items", itemService.findItemsWithPaging(pageRequest));
        return "home";
    }
}
