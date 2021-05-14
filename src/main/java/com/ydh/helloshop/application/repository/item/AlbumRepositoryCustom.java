package com.ydh.helloshop.application.repository.item;

import com.ydh.helloshop.application.controller.item.ItemSearch;
import com.ydh.helloshop.application.item.Album;

import java.util.List;

public interface AlbumRepositoryCustom {
    List<Album> findAlbumWihSearch(ItemSearch itemSearch);
}
