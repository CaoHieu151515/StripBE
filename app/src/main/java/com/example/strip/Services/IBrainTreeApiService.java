package com.example.strip.Services;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

public interface IBrainTreeApiService {
    @GET("api/payment/braintree/client-token")
    Call<ResponseBody> getClientToken();
}
