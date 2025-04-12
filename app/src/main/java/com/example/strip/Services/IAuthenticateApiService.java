package com.example.strip.Services;

import com.example.strip.Models.Request.LoginVM;
import com.example.strip.Models.Response.ResponseToken;
import com.example.strip.Models.Response.UserResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface IAuthenticateApiService {
    @POST("api/authenticate")
    Call<ResponseToken> login(@Body LoginVM request);
    @GET("api/getme")
    Call<UserResponse> getUserInfo();
}
