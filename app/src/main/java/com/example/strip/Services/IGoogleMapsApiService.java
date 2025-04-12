package com.example.strip.Services;

import com.example.strip.Models.Response.RouteResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IGoogleMapsApiService {
    @GET("maps/api/directions/json")
    Call<RouteResponse> getRoutes(
            @Query("origin") String origin,
            @Query("destination") String destination,
            @Query("alternatives") boolean alternatives,
            @Query("mode") String mode,
            @Query("key") String apiKey
    );
}
