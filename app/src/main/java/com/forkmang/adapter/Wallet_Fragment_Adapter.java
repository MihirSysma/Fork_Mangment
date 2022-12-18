package com.forkmang.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.forkmang.R;
import com.forkmang.activity.Activity_PaymentSummary_PickupFragment;
import com.forkmang.data.Wallet;

import java.util.ArrayList;


public class Wallet_Fragment_Adapter extends RecyclerView.Adapter<Wallet_Fragment_Adapter.WalletItemHolder> {
    Activity activity;
    ArrayList<Wallet> walletArrayList;

    public Wallet_Fragment_Adapter(Activity activity, ArrayList<Wallet> walletArrayList) {
        this.activity = activity;
        this.walletArrayList = walletArrayList;
    }

    public Wallet_Fragment_Adapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public WalletItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.wallet_cell, null);
        WalletItemHolder walletItemHolder = new WalletItemHolder(v);
        return walletItemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull WalletItemHolder holder, int position) {
        if(position%2 == 0)
        {
            holder.relative_view.setBackground(activity.getResources().getDrawable(R.drawable.layout_rectangle_gray_2));
            holder.money_transaction_icon.setImageResource(R.drawable.money_out);
        }
        else{
            holder.relative_view.setBackground(activity.getResources().getDrawable(R.drawable.layout_rectangle_gray_2_1));
            holder.money_transaction_icon.setImageResource(R.drawable.charge);
        }

    }

    @Override
    public int getItemCount(){
       //return bookTable_dataArrayList.size();
        return 10;
    }

    public class WalletItemHolder extends RecyclerView.ViewHolder {
        RelativeLayout relative_view;
        ImageView money_transaction_icon;

        public WalletItemHolder(@NonNull View itemView) {
            super(itemView);
            relative_view = itemView.findViewById(R.id.relative_view);
            money_transaction_icon = itemView.findViewById(R.id.money_transaction_icon);

        }
    }

}
