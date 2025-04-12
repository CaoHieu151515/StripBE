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

    public RegisterVM(@NotNull String email, @NotNull String password, @NotNull String login, boolean activated, @NotNull String otp) {
        this.email = email;
        this.password = password;
        this.login = login;
        this.activated = activated;
        this.otp = otp;
    }
}
