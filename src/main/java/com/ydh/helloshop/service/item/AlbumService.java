package com.ydh.helloshop.service.item;

import com.ydh.helloshop.controller.item.AlbumForm;
import com.ydh.helloshop.domain.Category;
import com.ydh.helloshop.domain.ItemCategory;
import com.ydh.helloshop.exception.NoSuchItem;
import com.ydh.helloshop.item.Album;
import com.ydh.helloshop.repository.CategoryRepository;
import com.ydh.helloshop.repository.item.AlbumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.ydh.helloshop.domain.ItemCategory.createItemCategory;
import static com.ydh.helloshop.item.Album.createAlbum;
import static java.util.stream.Collectors.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AlbumService implements ItemService<Album> {

    private final AlbumRepository albumRepository;
    private final CategoryRepository categoryRepository;

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
        return albumRepository.findById(id).orElseThrow(() -> new NoSuchItem("The Item could not be found."));
    }

    @Override
    public List<Album> findAll() {
        return albumRepository.findAll();
    }

    @Transactional
    public void update(Long id, AlbumForm form) {
        Album album = albumRepository.findById(id).orElseThrow(() -> new NoSuchItem("The Item could not be found."));
        album.changeAlbumInfo(form.getArtist(), form.getEtc());
        album.changeInfo(form.getName(), form.getPrice(), form.getStockQuantity());
    }

    @Transactional
    public Long create(AlbumForm form) {
        //조회
        List<Category> categories = categoryRepository.findAllById(form.getCategoryIds());

        //엔티티 생성
        List<ItemCategory> itemCategories = categories.stream().map(ItemCategory::createItemCategory).collect(toList());

        Album album = createAlbum(form, itemCategories);

        //저장
        albumRepository.save(album);

        return album.getId();
    }
}
