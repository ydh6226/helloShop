package com.ydh.helloshop.controller.item;

import com.ydh.helloshop.domain.Category;
import lombok.Data;

@Data
public class ItemSearch {
    private String categoryName;
    private String itemName;
}
