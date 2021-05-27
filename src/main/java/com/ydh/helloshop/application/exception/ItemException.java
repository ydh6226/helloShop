package com.ydh.helloshop.application.exception;

import org.springframework.security.access.AccessDeniedException;

public class ItemException {
    public static IllegalArgumentException noSuchItemException() {
        return new IllegalArgumentException("존재하지 않는 상품입니다.");
    }

    public static AccessDeniedException accessDeniedException() {
        return new AccessDeniedException("상품의 판매자가 아닙니다.");
    }
}
