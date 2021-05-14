package com.ydh.helloshop.application.controller.item;

import com.ydh.helloshop.application.domain.Category;
import lombok.Data;

@Data
public class ItemSearch {
    private String categoryName;
    private String itemName;
}
