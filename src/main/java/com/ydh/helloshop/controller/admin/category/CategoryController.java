package com.ydh.helloshop.controller.admin.category;

import com.ydh.helloshop.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping(path = "/admin")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/category")
    public String categoryList(Model model) {
        model.addAttribute("categories", categoryService.findAllByKind());
        model.addAttribute("form", new CategoryForm());
        return "/admin/category/categoryList";
    }

    @PostMapping("/category/new")
    public String createCategory(@Valid CategoryForm form, BindingResult result) {
        if (result.hasErrors()) {
            //[미해결] popover에서 name을 공백으로 제출할 때 에러 처리 못했음.
           return "redirect:/admin/category";
        }
        categoryService.create(form);
        return "redirect:/admin/category";
    }
}
