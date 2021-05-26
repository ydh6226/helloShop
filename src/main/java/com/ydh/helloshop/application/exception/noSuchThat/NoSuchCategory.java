package com.ydh.helloshop.application.exception.noSuchThat;

public class NoSuchCategory extends RuntimeException {
    public NoSuchCategory() {
        super();
    }

    public NoSuchCategory(String message) {
        super(message);
    }

    public NoSuchCategory(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchCategory(Throwable cause) {
        super(cause);
    }

    protected NoSuchCategory(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }


}
