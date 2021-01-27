package com.ydh.helloshop.controller.seller.item;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
public class AlbumForm {

    @NotEmpty(message = "가수이름을 입력하세요.")
    private String artist;

    private String etc;

    @NotEmpty(message = "상품이름을 입력하세요")
    private String name;

    @Min(value = 1, message = "가격은 0원이 될 수 없습니다.")
    private int price;

    @Min(value = 1, message = "수량은 0개가 될 수 없습니다.")
    private int stockQuantity;

    private Long sellerId;

    @Size(min = 1, message = "카테고리를 입력하세요.")
    private List<Long> categoryIds = new ArrayList<>();

    public AlbumForm(Long sellerId, String artist, String etc, String name, int price, int stockQuantity) {
        this.sellerId = sellerId;
        this.artist = artist;
        this.etc = etc;
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }
}