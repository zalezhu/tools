package com.zale.shortlink.exception;

/**
 * Created by zhaoyang on 4/27/16.
 */
public class RequestTransferException extends RuntimeException {
    public RequestTransferException() {
    }

    public RequestTransferException(String message) {
        super(message);
    }
}
