package com.ydh.helloshop.application.controller.seller.item;

import com.ydh.helloshop.application.controller.seller.form.BookForm;
import com.ydh.helloshop.application.domain.Category;
import com.ydh.helloshop.application.domain.item.ItemCategory;
import com.ydh.helloshop.application.domain.member.CurrentMember;
import com.ydh.helloshop.application.domain.member.Member;
import com.ydh.helloshop.application.service.CategoryService;
import com.ydh.helloshop.application.service.item.BookDto;
import com.ydh.helloshop.application.service.item.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final CategoryService categoryService;

    private final BookService albumService;

    @GetMapping("/seller/book/new")
    public String createItemForm(Model model, @CurrentMember Member member) {
        BookForm bookForm = new BookForm();
        bookForm.setSellerId(member.getId());

        model.addAttribute("bookForm", bookForm);
        model.addAttribute("categories", categoryService.findAllByKind());

        return "seller/create/book";
    }

    @PostMapping("/seller/book/new")
    public String createBook(@Valid BookForm bookForm , BindingResult result,
                             Model model, @CurrentMember Member member) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.findAllByKind());

            return "seller/create/book";
        }

        //조회
        List<Category> categories = categoryService.findAllById(bookForm.getCategoryIds());

        //엔티티 생성
        List<ItemCategory> itemCategories = categories.stream()
                .map(ItemCategory::createItemCategory).collect(toList());

        albumService.create(new BookDto(bookForm.getAuthor(), bookForm.getIsbn(),
                bookForm.getName(), bookForm.getPrice(),
                bookForm.getStockQuantity(), member.getId()), itemCategories);
        return "redirect:/seller/items";
    }
}
