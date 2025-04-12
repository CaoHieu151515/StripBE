package com.example.strip.Services;

import com.example.strip.Models.Request.JoinTripRequest;
import com.example.strip.Models.Request.StopLocationUpdateRequest;
import com.example.strip.Models.Request.TripCreateRequest;
import com.example.strip.Models.Trip;
import com.example.strip.Models.TripDetail;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ITripMobileApiService {
    @GET("api/mobile/trips/getAll/card")
    Call<List<Trip>> getAllTrips();
    @GET("api/mobile/trips/{tripId}/trip/fulldetails")
    Call<TripDetail> getTripDetails(@Path("tripId") String tripId);
    @POST("api/mobile/trips/create")
    Call<TripDetail> createTrip(@Body TripCreateRequest tripCreateRequest);
    @POST("api/mobile/user/trips/join")
    Call<ResponseBody> joinTrip(@Body JoinTripRequest request);
    @PUT("api/mobile/trips/{tripId}/locations/update")
    Call<ResponseBody> updateTripLocations(
            @Path("tripId") String tripId,
            @Body List<StopLocationUpdateRequest> stopLocations
    );
}
