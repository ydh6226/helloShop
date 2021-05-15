package com.ydh.helloshop.application.controller.seller.item;

import com.ydh.helloshop.application.controller.seller.form.AlbumForm;
import com.ydh.helloshop.application.domain.member.CurrentMember;
import com.ydh.helloshop.application.domain.member.Member;
import com.ydh.helloshop.application.service.CategoryService;
import com.ydh.helloshop.application.service.item.AlbumService;
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

    private final CategoryService categoryService;
    private final AlbumService albumService;

    @GetMapping("/seller/album/new")
    public String createItemForm(Model model, @CurrentMember Member member) {
        AlbumForm albumForm = new AlbumForm();
        albumForm.setSellerId(member.getId());

        model.addAttribute("albumForm", albumForm);
        model.addAttribute("categories", categoryService.findAllByKind());
        return "seller/create/album";
    }

    //memberId 값 유지하기 위해 memberForm 파라미터로 설정
    @PostMapping("/seller/album/new")
    public String createItem(@Valid AlbumForm albumForm, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.findAllByKind());
            return "seller/create/album";
        }

        albumService.create(albumForm);
        return "redirect:/seller/items";
    }

}
