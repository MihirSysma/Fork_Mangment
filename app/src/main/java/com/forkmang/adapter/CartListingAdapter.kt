package com.forkmang.adapter

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.forkmang.R
import com.forkmang.data.CartBooking
import com.forkmang.helper.Constant
import com.forkmang.helper.Utils
import com.forkmang.helper.showToastMessage
import java.util.*

class CartListingAdapter : RecyclerView.Adapter<CartListingAdapter.CartProductItemHolder> {
    lateinit var ctx: Context
    var cartBookingArrayList: ArrayList<CartBooking>? = null
    var activity: Activity? = null
    lateinit var onItemClicked: ((func_name: String, cart_item_id: String?, qty_update: String?) -> Unit)

    constructor(
        ctx: Context,
        cartBookingArrayList: ArrayList<CartBooking>?,
        onItemClicked: ((func_name: String, cart_item_id: String?, qty_update: String?) -> Unit)
    ) {
        this.ctx = ctx
        this.cartBookingArrayList = cartBookingArrayList
        this.onItemClicked = onItemClicked
    }

    constructor(activity: Activity?) {
        this.activity = activity
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CartProductItemHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.cart_view_cell, null)
        return CartProductItemHolder(v)
    }

    override fun onBindViewHolder(holder: CartProductItemHolder, position: Int) {
        val cartBooking = cartBookingArrayList!![position]
        holder.txtproductname.text = cartBooking.cart_item_details_name
        holder.txtToopings.text = cartBooking.extra_item_details_name
        holder.txtQty.text = cartBooking.cart_item_qty
        holder.txtPrice.text =
            ctx.resources.getString(R.string.rupee) + cartBooking.cart_item_details_price
    }

    override fun getItemCount() = cartBookingArrayList?.size?:0

    inner class CartProductItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txtproductname: TextView
        var txtToopings: TextView
        var txtType: TextView
        var txtQty: TextView
        var plusBtn: TextView
        var minusBtn: TextView
        var txtPrice: TextView
        var imgDel: ImageView
        var imgEdit: ImageView

        init {
            txtproductname = itemView.findViewById(R.id.txtproductname)
            txtToopings = itemView.findViewById(R.id.txt_toopings)
            txtType = itemView.findViewById(R.id.txt_type)
            txtType = itemView.findViewById(R.id.txt_type)
            txtQty = itemView.findViewById(R.id.txt_qty)
            txtPrice = itemView.findViewById(R.id.txt_price)
            plusBtn = itemView.findViewById(R.id.plus_btn)
            minusBtn = itemView.findViewById(R.id.minus_btn)
            imgDel = itemView.findViewById(R.id.img_del)
            imgEdit = itemView.findViewById(R.id.img_edit)
            minusBtn.setOnClickListener {
                val position = bindingAdapterPosition
                val cartBooking = cartBookingArrayList!![position]
                val cartItemId = cartBooking.cart_item_id
                if (Utils.isNetworkAvailable(ctx)) {
                    var qtyUpdate = txtQty.text.toString().toInt()
                    --qtyUpdate
                    txtQty.text = qtyUpdate.toString()
                    onItemClicked(ADD_QTY, cartItemId, qtyUpdate.toString())
                } else {
                    ctx.showToastMessage(Constant.NETWORKEROORMSG)
                }
            }
            plusBtn.setOnClickListener {
                val position = bindingAdapterPosition
                val cartBooking = cartBookingArrayList!![position]
                val cartItemId = cartBooking.cart_item_id
                if (Utils.isNetworkAvailable(ctx)) {
                    var qtyUpdate = txtQty.text.toString().toInt()
                    ++qtyUpdate
                    txtQty.text = qtyUpdate.toString()
                    onItemClicked(ADD_QTY, cartItemId, qtyUpdate.toString())
                } else {
                    ctx.showToastMessage(Constant.NETWORKEROORMSG)
                }
            }
            imgDel.setOnClickListener {
                val position = bindingAdapterPosition
                val cartBooking = cartBookingArrayList!![position]
                val cartItemId = cartBooking.cart_item_id
                val alertDialog = AlertDialog.Builder(ctx)
                val inflater =
                    ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val dialogView = inflater.inflate(R.layout.msg_view_4, null)
                alertDialog.setView(dialogView)
                alertDialog.setCancelable(true)
                val dialog = alertDialog.create()
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                val tvRemove: TextView = dialogView.findViewById(R.id.tvcancel)
                val tvClose: TextView = dialogView.findViewById(R.id.tvclose)
                val txtMsg: TextView = dialogView.findViewById(R.id.txt_msg)
                tvClose.setText(R.string.cancel)
                tvRemove.setText(R.string.remove)
                txtMsg.text = ctx.resources.getString(R.string.deleteproductmsg)
                tvRemove.setOnClickListener {
                    dialog.dismiss()
                    if (Utils.isNetworkAvailable(ctx)) {
                        onItemClicked(REMOVE_CART_ITEM, cartItemId, null)
                    } else {
                        ctx.showToastMessage(Constant.NETWORKEROORMSG)
                    }
                }
                tvClose.setOnClickListener {
                    dialog.dismiss()
                    //onBackPressed();
                }
                dialog.show()
            }
        }
    } /*public void callApi_addqty(String cart_itemid,String qty)
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
                            ctx.showToastMessage("Error occur please try again", Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        //progressBar.setVisibility(View.GONE);
                        ctx.showToastMessage("Error occur please try again", Toast.LENGTH_LONG).show();

                    }
                });
    }*/
}