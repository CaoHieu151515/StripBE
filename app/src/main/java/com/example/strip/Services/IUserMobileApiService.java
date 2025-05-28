package com.example.strip.Services;

import com.example.strip.Models.PackageDriver;
import com.example.strip.Models.Request.AddVehicleRequest;
import com.example.strip.Models.Request.ChangePasswordRequest;
import com.example.strip.Models.Request.ConfirmDriverRequest;
import com.example.strip.Models.Request.PassengerProfileRequest;
import com.example.strip.Models.Request.WithDrawRequest;
import com.example.strip.Models.Response.ConfirmDriverResponse;
import com.example.strip.Models.Response.DriverResponse;
import com.example.strip.Models.Response.TripBookingResponse;
import com.example.strip.Models.Response.TripDoneResponse;
import com.example.strip.Models.Response.UserMoreResponse;
import com.example.strip.Models.Response.WalletResponse;


import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface IUserMobileApiService {
    @GET("api/mobile/user/getme")
    Call<UserMoreResponse> getUserInfo();
    @GET("api/mobile/user/wallet/full")
    Call<WalletResponse> getWalletDetails();
    @POST("api/mobile/user/change-password")
    Call<Void> changePassword(@Body ChangePasswordRequest request);
    @GET("api/mobile/user/view/allpackages")
    Call<List<PackageDriver>> getAllPackages();
    @PUT("api/mobile/user/update-profile")
    Call<Void> updateProfile(@Body PassengerProfileRequest passengerProfileRequest);
    @POST("api/mobile/user/confirm-driver")
    Call<ResponseBody> confirmDriver(@Body ConfirmDriverRequest confirmDriverRequest);
    @GET("api/mobile/user/confirm-driver")
    Call<DriverResponse> getDriverDetails();
    @GET("api/mobile/user/confirm-driver")
    Call<ConfirmDriverResponse> getConfirmDriver();
    @POST("api/mobile/user/packages/{packageId}/buy")
    Call<Void> buyPackage(@Path("packageId") String packageId);

    @GET("api/mobile/user/trips/booking")
    Call<List<TripBookingResponse>> getBookedTrips();

    @GET("api/mobile/user/trips/history")
    Call<List<TripDoneResponse>> getDoneTrips();
    @POST("api/mobile/user/driver/vehicles/add")
    Call<ResponseBody> addVehicle(@Body AddVehicleRequest addVehicleRequest);

    @POST("api/mobile/user/withdraw/request")
    Call<Void> withdrawMoney(@Body WithDrawRequest withDrawRequest);
}
