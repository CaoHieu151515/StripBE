package com.example.strip.Services;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface IBrainTreeApiService {
    @GET("api/payment/braintree/client-token")
    Call<ResponseBody> getClientToken();

    @FormUrlEncoded
    @POST("api/payment/braintree/checkout")
    Call<ResponseBody> checkout(
            @Field("nonce") String nonce,
            @Field("amount") String amount
    );
}
