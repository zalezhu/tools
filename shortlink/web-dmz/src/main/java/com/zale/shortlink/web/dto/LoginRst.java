package com.zale.shortlink.web.dto;

/**
 * Created by Zale on 16/8/24.
 */
public class LoginRst {
    private String token;
    private String error;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
