package com.example.strip.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;
import com.example.strip.Utils.UnsafeOkHttpClient;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static Retrofit retrofit = null;
    private static final String BASE_URL = "https://stripbe-production.up.railway.app/";

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(UnsafeOkHttpClient.getUnsafeOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getClientWithToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String jwtToken = sharedPreferences.getString("jwtToken", null);

        if (jwtToken == null) {
            Toast.makeText(context, "Bạn chưa đăng nhập!", Toast.LENGTH_LONG).show();
        }

        OkHttpClient client = UnsafeOkHttpClient.getUnsafeOkHttpClient()
                .newBuilder()
                .addInterceptor(chain -> {
                    Request.Builder requestBuilder = chain.request().newBuilder();
                    if (jwtToken != null) {
                        requestBuilder.addHeader("Authorization", "Bearer " + jwtToken);
                    }
                    return chain.proceed(requestBuilder.build());
                })
                .build();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
