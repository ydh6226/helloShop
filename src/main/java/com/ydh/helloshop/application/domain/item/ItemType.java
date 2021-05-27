package com.ydh.helloshop.application.domain.item;

public enum ItemType {
    ALBUM(Values.ALBUM),
    BOOK(Values.BOOK),
    FURNITURE(Values.FURNITURE);

    ItemType(String val) {
        if (!this.name().equals(val)) {
            throw new IllegalArgumentException("열거형 변수명과 문자열이 일치하지 않습니다.");
        }
    }

    public static class Values {
        public static final String ALBUM ="ALBUM";
        public static final String BOOK ="BOOK";
        public static final String FURNITURE ="FURNITURE";
    }
}
