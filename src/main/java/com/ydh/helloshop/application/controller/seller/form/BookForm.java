package com.ydh.helloshop.application.controller.seller.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class BookForm {

    @NotEmpty(message = "작가를 입력하세요.")
    private String author;

    @NotEmpty(message = "ISBN을 입력하세요.")
    private String isbn;

    @NotEmpty(message = "상품이름을 입력하세요")
    private String name;

    @Min(value = 1, message = "가격은 0원이 될 수 없습니다.")
    private int price;

    @Min(value = 1, message = "수량은 0개가 될 수 없습니다.")
    private int stockQuantity;

    private Long sellerId;

    @Size(min = 1, message = "카테고리를 입력하세요.")
    private List<Long> categoryIds = new ArrayList<>();
}
