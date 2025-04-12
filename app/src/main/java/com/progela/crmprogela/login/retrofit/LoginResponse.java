package com.progela.crmprogela.login.retrofit;

import com.google.gson.annotations.SerializedName;

public class LoginResponse
{
    @SerializedName("token")
    private String Token;

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }
}
