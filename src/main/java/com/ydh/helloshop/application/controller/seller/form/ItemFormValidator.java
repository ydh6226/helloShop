package com.ydh.helloshop.application.controller.seller.form;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class ItemFormValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return ItemForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ItemForm itemForm = (ItemForm) target;
        if (itemForm.getRepresentativeImageFile().getSize() <= 0) {
            errors.rejectValue("representativeImageFile", "invalid.representativeImageFile", "대표이미지를 설정하세요");
        }

        if (itemForm.getItemType() == null) {
            return;
        }

        switch (itemForm.getItemType()) {
            case ALBUM:
                ValidationUtils.rejectIfEmptyOrWhitespace(errors, "artist", "invalid.artist", "가수명을 입력하세요.");
                ValidationUtils.rejectIfEmptyOrWhitespace(errors, "etc", "invalid.etc", "기타사항을 입력하세요.");
                break;
            case BOOK:
                ValidationUtils.rejectIfEmptyOrWhitespace(errors, "author", "invalid.author", "작가를 입력하세요.");
                ValidationUtils.rejectIfEmptyOrWhitespace(errors, "isbn", "invalid.isbn", "ISBN를 입력하세요.");
                break;
            case FURNITURE:
                Double length = itemForm.getLength();
                Double width = itemForm.getWidth();
                Double height = itemForm.getHeight();
                if (length == null || length <= 0) {
                    errors.rejectValue("length", "invalid.length", "길이는 0보다 커야합니다.");
                }
                if (width == null || width <= 0) {
                    errors.rejectValue("width", "invalid.width", "길이는 0보다 커야합니다.");
                }
                if (height == null || height <= 0) {
                    errors.rejectValue("height", "invalid.height", "길이는 0보다 커야합니다.");
                }
                break;
        }
    }
}
