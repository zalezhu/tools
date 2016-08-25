package com.zale.shortlink.web.dto;

/**
 * Created by Zale on 16/8/24.
 */
public class LoginModel {
    private String userName;
    private String password;
    private String passCode;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassCode() {
        return passCode;
    }

    public void setPassCode(String passCode) {
        this.passCode = passCode;
    }
}
