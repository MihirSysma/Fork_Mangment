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
import com.forkmang.helper.Constant.NETWORKEROORMSG
import com.forkmang.helper.Utils.isNetworkAvailable
import com.forkmang.helper.showToastMessage
import java.util.*

const val ADD_QTY = "ADD_QTY"
const val REMOVE_CART_ITEM = "REMOVE_CART_ITEM"

class PickupListingAdapter(
    val ctx: Context,
    var cartBookingArrayList: ArrayList<CartBooking>?,
    private var onItemClicked: ((func_name: String, cart_item_id: String?, qty_update: String?) -> Unit)
) :
    RecyclerView.Adapter<PickupListingAdapter.CartProductItemHolder>() {
    var activity: Activity? = null


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
        holder.txt_toopings.text = cartBooking.extra_item_details_name
        holder.txt_qty.text = cartBooking.cart_item_qty
        holder.txt_price.text =
            ctx.resources.getString(R.string.rupee) + cartBooking.cart_item_details_price
    }

    override fun getItemCount() = cartBookingArrayList?.size ?: 0

    inner class CartProductItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txtproductname: TextView
        var txt_toopings: TextView
        var txt_type: TextView
        var txt_qty: TextView
        var plus_btn: TextView
        var minus_btn: TextView
        var txt_price: TextView
        var img_del: ImageView
        var img_edit: ImageView

        init {
            txtproductname = itemView.findViewById(R.id.txtproductname)
            txt_toopings = itemView.findViewById(R.id.txt_toopings)
            txt_type = itemView.findViewById(R.id.txt_type)
            txt_type = itemView.findViewById(R.id.txt_type)
            txt_qty = itemView.findViewById(R.id.txt_qty)
            txt_price = itemView.findViewById(R.id.txt_price)
            plus_btn = itemView.findViewById(R.id.plus_btn)
            minus_btn = itemView.findViewById(R.id.minus_btn)
            img_del = itemView.findViewById(R.id.img_del)
            img_edit = itemView.findViewById(R.id.img_edit)

            minus_btn.setOnClickListener {
                val position = bindingAdapterPosition
                val cartBooking = cartBookingArrayList!![position]
                val cartItemId = cartBooking.cart_item_id
                if (isNetworkAvailable(ctx)) {
                    var qtyUpdate = txt_qty.text.toString().toInt()
                    --qtyUpdate
                    txt_qty.text = qtyUpdate.toString()
                    onItemClicked(ADD_QTY, cartItemId, qtyUpdate.toString())
                } else {
                    ctx.showToastMessage(NETWORKEROORMSG)
                }
            }

            plus_btn.setOnClickListener {
                val position = bindingAdapterPosition
                val cartBooking = cartBookingArrayList!![position]
                val cartItemId = cartBooking.cart_item_id
                if (isNetworkAvailable(ctx)) {
                    var qtyUpdate = txt_qty.text.toString().toInt()
                    ++qtyUpdate
                    txt_qty.text = qtyUpdate.toString()
                    onItemClicked(ADD_QTY, cartItemId, qtyUpdate.toString())
                } else {
                    ctx.showToastMessage(Constant.NETWORKEROORMSG)
                }
            }

            img_del.setOnClickListener {
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
                    if (isNetworkAvailable(ctx)) {
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