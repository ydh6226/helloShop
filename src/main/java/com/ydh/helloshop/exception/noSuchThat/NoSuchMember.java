package com.ydh.helloshop.exception.noSuchThat;

public class NoSuchMember extends RuntimeException {
    public NoSuchMember() {
    }

    public NoSuchMember(String message) {
        super(message);
    }

    public NoSuchMember(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchMember(Throwable cause) {
        super(cause);
    }

    public NoSuchMember(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
