package com.ydh.helloshop.application.domain.item;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("F")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Furniture extends Item {

    private double length;
    private double width;
    private double height;

    public Furniture(double length, double width, double height) {
        this.length = length;
        this.width = width;
        this.height = height;
    }
}