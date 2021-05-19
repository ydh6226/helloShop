package com.ydh.helloshop.application.controller.seller;

import com.ydh.helloshop.application.controller.seller.form.ItemForm;
import com.ydh.helloshop.application.controller.seller.form.ItemFormValidator;
import com.ydh.helloshop.application.domain.member.CurrentMember;
import com.ydh.helloshop.application.domain.member.Member;
import com.ydh.helloshop.application.repository.item.ItemCategoryRepository;
import com.ydh.helloshop.application.service.CartItemService;
import com.ydh.helloshop.application.service.CategoryService;
import com.ydh.helloshop.application.service.ItemService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/seller")
@RequiredArgsConstructor
public class SellerController {

    private final ItemService itemService;

    private final CartItemService cartItemService;

    private final CategoryService categoryService;

    private final ItemCategoryRepository itemCategoryRepository;

    private final ItemFormValidator itemFormValidator;


    private static final String SETTINGS_ITEM_LIST_URL = "/settings/itemList";
    private static final String SETTINGS_ITEM_LIST_VIEW = "seller/settings/itemList";

    private static final String SETTINGS_REGISTER_URL = "/settings/register";
    private static final String SETTINGS_REGISTER_VIEW = "seller/settings/register";

    @InitBinder("itemForm")
    public void itemFormExtraInfoValidator(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(itemFormValidator);
    }

    @GetMapping(SETTINGS_ITEM_LIST_URL)
    public String itemList() {
        return SETTINGS_ITEM_LIST_VIEW;
    }

    @GetMapping(SETTINGS_REGISTER_URL)
    public String registerItem(Model model) {
        model.addAttribute(new ItemForm());
        model.addAttribute("categories", categoryService.findAllGroupedByType());
        return SETTINGS_REGISTER_VIEW;
    }

    @PostMapping(SETTINGS_REGISTER_URL)
    public String registerItem(Model model, @Valid ItemForm itemForm, BindingResult result, @CurrentMember Member member) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.findAllGroupedByType());
            return SETTINGS_REGISTER_VIEW;
        }

        itemService.registerItem(itemForm, member.getId());
        return "redirect:/";
    }

//    @GetMapping("/items")
//    public String createItem(Model model, @CurrentMember Member member) {
//        model.addAttribute("itemList", itemService.findAllBySellerId(3L));
//        return "seller/itemList";
//    }

    @ResponseBody
    @PostMapping("/items/delete")
    public void deleteItem(@RequestBody SimpleItemDto itemDto) {
        List<Long> ids = itemDto.getIds();

        //ItemCategory 삭제
        itemCategoryRepository.deleteItemCategoryByIdInQuery(ids);

        //CartItem 삭제
        cartItemService.deleteByItemIds(ids);

        //Item삭제
        itemService.deleteItemsByIdsQuery(ids);
    }

    @Data
    static class SimpleItemDto {
        List<Long> ids = new ArrayList<>();
    }
}
