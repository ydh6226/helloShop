package com.ydh.helloshop.controller.seller;

import com.ydh.helloshop.controller.HomeController;
import com.ydh.helloshop.controller.member.MemberForm;
import com.ydh.helloshop.domain.Member;
import com.ydh.helloshop.repository.ItemCategoryRepository;
import com.ydh.helloshop.repository.item.ItemRepository;
import com.ydh.helloshop.service.item.ItemServiceImpl;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.*;

@Controller
@AllArgsConstructor
public class SellerController {

    ItemServiceImpl itemService;

    //Repository 직접 사용
    ItemCategoryRepository itemCategoryRepository;

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


    @ResponseBody
    @PostMapping("/seller/item/delete")
    public void deleteItem(@RequestBody SimpleItemDto itemDto) {
        List<Long> ids = itemDto.getIds();
        itemCategoryRepository.deleteItemCategoryByIdInQuery(ids);
        itemService.deleteItemsByIdsQuery(ids);
    }

    @Data
    static class SimpleItemDto {
        List<Long> ids = new ArrayList<>();
    }
}
