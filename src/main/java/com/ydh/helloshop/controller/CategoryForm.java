package com.ydh.helloshop.controller;

import lombok.Data;

@Data
public class CategoryForm {
    String name;
    Long parentId;
}
