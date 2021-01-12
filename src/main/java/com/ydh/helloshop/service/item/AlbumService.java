package com.ydh.helloshop.service.item;

import com.ydh.helloshop.controller.item.AlbumForm;
import com.ydh.helloshop.exception.NoSuchItem;
import com.ydh.helloshop.item.Album;
import com.ydh.helloshop.repository.item.AlbumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AlbumService implements ItemService<Album> {

    private final AlbumRepository albumRepository;

    @Override
    @Transactional
    public Long save(Album album) {
        albumRepository.save(album);
        return album.getId();
    }

    @Override
    public void delete(Long id) {
        albumRepository.deleteById(id);
    }

    @Override
    public Album findOne(Long id) {
        return albumRepository.findById(id).orElseThrow(() -> new NoSuchItem("Register the Item!!"));
    }

    @Override
    public List<Album> findAll() {
        return albumRepository.findAll();
    }

    @Transactional
    public void update(Long id, AlbumForm form) {
        Album album = albumRepository.findById(id).orElseThrow(() -> new NoSuchItem("Register the Item!!"));
        album.changeAlbumInfo(form.getArtist(), form.getEtc());
        album.changeInfo(form.getName(), form.getPrice(), form.getStockQuantity());
    }
}
