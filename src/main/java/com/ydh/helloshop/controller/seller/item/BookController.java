package com.ydh.helloshop.controller.seller.item;

import com.ydh.helloshop.controller.member.form.MemberForm;
import com.ydh.helloshop.controller.seller.form.BookForm;
import com.ydh.helloshop.domain.Category;
import com.ydh.helloshop.domain.ItemCategory;
import com.ydh.helloshop.domain.member.Member;
import com.ydh.helloshop.service.CartItemService;
import com.ydh.helloshop.service.CategoryService;
import com.ydh.helloshop.service.item.BookDto;
import com.ydh.helloshop.service.item.BookService;
import com.ydh.helloshop.service.item.ItemServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    private final ItemServiceImpl itemService;

    private final CategoryService categoryService;

    private final BookService albumService;

    private final CartItemService cartItemService;


    @GetMapping("/seller/book/new")
    public String createItemForm(Model model, @AuthenticationPrincipal Member member) {
        BookForm bookForm = new BookForm();
        bookForm.setSellerId(member.getId());

        model.addAttribute("bookForm", bookForm);
        model.addAttribute("categories", categoryService.findAllByKind());

        return "seller/create/book";
    }

    @PostMapping("/seller/book/new")
    public String createBook(@Valid BookForm bookForm , BindingResult result,
                             Model model, @AuthenticationPrincipal Member member) {
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
