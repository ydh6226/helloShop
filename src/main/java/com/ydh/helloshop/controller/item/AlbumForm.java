package com.ydh.helloshop.controller.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class AlbumForm {

    String artist;
    String etc;
    String name;
    int price;
    int stockQuantity;
}