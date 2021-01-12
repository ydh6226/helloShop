package com.ydh.helloshop.item;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("A")
@NoArgsConstructor
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

    //setter
    public void changeAlbumInfo(String artist, String etc){
        this.artist = artist;
        this.etc = etc;
    }

    //생성 메서드
    public static Album createAlbum(String artist, String etc, String name, int price, int stockQuantity) {
        return new Album(artist, etc, name, price, stockQuantity);
    }
}
