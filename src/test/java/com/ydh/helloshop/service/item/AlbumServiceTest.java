package com.ydh.helloshop.service.item;

import com.ydh.helloshop.controller.item.ItemSearch;
import com.ydh.helloshop.controller.seller.AlbumForm;
import com.ydh.helloshop.exception.noSuchThat.NoSuchItem;
import com.ydh.helloshop.item.Album;
import com.ydh.helloshop.repository.item.AlbumRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class AlbumServiceTest {

    @Autowired
    AlbumService albumService;

    @Autowired
    AlbumRepository albumRepository;

    @Autowired
    EntityManager em;

    @Test
    @DisplayName("앨범 정보 변경")
    public void createAlbum() throws Exception {
        //given
        Album album = new Album("김영한", "", "jpa 정석", 10000, 100);

        //when
        Long albumId = albumService.save(album);
        albumService.update(albumId, new AlbumForm(0L, "김영한", "흠집있음", "jpa 정석", 10000, 100));

        em.flush();
        em.clear();

        //then
        Album findAlbum = albumRepository.findById(albumId).orElseThrow(() -> new NoSuchItem("The Item could not be found."));
        assertThat(findAlbum.getEtc()).isEqualTo("흠집있음");
        assertThrows(NoSuchItem.class, () -> albumRepository.findById(10L).orElseThrow(() -> new NoSuchItem("The Item could not be found.")));
    }

    @Test
    @DisplayName("앨범 삭제")
    public void deleteAlbum() throws Exception {
        //given
        Album album = new Album("김영한", "", "jpa 정석", 10000, 100);
        Long id = albumService.save(album);

        //when
        em.flush();
        em.clear();
        albumRepository.deleteById(id);

        //then
        List<Album> result = albumRepository.findAll();

        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    public void test() throws Exception {
        ItemSearch itemSearch = new ItemSearch();
        itemSearch.setCategoryName("만화");
        itemSearch.setItemName("타입");

        List<Album> albums = albumService.findAlbums(itemSearch);
        for (Album album : albums) {
            System.out.println("album.getName() = " + album.getName());

        }
    }

}