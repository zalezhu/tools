package com.zale.shortlink.exception;

import com.cardsmart.inf.entity.RespEntity;

/**
 * Created by zhaoyang on 4/28/16.
 */
public class ErrorRespException extends RuntimeException {
    private int statusCode;
    private RespEntity respEntity;

    public ErrorRespException(String message, int statusCode, RespEntity respEntity) {
        super(message);
        this.statusCode = statusCode;
        this.respEntity = respEntity;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public RespEntity getRespEntity() {
        return respEntity;
    }

    public void setRespEntity(RespEntity respEntity) {
        this.respEntity = respEntity;
    }
}
