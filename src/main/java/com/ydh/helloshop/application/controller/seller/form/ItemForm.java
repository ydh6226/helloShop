package com.ydh.helloshop.application.controller.seller.form;

import com.ydh.helloshop.application.domain.item.ItemType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemForm {

    // 공통
    @NotNull(message = "대분류를 선택하세요.")
    private ItemType itemType;

    @NotNull(message = "소분류를 선택하세요.")
    private Long categoryId;

    @NotBlank(message = "상품명을 입력하세요")
    private String name;

    @Min(value = 0, message = "가격은 0원 이상이어합니다.")
    private int price;

    @Min(value = 0, message = "재고는 0개 이상이어합니다.")
    private int stockQuantity;

    @NotBlank(message = "상품을 설명해주세요.")
    private String description;

    private MultipartFile representativeImageFile;

    //앨범
    private String artist;
    private String etc;

    //도서
    private String author;
    private String isbn;

    //가구
    private Double length;
    private Double width;
    private Double height;
}
