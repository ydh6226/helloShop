package com.ydh.helloshop.application.controller.item;

import com.ydh.helloshop.application.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/items/{itemId}")
    public String itemView(@PathVariable("itemId") Long itemId, Model model) {
        model.addAttribute("album", itemService.findOne(itemId));
        return "item/view";
    }

    @GetMapping("/items")
    public String itemList(@ModelAttribute("itemSearch") ItemSearch itemSearch, Model model) {
        return "";
    }
}
