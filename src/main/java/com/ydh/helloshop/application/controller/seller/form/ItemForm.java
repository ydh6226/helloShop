package com.ydh.helloshop.application.controller.seller.form;

import com.ydh.helloshop.application.domain.item.ItemType;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ItemForm {
    // TODO: 2021-05-19[양동혁] itemType 입력 에러처리 
    
    // 공통
    private ItemType itemType;
    private Long categoryId;
    private String name;
    private int price;
    private int stockQuantity;
    private String description;
    private MultipartFile representativeImage;

    //앨범
    private String artist;
    private String etc;

    //도서
    private String author;
    private String isbn;

    //가구
    private double length;
    private double width;
    private double height;
}
