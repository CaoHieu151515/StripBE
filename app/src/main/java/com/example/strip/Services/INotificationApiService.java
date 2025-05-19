package com.example.strip.Services;

import com.example.strip.Models.Request.NotificationRequest;
import com.example.strip.Models.Response.NotificationResponse;
import com.example.strip.Models.Response.UserMoreResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface INotificationApiService {
    @POST("api/notification/create")
    Call<Void> createNotification(@Body NotificationRequest request);
    @GET("api/notification/my")
    Call<List<NotificationResponse>> getMyNotifications();
    @PATCH("api/notification/{id}/read")
    Call<Void> markAsRead(@Path("id") Long id);
}
