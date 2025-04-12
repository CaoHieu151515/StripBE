package com.example.strip.Models.Request;

import androidx.annotation.Size;

import org.jetbrains.annotations.NotNull;

public class LoginVM {
    @NotNull
    @Size(min = 1, max = 50)
    private String username;
    @NotNull
    @Size(min = 1, max = 50)
    private String password;
    private boolean rememberMe;

    public LoginVM(@NotNull String username, @NotNull String password, boolean rememberMe) {
        this.username = username;
        this.password = password;
        this.rememberMe = rememberMe;
    }
}
