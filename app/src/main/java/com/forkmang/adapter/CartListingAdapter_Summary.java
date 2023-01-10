package com.forkmang.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.forkmang.R;
import com.forkmang.activity.Activity_PaymentSummary;
import com.forkmang.data.CartBooking;
import com.forkmang.helper.Constant;
import com.forkmang.helper.StorePrefrence;
import com.forkmang.helper.Utils;

import java.util.ArrayList;
import java.util.Objects;


public class CartListingAdapter_Summary extends RecyclerView.Adapter<CartListingAdapter_Summary.CartProductItemHolder> {
    Context ctx;
    ArrayList<CartBooking> cartBookingArrayList;
    Activity activity;
    StorePrefrence storePrefrence;

    public CartListingAdapter_Summary(Context ctx, ArrayList<CartBooking> cartBookingArrayList) {
        this.ctx = ctx;
        this.cartBookingArrayList = cartBookingArrayList;
        storePrefrence=new StorePrefrence(ctx);
    }

    public CartListingAdapter_Summary(Activity activity) {
        this.activity = activity;
    }
    @NonNull
    @Override
    public CartProductItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_view_cell, null);
        CartProductItemHolder cartProductItemHolder = new CartProductItemHolder(v);
        return cartProductItemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CartProductItemHolder holder, int position) {
        CartBooking cartBooking = cartBookingArrayList.get(position);
        holder.txtproductname.setText(cartBooking.getCart_item_details_name());
        holder.txt_toopings.setText(cartBooking.getExtra_item_details_name());
        holder.txt_qty.setText(cartBooking.getCart_item_qty());
        holder.txt_price.setText(ctx.getResources().getString(R.string.rupee)+cartBooking.getCart_item_details_price());


    }

    @Override
    public int getItemCount(){
       return cartBookingArrayList.size();

    }

    public class CartProductItemHolder extends RecyclerView.ViewHolder {
        TextView txtproductname ,txt_toopings, txt_type, txt_qty, plus_btn, minus_btn,txt_price;
        ImageView img_del, img_edit;
        public CartProductItemHolder(@NonNull View itemView) {
            super(itemView);
            txtproductname = itemView.findViewById(R.id.txtproductname);
            txt_toopings = itemView.findViewById(R.id.txt_toopings);
            txt_type = itemView.findViewById(R.id.txt_type);
            txt_type = itemView.findViewById(R.id.txt_type);
            txt_qty = itemView.findViewById(R.id.txt_qty);
            txt_price = itemView.findViewById(R.id.txt_price);
            plus_btn = itemView.findViewById(R.id.plus_btn);
            minus_btn = itemView.findViewById(R.id.minus_btn);
            img_del = itemView.findViewById(R.id.img_del);
            img_edit = itemView.findViewById(R.id.img_edit);


            minus_btn.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                CartBooking cartBooking = cartBookingArrayList.get(position);
                String cart_item_id = cartBooking.getCart_item_id();

                if (Utils.isNetworkAvailable(ctx)) {
                    int qty_update = Integer.parseInt(txt_qty.getText().toString());
                    --qty_update;
                    txt_qty.setText(String.valueOf(qty_update));

                    ((Activity_PaymentSummary)ctx).callApi_addqty(cart_item_id, String.valueOf(qty_update));

                }
                else{
                    Toast.makeText(ctx, Constant.NETWORKEROORMSG, Toast.LENGTH_SHORT).show();

                }


            });

            plus_btn.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                CartBooking cartBooking = cartBookingArrayList.get(position);
                String cart_item_id = cartBooking.getCart_item_id();

                if (Utils.isNetworkAvailable(ctx)) {
                    int qty_update = Integer.parseInt(txt_qty.getText().toString());
                    ++qty_update;
                    txt_qty.setText(String.valueOf(qty_update));
                    ((Activity_PaymentSummary)ctx).callApi_addqty(cart_item_id, String.valueOf(qty_update));
                }
                else{
                    Toast.makeText(ctx, Constant.NETWORKEROORMSG, Toast.LENGTH_SHORT).show();

                }


            });

            img_del.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                CartBooking cartBooking = cartBookingArrayList.get(position);
                String cart_item_id = cartBooking.getCart_item_id();

                final androidx.appcompat.app.AlertDialog.Builder alertDialog = new androidx.appcompat.app.AlertDialog.Builder(ctx);
                LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View dialogView = inflater.inflate(R.layout.msg_view_4, null);
                alertDialog.setView(dialogView);
                alertDialog.setCancelable(true);
                final androidx.appcompat.app.AlertDialog dialog = alertDialog.create();

                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                TextView tvremove,tvclose,txt_msg;

                tvremove = dialogView.findViewById(R.id.tvcancel);
                tvclose = dialogView.findViewById(R.id.tvclose);
                txt_msg = dialogView.findViewById(R.id.txt_msg);

                tvclose.setText(R.string.cancel);
                tvremove.setText(R.string.remove);
                txt_msg.setText(ctx.getResources().getString(R.string.deleteproductmsg));

                tvremove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        if (Utils.isNetworkAvailable(ctx)) {
                            ((Activity_PaymentSummary)ctx).callApi_removeitemcart(cart_item_id);
                        }
                        else{
                            Toast.makeText(ctx, Constant.NETWORKEROORMSG, Toast.LENGTH_SHORT).show();
                         }
                    }
                });
                tvclose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        //onBackPressed();
                    }
                });
                dialog.show();
            });


        }
    }





    /*public void callApi_addqty(String cart_itemid,String qty)
    {
        //showProgress();
        //progressBar.setVisibility(View.VISIBLE);
        Api.getInfo().cart_updateqty("Bearer "+storePrefrence.getString(TOKEN_LOGIN),cart_itemid, qty).
                enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        try{
                            //Log.d("Result", jsonObject.toString());
                            if(response.code() == Constant.SUCCESS_CODE_n)
                            {
                                JSONObject obj = new JSONObject(new Gson().toJson(response.body()));
                                if(obj.getString("status").equalsIgnoreCase("200"))
                                {
                                    JSONObject data_obj = obj.getJSONObject("data");
                                    //progressBar.setVisibility(View.GONE);


                                }
                            }
                            else if(response.code() == Constant.ERROR_CODE_n || response.code() == Constant.ERROR_CODE)
                            {
                                JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                //progressBar.setVisibility(View.GONE);

                            }
                        }
                        catch (Exception ex)
                        {
                            ex.printStackTrace();
                            //progressBar.setVisibility(View.GONE);
                            Toast.makeText(ctx, "Error occur please try again", Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        //progressBar.setVisibility(View.GONE);
                        Toast.makeText(ctx, "Error occur please try again", Toast.LENGTH_LONG).show();

                    }
                });
    }*/


}
