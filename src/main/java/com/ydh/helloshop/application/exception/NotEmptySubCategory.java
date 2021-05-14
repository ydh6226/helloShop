package com.ydh.helloshop.application.exception;

public class NotEmptySubCategory extends RuntimeException {
    public NotEmptySubCategory() {
        super();
    }

    public NotEmptySubCategory(String message) {
        super(message);
    }

    public NotEmptySubCategory(String message, Throwable cause) {
        super(message, cause);
    }

    public NotEmptySubCategory(Throwable cause) {
        super(cause);
    }

    protected NotEmptySubCategory(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
