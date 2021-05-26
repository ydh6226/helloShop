package com.ydh.helloshop.application.repository.item;

import com.ydh.helloshop.application.domain.item.Album;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlbumRepository extends JpaRepository<Album, Long>, AlbumRepositoryCustom {

}
