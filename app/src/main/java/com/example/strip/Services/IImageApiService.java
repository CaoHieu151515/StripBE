package com.example.strip.Services;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IImageApiService {
    @GET("api/images/user/avatar/{userDetailId}")
    Call<Void> getUserAvatar(@Path("userDetailId") String userDetailId);
}
