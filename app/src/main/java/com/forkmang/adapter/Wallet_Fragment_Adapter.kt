package com.forkmang.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.forkmang.R
import com.forkmang.adapter.Wallet_Fragment_Adapter.WalletItemHolder
import com.forkmang.data.Wallet

class Wallet_Fragment_Adapter : RecyclerView.Adapter<WalletItemHolder> {
    var activity: Activity
    var walletArrayList: ArrayList<Wallet>? = null

    constructor(activity: Activity, walletArrayList: ArrayList<Wallet>?) {
        this.activity = activity
        this.walletArrayList = walletArrayList
    }

    constructor(activity: Activity) {
        this.activity = activity
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WalletItemHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.wallet_cell, null)
        return WalletItemHolder(v)
    }

    override fun onBindViewHolder(holder: WalletItemHolder, position: Int) {
        if (position % 2 == 0) {
            holder.relative_view.background =
                activity.resources.getDrawable(R.drawable.layout_rectangle_gray_2)
            holder.money_transaction_icon.setImageResource(R.drawable.money_out)
        } else {
            holder.relative_view.background =
                activity.resources.getDrawable(R.drawable.layout_rectangle_gray_2_1)
            holder.money_transaction_icon.setImageResource(R.drawable.charge)
        }
    }

    override fun getItemCount(): Int {
        //return bookTable_dataArrayList.size();
        return 10
    }

    inner class WalletItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var relative_view: RelativeLayout
        var money_transaction_icon: ImageView

        init {
            relative_view = itemView.findViewById(R.id.relative_view)
            money_transaction_icon = itemView.findViewById(R.id.money_transaction_icon)
        }
    }
}