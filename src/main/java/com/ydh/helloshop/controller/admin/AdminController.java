package com.ydh.helloshop.controller.admin;

import com.ydh.helloshop.controller.admin.form.CategoryForm;
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
public class AdminController {

    private final CategoryService categoryService;

    @GetMapping("/admin")
    public String adminMenu() {
        return "admin/home";
    }

    @GetMapping("/admin/category")
    public String categoryList(Model model) {
        model.addAttribute("categories", categoryService.findAllByKind());
        model.addAttribute("form", new CategoryForm());
        return "admin/category/categoryList";
    }

    @PostMapping("/admin/category/new")
    public String createCategory(@Valid CategoryForm form, BindingResult result) {
        if (result.hasErrors()) {
            //[미해결] popover에서 name을 공백으로 제출할 때 에러 처리 못했음.
            return "redirect:/admin/category";
        }
        categoryService.create(form);
        return "redirect:/admin/category";
    }
}
