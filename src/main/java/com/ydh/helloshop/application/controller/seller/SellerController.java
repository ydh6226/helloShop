package com.ydh.helloshop.application.controller.seller;

import com.ydh.helloshop.application.controller.member.form.MemberForm;
import com.ydh.helloshop.application.domain.member.Member;
import com.ydh.helloshop.application.repository.item.ItemCategoryRepository;
import com.ydh.helloshop.application.service.CartItemService;
import com.ydh.helloshop.application.service.CategoryService;
import com.ydh.helloshop.application.service.item.AlbumService;
import com.ydh.helloshop.application.service.item.ItemServiceImpl;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class SellerController {

    private final ItemServiceImpl itemService;

    private final CategoryService categoryService;

    private final AlbumService albumService;

    private final CartItemService cartItemService;

    //Repository 직접 사용
    private final ItemCategoryRepository itemCategoryRepository;

    @GetMapping("/seller")
    public String sellerMenu() {
        return "seller/home";
    }

    @GetMapping("/seller/items")
    public String createItem(Model model, @AuthenticationPrincipal Member member) {
        if(member != null) {
            model.addAttribute("memberInfo",
                    MemberForm.builder()
                            .name(member.getName())
                            .id(member.getId())
                            .email(member.getEmail()).build());
            model.addAttribute("itemList", itemService.findAllBySellerId(member.getId()));
        }
        else{
            model.addAttribute("itemList", itemService.findAllBySellerId(3L));
        }

        return "seller/itemList";
    }

    @ResponseBody
    @PostMapping("/seller/items/delete")
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
