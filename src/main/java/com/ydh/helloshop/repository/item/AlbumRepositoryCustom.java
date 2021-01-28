package com.ydh.helloshop.repository.item;

import com.ydh.helloshop.controller.ItemSearch;
import com.ydh.helloshop.item.Album;

import java.util.List;

public interface AlbumRepositoryCustom {
    List<Album> findAlbumWihSearch(ItemSearch itemSearch);
}
