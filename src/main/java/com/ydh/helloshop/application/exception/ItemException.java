package com.ydh.helloshop.application.exception;

import org.springframework.security.access.AccessDeniedException;

public class ItemException {
    public static IllegalArgumentException noSuchItemException() {
        return new IllegalArgumentException("존재하지 않는 상품입니다.");
    }

    public static AccessDeniedException accessDeniedException() {
        return new AccessDeniedException("상품의 판매자가 아닙니다.");
    }

    public static IllegalStateException illegalStateException() {
        return new IllegalStateException("잔여수량이 0인경우 판매 상태로 변경할 수 없습니다.");
    }
}
