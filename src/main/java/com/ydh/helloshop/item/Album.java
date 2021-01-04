package com.ydh.helloshop.item;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("A")
@NoArgsConstructor
@Getter
public class Album extends Item {

    String artist;
    String etc;

    public Album(String artist, String etc, String name, int price, int stockQuantity) {
        this.artist = artist;
        this.etc = etc;
        super.setInfo(name, price, stockQuantity);
    }
}
