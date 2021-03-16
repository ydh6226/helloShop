package com.ydh.helloshop.controller.item;

import com.ydh.helloshop.item.Album;
import com.ydh.helloshop.service.item.AlbumService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final AlbumService albumService;

    @GetMapping("/items/{itemId}")
    public String itemView(@PathVariable("itemId") Long itemId, Model model) {
        Album album = albumService.findOne(itemId);
        model.addAttribute("album", album);
        return "itemView";
    }

    @GetMapping("/items")
    public String itemList(@ModelAttribute("itemSearch") ItemSearch itemSearch, Model model) {
        List<Album> albums = albumService.findWithSearch(itemSearch);
        model.addAttribute("albums", albums);
        return "itemSearch";
    }
}
