package com.secured.jwt;

import lombok.NoArgsConstructor;

public class UserNameAndPasswordAuthRequest {

    public UserNameAndPasswordAuthRequest() {}

    public UserNameAndPasswordAuthRequest(String userName, CharSequence password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public CharSequence getPassword() {
        return password;
    }

    public void setPassword(CharSequence password) {
        this.password = password;
    }

    private String userName;
    private CharSequence password;
}
