package com.ydh.helloshop.application.domain.item;

import com.ydh.helloshop.application.controller.seller.form.AlbumForm;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Entity
@DiscriminatorValue("A")
@NoArgsConstructor
@Getter
public class Album extends Item {

    private String artist;
    private String etc;

    public Album(String artist, String etc) {
        this.artist = artist;
        this.etc = etc;
    }

    public Album(Long sellerId ,String artist, String etc, String name, int price, int stockQuantity) {
        this.artist = artist;
        this.etc = etc;
        super.setBasicInfo(name, price, stockQuantity);
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
