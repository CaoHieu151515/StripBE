package com.example.strip.Models.Request;

import androidx.annotation.Size;

import org.jetbrains.annotations.NotNull;

public class RegisterVM {
    @NotNull
    @Size(min = 1, max = 50)
    private String email;
    @NotNull
    @Size(min = 1, max = 50)
    private String password;
    @NotNull
    @Size(min = 1, max = 50)
    private String login;
    private boolean activated;

    @NotNull
    @Size(min = 1, max = 50)
    private String otp;
    @NotNull
    @Size(min = 1, max = 50)
    private String phone;
    public RegisterVM(@NotNull String email, @NotNull String password, @NotNull String login, boolean activated, @NotNull String otp, @NotNull String phone) {
        this.email = email;
        this.password = password;
        this.login = login;
        this.activated = activated;
        this.otp = otp;
        this.phone = phone;
    }
}
