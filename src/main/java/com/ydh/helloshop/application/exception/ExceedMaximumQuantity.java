package com.ydh.helloshop.application.exception;

public class ExceedMaximumQuantity extends RuntimeException {
    public ExceedMaximumQuantity() {
        super();
    }

    public ExceedMaximumQuantity(String message) {
        super(message);
    }

    public ExceedMaximumQuantity(String message, Throwable cause) {
        super(message, cause);
    }

    public ExceedMaximumQuantity(Throwable cause) {
        super(cause);
    }

    protected ExceedMaximumQuantity(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
