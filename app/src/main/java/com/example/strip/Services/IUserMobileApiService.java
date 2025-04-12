package com.example.strip.Services;

import com.example.strip.Models.PackageDriver;
import com.example.strip.Models.Request.ChangePasswordRequest;
import com.example.strip.Models.Request.PassengerProfileRequest;
import com.example.strip.Models.Response.ConfirmDriverResponse;
import com.example.strip.Models.Response.DriverResponse;
import com.example.strip.Models.Response.UserMoreResponse;
import com.example.strip.Models.Response.WalletResponse;


import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
//    @PUT("api/mobile/user/update-profile")
//
//    Call<Void> updateProfile(
//            @Part("firstName") RequestBody firstName,
//            @Part("lastName") RequestBody lastName,
//            @Part("phone") RequestBody phone,
//            @Part("address") RequestBody address,
//            @Part("dob") RequestBody dob,
//            @Part("gender") RequestBody gender,
//            @Part("userImage") MultipartBody.Part userImage
//    );
    @GET("api/mobile/user/confirm-driver")
    Call<DriverResponse> getDriverDetails();
    @GET("api/mobile/user/confirm-driver")
    Call<ConfirmDriverResponse> getConfirmDriver();
    @POST("api/mobile/user/packages/{packageId}/buy")
    Call<Void> buyPackage(@Path("packageId") String packageId);
}
