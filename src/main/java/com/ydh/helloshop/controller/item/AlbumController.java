package com.ydh.helloshop.controller.item;

import com.ydh.helloshop.service.item.AlbumService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class AlbumController {

    private final AlbumService albumService;

    @GetMapping("/items/new")
    public String createForm(Model model) {
        model.addAttribute("AlbumForm", new AlbumForm());
        return "/items/createItemForm";
    }

    @PostMapping("/items/new")
    public String create(@Valid AlbumForm form, BindingResult result) {
        if (result.hasErrors()){
           return "/items/createItemForm";
        }

        albumService.create(form);

        return "redirect:/";
    }


}
