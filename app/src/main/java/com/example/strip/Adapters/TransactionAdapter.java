package com.example.strip.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.strip.Models.Transaction;
import com.example.strip.R;
import com.example.strip.Utils.DateFormatter;
import com.example.strip.Utils.TripStatusTranslate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {
    private List<Transaction> transactionList;

    public TransactionAdapter(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        Transaction transaction = transactionList.get(position);
        holder.tvAmount.setText(String.format("%.2f", transaction.getAmount()));
        holder.tvDate.setText(DateFormatter.formatDate(transaction.getDate()));
        holder.tvWalletType.setText(TripStatusTranslate.translateWalletTypeTransaction(transaction.getWalletType()));
        holder.tvTransStatus.setText(TripStatusTranslate.translateWalletStatusTransaction(transaction.getTransStatus()));
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public static class TransactionViewHolder extends RecyclerView.ViewHolder {
        TextView tvAmount, tvDate, tvWalletType, tvTransStatus;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvWalletType = itemView.findViewById(R.id.tvWalletType);
            tvTransStatus = itemView.findViewById(R.id.tvTransStatus);
        }
    }
}

