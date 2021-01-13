package com.ydh.helloshop.repository.item;

import com.ydh.helloshop.item.Album;
import com.ydh.helloshop.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

public interface AlbumRepository extends JpaRepository<Album, Long> {

}
