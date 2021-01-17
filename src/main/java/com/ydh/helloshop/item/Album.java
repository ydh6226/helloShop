package com.ydh.helloshop.item;

import com.ydh.helloshop.controller.item.AlbumForm;
import com.ydh.helloshop.domain.Category;
import com.ydh.helloshop.domain.ItemCategory;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.List;

import static lombok.AccessLevel.*;

@Entity
@DiscriminatorValue("A")
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Album extends Item {

    String artist;
    String etc;

    //생성자
    public Album(String artist, String etc, String name, int price, int stockQuantity) {
        this.artist = artist;
        this.etc = etc;
        super.changeInfo(name, price, stockQuantity);
    }

    public Album(Long sellerId ,String artist, String etc, String name, int price, int stockQuantity) {
        super(sellerId);
        this.artist = artist;
        this.etc = etc;
        super.changeInfo(name, price, stockQuantity);
    }

    //setter
    public void changeAlbumInfo(String artist, String etc){
        this.artist = artist;
        this.etc = etc;
    }

    //생성 메서드
    public static Album createAlbum(AlbumForm form, List<ItemCategory> itemCategories) {
        Album album = new Album(form.getSellerId(), form.getArtist(),
                form.getEtc(), form.getName(), form.getPrice(), form.getStockQuantity());
        itemCategories.forEach(album::addItemCategory);
        return album;
    }
}
