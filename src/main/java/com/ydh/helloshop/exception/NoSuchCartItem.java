package com.ydh.helloshop.exception;

public class NoSuchCartItem extends RuntimeException {
    public NoSuchCartItem() {
        super();
    }

    public NoSuchCartItem(String message) {
        super(message);
    }

    public NoSuchCartItem(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchCartItem(Throwable cause) {
        super(cause);
    }

    protected NoSuchCartItem(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
