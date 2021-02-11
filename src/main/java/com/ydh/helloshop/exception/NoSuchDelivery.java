package com.ydh.helloshop.exception;

public class NoSuchDelivery extends RuntimeException {
    public NoSuchDelivery() {
        super();
    }

    public NoSuchDelivery(String message) {
        super(message);
    }

    public NoSuchDelivery(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchDelivery(Throwable cause) {
        super(cause);
    }

    protected NoSuchDelivery(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
