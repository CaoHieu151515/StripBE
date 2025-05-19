package com.example.strip.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.strip.Activities.Wallet.PaymentActivity;
import com.example.strip.Models.PackageDriver;

import com.example.strip.R;
import com.example.strip.Services.IUserMobileApiService;
import com.example.strip.Utils.ErrorTranslate;
import com.example.strip.Utils.NotificationPopup;
import com.example.strip.Utils.TripStatusTranslate;
import com.example.strip.Utils.UnsafeOkHttpClient;
import com.example.strip.network.ApiClient;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PackageAdapter extends RecyclerView.Adapter<PackageAdapter.ViewHolder> {
    private List<PackageDriver> packageList;
    private Context context;
    private OnPackagePurchaseListener purchaseListener;
    private NotificationPopup notificationPopup;
    public PackageAdapter(Context context, List<PackageDriver> packageList, OnPackagePurchaseListener purchaseListener) {
        this.context = context;
        this.packageList = packageList;
        this.purchaseListener = purchaseListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_package, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PackageDriver pack = packageList.get(position);
        holder.tvName.setText(pack.getName());
        DecimalFormat format = new DecimalFormat("#,###");
        String formattedPrice = format.format(pack.getPrice());
        holder.tvPrice.setText(formattedPrice + " VND");
        holder.tvTime.setText("/" + pack.getTime() + " ngày");
        holder.tvDescription.setText("Miêu tả: " + pack.getDescription());
        holder.tvBonus.setText("Thơi gian thêm: " + pack.getBonus());
        holder.tvStatus.setText("Trạng thái gói: " + TripStatusTranslate.translateStatus(pack.getStatus()));
        holder.btnSubscribe.setOnClickListener(v -> {
            String packageId = pack.getPackageID(); // Assuming you have getId()
            buyPackage(packageId, v);
        });
    }

    @Override
    public int getItemCount() {
        return packageList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice, tvTime, tvStatus, tvDescription, tvBonus;
        Button btnSubscribe;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvPackageName);
            tvPrice = itemView.findViewById(R.id.tvPackagePrice);
            tvTime = itemView.findViewById(R.id.tvPackageTime);
            tvStatus = itemView.findViewById(R.id.tvPackageStatus);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvBonus = itemView.findViewById(R.id.tvBonus);
            btnSubscribe = itemView.findViewById(R.id.btnSubscribe); // 👈 Add this

        }
    }
//    private Retrofit getRetrofitClient() {
//        SharedPreferences sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
//        String jwtToken = sharedPreferences.getString("jwtToken", null);
//
//        if (jwtToken == null) {
//            Toast.makeText(context, "Bạn chưa đăng nhập!", Toast.LENGTH_LONG).show();
//        }
//
//        OkHttpClient client = UnsafeOkHttpClient.getUnsafeOkHttpClient()
//                .newBuilder()
//                .addInterceptor(chain -> {
//                    Request.Builder requestBuilder = chain.request().newBuilder();
//                    if (jwtToken != null) {
//                        requestBuilder.addHeader("Authorization", "Bearer " + jwtToken);
//                    }
//                    return chain.proceed(requestBuilder.build());
//                })
//                .build();
//
//        return new Retrofit.Builder()
//                .baseUrl("http://10.0.2.2:8080/")
//                .client(client)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//    }

    private void buyPackage(String packageId, View view) {
        notificationPopup = new NotificationPopup(context);
        Retrofit retrofit = ApiClient.getClientWithToken(context);

        IUserMobileApiService api = retrofit.create(IUserMobileApiService.class);

        Call<Void> call = api.buyPackage(packageId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    if (purchaseListener != null) {
                        purchaseListener.onPackagePurchased(); // Notify activity
                    }

                } else {
                    Log.e("Failed", "Failed to buy package!" + response.code());
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("Failed", "Mua gói thất bại! " + errorBody);

                        JSONObject jsonObject = new JSONObject(errorBody);
                        String detailMessage = jsonObject.optString("detail", "Lỗi không xác định");
                        // Dịch sang tiếng Việt
                        String translated = ErrorTranslate.translateError(detailMessage);

                        notificationPopup.showPopup("Mua gói thất bại! \n" + translated, true);
                    } catch (Exception e) {
                        e.printStackTrace();
                        notificationPopup.showPopup("Mua gói thất bại! \n Không thể lấy thông báo lỗi", true);
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("API_ERROR", "Error: " + t.getMessage());
                notificationPopup.showPopup("Lỗi: " + t.getMessage(), true);
            }
        });
    }
    public interface OnPackagePurchaseListener {
        void onPackagePurchased();
    }

}
