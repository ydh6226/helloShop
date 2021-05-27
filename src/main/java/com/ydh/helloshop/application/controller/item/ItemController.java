package com.ydh.helloshop.application.controller.item;

import com.ydh.helloshop.application.domain.item.Item;
import com.ydh.helloshop.application.exception.ItemException;
import com.ydh.helloshop.application.repository.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemRepository itemRepository;

    @GetMapping("/items/{itemId}")
    public String itemView(@PathVariable("itemId") Long itemId, Model model) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(ItemException::noSuchItemException);

        model.addAttribute("item", item);
        return "item/view";
    }
}
