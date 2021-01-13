package com.ydh.helloshop.controller;

import com.ydh.helloshop.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/category/new")
    public String createForm(Model model) {
        model.addAttribute("categoryForm", new CategoryForm());
        return "/category/createCategoryForm";
    }

    @PostMapping("/category/new")
    public String create(@Valid CategoryForm form, BindingResult result) {
        if (result.hasErrors()) {
           return "/category/createCategoryForm";
        }
        categoryService.create(form);

        return "redirect:/";
    }
}
