package com.ydh.helloshop.application.exception;

public class CannotChangeSellerId extends RuntimeException {
    public CannotChangeSellerId() {
        super();
    }

    public CannotChangeSellerId(String message) {
        super(message);
    }

    public CannotChangeSellerId(String message, Throwable cause) {
        super(message, cause);
    }

    public CannotChangeSellerId(Throwable cause) {
        super(cause);
    }

    protected CannotChangeSellerId(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
