package com.example.strip.Services;

import com.example.strip.Models.Request.JoinTripRequest;
import com.example.strip.Models.Request.StopLocationUpdateRequest;
import com.example.strip.Models.Request.TripCreateRequest;
import com.example.strip.Models.Response.DriverInfoResponse;
import com.example.strip.Models.Response.RequestTripResponse;
import com.example.strip.Models.Response.TripActiveResponse;
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
import retrofit2.http.Query;

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
    @GET("api/mobile/trips/getall/active")
    Call<List<TripActiveResponse>> getActiveTrips(
            @Query("page") int page,
            @Query("size") int size
    );
    @PUT("api/mobile/trips/{tripId}/start")
    Call<Void> startTrip(@Path("tripId") String tripId);

    @PUT("api/mobile/trips/trips/{tripId}/complete")
    Call<Void> completeTrip(@Path("tripId") String tripId);
    @GET("api/mobile/trips/{tripId}/requests/getall")
    Call<List<RequestTripResponse>> getAllRequestsByTripId(@Path("tripId") String tripId);
    @PUT("api/mobile/trips/request-trips/{requestTripId}/check-in")
    Call<ResponseBody> checkInRequestTrip(@Path("requestTripId") String requestTripId);
    @PUT("api/mobile/trips/request-trips/{requestTripId}/check-out")
    Call<ResponseBody> checkOutRequestTrip(@Path("requestTripId") String requestTripId);
    @PUT("api/mobile/trips/request-trips/{requestTripId}/accept")
    Call<ResponseBody> acceptRequestTrip(@Path("requestTripId") String requestTripId);
    @PUT("api/mobile/trips/request-trips/{requestTripId}/reject")
    Call<ResponseBody> rejectRequestTrip(@Path("requestTripId") String requestTripId);
    @GET("api/mobile/trips/drivers/{driverId}/info")
    Call<DriverInfoResponse> getDriverInfo(@Path("driverId") String driverId);

    @POST("api/mobile/trips/drivers/{tripId}/feedback")
    Call<ResponseBody> sendFeedbackToDriver(@Path("tripId") String tripId);
}
