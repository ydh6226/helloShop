package com.ydh.helloshop.application.service.item;

import com.ydh.helloshop.application.controller.item.ItemSearch;
import com.ydh.helloshop.application.controller.seller.form.AlbumForm;
import com.ydh.helloshop.application.domain.Category;
import com.ydh.helloshop.application.domain.item.Album;
import com.ydh.helloshop.application.domain.item.ItemCategory;
import com.ydh.helloshop.application.exception.noSuchThat.NoSuchItem;
import com.ydh.helloshop.application.repository.CategoryRepository;
import com.ydh.helloshop.application.repository.item.AlbumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ydh.helloshop.application.domain.item.Album.createAlbum;
import static java.util.stream.Collectors.toList;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AlbumService implements ItemService<Album> {

    private final AlbumRepository albumRepository;
    private final CategoryRepository categoryRepository;

    private NoSuchItem exception() {
        return new NoSuchItem("The Item could not be found.");
    }

    @Override
    @Transactional
    public Long save(Album album) {
        return albumRepository.save(album).getId();
    }

    @Override
    public void delete(Long id) {
        albumRepository.deleteById(id);
    }

    @Override
    public Album findOne(Long id) {
        return albumRepository.findById(id).orElseThrow(this::exception);
    }

    @Override
    public List<Album> findAll() {
        return albumRepository.findAll();
    }

    public List<Album> findWithSearch(ItemSearch itemSearch) {
        return albumRepository.findAlbumWihSearch(itemSearch);
    }

    @Transactional
    public void update(Long id, AlbumForm form) {
        Album album = albumRepository.findById(id).orElseThrow(this::exception);
        album.changeAlbumInfo(form.getArtist(), form.getEtc());
        album.changeInfo(form.getName(), form.getPrice(), form.getStockQuantity());
    }

    @Transactional
    public Long create(AlbumForm form) {
        //조회
        List<Category> categories = categoryRepository.findAllById(form.getCategoryIds());

        //엔티티 생성
        List<ItemCategory> itemCategories = categories.stream()
                .map(ItemCategory::createItemCategory).collect(toList());

        Album album = createAlbum(form, itemCategories);

        //저장
        albumRepository.save(album);

        return album.getId();
    }
}
