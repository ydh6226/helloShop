package com.ydh.helloshop.application.repository.item;

import com.ydh.helloshop.application.item.Album;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumRepository extends JpaRepository<Album, Long>, AlbumRepositoryCustom {

}
