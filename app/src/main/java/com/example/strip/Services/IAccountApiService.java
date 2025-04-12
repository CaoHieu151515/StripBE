package com.example.strip.Services;
import com.example.strip.Models.Request.OtpVM;
import com.example.strip.Models.Request.RegisterVM;
import com.example.strip.Models.Response.ResponseMessage;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IAccountApiService {
    @POST("api/send-otp")
    Call<Void> register(@Body RegisterVM request);
    @POST("api/verify")
    Call<Void> verify(@Body OtpVM request);
}
