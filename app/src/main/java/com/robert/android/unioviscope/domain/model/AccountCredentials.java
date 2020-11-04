package com.robert.android.unioviscope.domain.model;

import java.io.Serializable;

public class AccountCredentials implements Serializable {

    private String userName;
    private String password;

    // no-args constructor
    @SuppressWarnings("unused")
    AccountCredentials() {

    }

    public AccountCredentials(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    @SuppressWarnings("unused")
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @SuppressWarnings("unused")
    public String getPassword() {
        return password;
    }

    @SuppressWarnings("unused")
    public void setPassword(String password) {
        this.password = password;
    }
}