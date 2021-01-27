package com.ydh.helloshop.controller.seller;

import com.ydh.helloshop.controller.seller.item.AlbumForm;
import com.ydh.helloshop.controller.member.MemberForm;
import com.ydh.helloshop.domain.Cart;
import com.ydh.helloshop.domain.Member;
import com.ydh.helloshop.repository.CartRepository;
import com.ydh.helloshop.repository.ItemCategoryRepository;
import com.ydh.helloshop.service.CartItemService;
import com.ydh.helloshop.service.CategoryService;
import com.ydh.helloshop.service.item.AlbumService;
import com.ydh.helloshop.service.item.ItemServiceImpl;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class SellerController {

    private final ItemServiceImpl itemService;

    private final CategoryService categoryService;

    private final AlbumService albumService;

    private final CartItemService cartItemService;

    private final CartRepository cartRepository;

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

        return "/seller/itemList";
    }

    @GetMapping("/seller/items/new")
    public String createItemForm(Model model, @AuthenticationPrincipal Member member) {
        model.addAttribute("albumForm", new AlbumForm());
        model.addAttribute("categories", categoryService.findAllByKind());

        if (member != null) {
            model.addAttribute("memberForm",
                    MemberForm.builder()
                            .name(member.getName())
                            .id(member.getId())
                            .build());
        }
        else {
            model.addAttribute("memberForm",
                    MemberForm.builder()
                            .name("hong")
                            .id(3L).build());
        }

        return "/seller/createItem";
    }

    //memberId 값 유지하기 위해 memberForm 파라미터로 설정
    @PostMapping("/seller/items/new")
    public String createItem(@Valid AlbumForm albumForm ,BindingResult result,
                             Model model, @AuthenticationPrincipal Member member) {
        model.addAttribute("categories", categoryService.findAllByKind());
        if (result.hasErrors()) {
            if (member != null) {
                model.addAttribute("memberForm",
                        MemberForm.builder()
                                .name(member.getName())
                                .id(member.getId())
                                .build());
            }
            else {
                model.addAttribute("memberForm",
                        MemberForm.builder()
                                .name("hong")
                                .id(3L).build());
            }
            return "/seller/createItem";
        }

        albumService.create(albumForm);
        return "redirect:/seller/items";
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
