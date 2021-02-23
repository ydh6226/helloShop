package com.ydh.helloshop.exception.noSuchThat;

public class NoSuchItem extends RuntimeException {
    public NoSuchItem() {
        super();
    }

    public NoSuchItem(String message) {
        super(message);
    }

    public NoSuchItem(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchItem(Throwable cause) {
        super(cause);
    }

    protected NoSuchItem(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
