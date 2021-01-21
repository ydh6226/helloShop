package com.ydh.helloshop.controller.admin.category;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * categoryList.html 에서
 * <input type="hidden" name="parentId" th:value="${category.parent.id}">
 *  변수 명 변경시 위에 input:name 직접 바꿔야함.
 */
@Data
public class CategoryForm {

    @NotEmpty(message = "카테고리명을 입력하세요.")
    String name;
    Long parentId;
}
