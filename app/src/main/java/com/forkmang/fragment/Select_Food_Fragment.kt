package com.forkmang.fragment

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.forkmang.R
import com.forkmang.activity.Activity_PaymentSummary
import com.forkmang.adapter.CartListingAdapter
import com.forkmang.adapter.FoodList_Adapter
import com.forkmang.data.CartBooking
import com.forkmang.data.Category_ItemList
import com.forkmang.data.Extra_Topping
import com.forkmang.data.RestoData
import com.forkmang.databinding.FragmentOrderfoodLayoutBinding
import com.forkmang.helper.Constant
import com.forkmang.helper.Constant.MOBILE
import com.forkmang.helper.Constant.SUCCESS_CODE
import com.forkmang.helper.Constant.TOKEN_LOGIN
import com.forkmang.helper.StorePrefrence
import com.forkmang.helper.Utils.isNetworkAvailable
import com.forkmang.models.TableList
import com.forkmang.network_call.Api.info
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class Select_Food_Fragment : Fragment() {
    var category_itemLists: ArrayList<Category_ItemList>? = null
    var extra_toppingArrayList: ArrayList<Extra_Topping>? = null
    var cartBookingArrayList: ArrayList<CartBooking>? = null
    var booking_id = "0"
    var selectedId_radiobtn_topping = 0
    private val storePrefrence by lazy { StorePrefrence(requireContext()) }
    var progressBar_alertview: ProgressBar? = null
    var all_orderFood_adapter: FoodList_Adapter? = null

    private var _binding: FragmentOrderfoodLayoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderfoodLayoutBinding.inflate(inflater, container, false)
        binding.orderFoodRecycleview.layoutManager = LinearLayoutManager(context)
        return binding.root
    }

    fun callApi_fooditem(category_id: String?) {
        //Toast.makeText(getContext(),"CategoryID->"+category_id,Toast.LENGTH_SHORT).show();
        binding.progressBar.visibility = View.VISIBLE
        info.getres_catitemlist(category_id)!!.enqueue(object : Callback<JsonObject?> {
            override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                try {
                    //Log.d("Result", jsonObject.toString());
                    if (response.code() == Constant.SUCCESS_CODE_n) {
                        val jsonObject = JSONObject(Gson().toJson(response.body()))
                        if (jsonObject.getString("status")
                                .equals(SUCCESS_CODE, ignoreCase = true)
                        ) {
                            category_itemLists = ArrayList()
                            val mjson_arr = jsonObject.getJSONArray("data")
                            for (i in 0 until mjson_arr.length()) {
                                val category_itemList = Category_ItemList()
                                val mjson_obj = mjson_arr.getJSONObject(i)
                                category_itemList.id = mjson_obj.getString("id")
                                category_itemList.category_id = mjson_obj.getString("category_id")
                                category_itemList.name = mjson_obj.getString("name")
                                category_itemList.price = mjson_obj.getString("price")
                                category_itemList.image = mjson_obj.getString("image")
                                val mjson_arr_extra = mjson_obj.getJSONArray("extra")
                                extra_toppingArrayList = ArrayList()
                                for (j in 0 until mjson_arr_extra.length()) {
                                    val mjson_obj_extra = mjson_arr_extra.getJSONObject(j)
                                    val extra_topping = Extra_Topping()
                                    extra_topping.id = mjson_obj_extra.getString("id")
                                    extra_topping.item_id = mjson_obj_extra.getString("item_id")
                                    extra_topping.name = mjson_obj_extra.getString("name")
                                    extra_topping.price = mjson_obj_extra.getString("price")
                                    extra_toppingArrayList?.add(extra_topping)
                                }
                                category_itemList.extra_toppingArrayList = extra_toppingArrayList
                                category_itemLists!!.add(category_itemList)
                            }
                            binding.progressBar.visibility = View.GONE
                            all_orderFood_adapter = FoodList_Adapter(
                                context!!,
                                activity!!,
                                category_itemLists!!,
                                this@Select_Food_Fragment
                            )
                            binding.orderFoodRecycleview.adapter = all_orderFood_adapter


                            /*if(all_orderFood_adapter == null)
                                    {
                                        all_orderFood_adapter = new All_Food_Adapter(getContext(),getActivity(), category_itemLists, Select_Food_Fragment.this);
                                        recyclerView.setAdapter(all_orderFood_adapter);
                                    }
                                    else{
                                        all_orderFood_adapter.notifyDataSetChanged();
                                    }*/
                        }
                    } else if (response.code() == Constant.ERROR_CODE) {
                        val jsonObject = JSONObject(response.errorBody()!!.string())
                        binding.progressBar.visibility = View.GONE
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(context, "Error occur please try again", Toast.LENGTH_LONG)
                        .show()
                }
            }

            override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                Toast.makeText(context, "Error occur please try again", Toast.LENGTH_LONG).show()
                binding.progressBar.visibility = View.GONE
            }
        })
    }

    fun callApi_searchfooditem(category_id: String?, search_item: String?) {
        //Toast.makeText(getContext(),"CategoryID->"+category_id,Toast.LENGTH_SHORT).show();
        binding.progressBar.visibility = View.VISIBLE
        info.getres_catitemlist_search(category_id, search_item)!!
            .enqueue(object : Callback<JsonObject?> {
                override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                    try {
                        //Log.d("Result", jsonObject.toString());
                        if (response.code() == Constant.SUCCESS_CODE_n) {
                            val jsonObject = JSONObject(Gson().toJson(response.body()))
                            if (jsonObject.getString("status")
                                    .equals(SUCCESS_CODE, ignoreCase = true)
                            ) {
                                category_itemLists = ArrayList()
                                val mjson_arr = jsonObject.getJSONArray("data")
                                for (i in 0 until mjson_arr.length()) {
                                    val category_itemList = Category_ItemList()
                                    val mjson_obj = mjson_arr.getJSONObject(i)
                                    category_itemList.id = mjson_obj.getString("id")
                                    category_itemList.category_id =
                                        mjson_obj.getString("category_id")
                                    category_itemList.name = mjson_obj.getString("name")
                                    category_itemList.price = mjson_obj.getString("price")
                                    category_itemList.image = mjson_obj.getString("image")
                                    val mjson_arr_extra = mjson_obj.getJSONArray("extra")
                                    extra_toppingArrayList = ArrayList()
                                    for (j in 0 until mjson_arr_extra.length()) {
                                        val mjson_obj_extra = mjson_arr_extra.getJSONObject(j)
                                        val extra_topping = Extra_Topping()
                                        extra_topping.id = mjson_obj_extra.getString("id")
                                        extra_topping.item_id = mjson_obj_extra.getString("item_id")
                                        extra_topping.name = mjson_obj_extra.getString("name")
                                        extra_topping.price = mjson_obj_extra.getString("price")
                                        extra_toppingArrayList?.add(extra_topping)
                                    }
                                    category_itemList.extra_toppingArrayList =
                                        extra_toppingArrayList
                                    category_itemLists?.add(category_itemList)
                                }
                                binding.progressBar.visibility = View.GONE
                                all_orderFood_adapter = FoodList_Adapter(
                                    requireContext(),
                                    requireActivity(),
                                    category_itemLists!!,
                                    this@Select_Food_Fragment
                                )
                                binding.orderFoodRecycleview.adapter = all_orderFood_adapter
                            }
                        } else if (response.code() == Constant.ERROR_CODE) {
                            val jsonObject = JSONObject(response.errorBody()!!.string())
                            binding.progressBar.visibility = View.GONE
                        }
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(context, "Error occur please try again", Toast.LENGTH_LONG)
                            .show()
                    }
                }

                override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                    Toast.makeText(context, "Error occur please try again", Toast.LENGTH_LONG)
                        .show()
                    binding.progressBar.visibility = View.GONE
                }
            })
    }

    fun callApi_addtocart(
        item_id: String?,
        qty: String?,
        booking_table_id: String?,
        item_extra: String?,
        type: String?
    ) {
        //showProgress();
        binding.progressBar.visibility = View.VISIBLE
        info.additem_cart(
            "Bearer " + storePrefrence.getString(TOKEN_LOGIN),
            item_id,
            qty,
            booking_table_id,
            item_extra,
            storePrefrence.getString(
                Constant.IDENTFIER
            ),
            type
        )
            ?.enqueue(object : Callback<JsonObject?> {
                override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                    try {
                        //Log.d("Result", jsonObject.toString());
                        if (response.code() == Constant.SUCCESS_CODE_n) {
                            val jsonObject = JSONObject(Gson().toJson(response.body()))
                            if (jsonObject.getString("status").equals("200", ignoreCase = true)) {
                                Toast.makeText(
                                    context,
                                    jsonObject.getString("message"),
                                    Toast.LENGTH_LONG
                                ).show()
                                storePrefrence.setString(
                                    Constant.CARTID,
                                    jsonObject.getJSONObject("data").getString("cart_id")
                                )
                                storePrefrence.setString(
                                    Constant.CART_ITEMID,
                                    jsonObject.getJSONObject("data").getString("item_id")
                                )
                                binding.progressBar.visibility = View.GONE
                                cartListingView()
                            }
                        } else if (response.code() == Constant.ERROR_CODE_n || response.code() == Constant.ERROR_CODE) {
                            val jsonObject = JSONObject(response.errorBody()!!.string())
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(
                                context,
                                jsonObject.getString("message"),
                                Toast.LENGTH_LONG
                            ).show()

                            /*JSONObject obj = new JSONObject(loadJSONFromAsset());
                                if(obj.getString("status").equalsIgnoreCase("200"))
                                {
                                    Toast.makeText(getContext(), obj.getString("message")+" offline ", Toast.LENGTH_LONG).show();
                                    storePrefrence.setString("cartid", obj.getJSONObject("data").getString("cart_id"));
                                    storePrefrence.setString("item_id", obj.getJSONObject("data").getString("item_id"));
                                    cartListingView();

                                }*/
                        }
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(context, "Error occur please try again", Toast.LENGTH_LONG)
                            .show()
                    }
                }

                override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                    Toast.makeText(context, "Error occur please try again", Toast.LENGTH_LONG)
                        .show()
                    binding.progressBar.visibility = View.GONE
                    //stopProgress();
                }
            })
    }

    fun callApi_food_1(category_id: String?, booking_id: String) {
        if (isNetworkAvailable(requireContext())) {
            this.booking_id = booking_id
            callApi_fooditem(category_id)
        } else {
            Toast.makeText(context, Constant.NETWORKEROORMSG, Toast.LENGTH_SHORT).show()
        }
    }

    fun showAlertView(category_itemList: Category_ItemList) {
        val alertDialog = AlertDialog.Builder(
            requireActivity()
        )
        val inflater =
            requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dialogView = inflater.inflate(R.layout.addqty_alertview, null)
        alertDialog.setView(dialogView)
        alertDialog.setCancelable(true)
        val dialog = alertDialog.create()
        val radio_btn_id_arr = ArrayList<String>()
        var radioButton4_extra: RadioButton
        var radioButton5_extra: RadioButton
        var radioButton6_extra: RadioButton
        /*radioButton4_extra=dialogView.findViewById(R.id.radioButton4);
        radioButton5_extra=dialogView.findViewById(R.id.radioButton5);
        radioButton6_extra=dialogView.findViewById(R.id.radioButton6);

        ArrayList<Extra_Topping> extra_toppingArrayList_get;
        extra_toppingArrayList_get = category_itemList.getExtra_toppingArrayList();
        for(int i=0;i<extra_toppingArrayList_get.size(); i++)
        {
            Extra_Topping extra_topping = extra_toppingArrayList_get.get(i);
            radioButton4_extra

        }*/
        val extra_toppingArrayList_get: ArrayList<Extra_Topping>? = category_itemList.extra_toppingArrayList
        val rb = extra_toppingArrayList_get?.size?.let { arrayOfNulls<RadioButton>(it) }
        val rg = RadioGroup(context) //create the RadioGroup
        rg.orientation = RadioGroup.VERTICAL //or RadioGroup.VERTICAL
        val layout2 = LinearLayout(context)
        layout2.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layout2.orientation = LinearLayout.VERTICAL
        //rg.setBackgroundColor(Color.parseColor("#3F51B5"));
        val colorStateList = ColorStateList(
            arrayOf(
                intArrayOf(-android.R.attr.state_enabled),
                intArrayOf(android.R.attr.state_enabled)
            ), intArrayOf(
                Color.parseColor("#000000"),  // disabled
                Color.parseColor("#C91107") // enabled
            )
        )
        for (i in extra_toppingArrayList_get?.indices!!) {
            val extra_topping = extra_toppingArrayList_get[i]
            rb?.set(i, RadioButton(context))
            rb?.get(i)?.text = extra_topping.name
            rb?.get(i)?.setTextColor(resources.getColor(R.color.black))
            rb?.get(i)?.buttonTintList = colorStateList
            rb?.get(i)?.id = extra_topping.id?.toInt()!!

            //rg.addView(rb[i]);
            layout2.addView(rb?.get(i)!!)
            rb.get(i)
                ?.setOnCheckedChangeListener { buttonView: CompoundButton, isChecked: Boolean ->
                    if (buttonView.isChecked) {
                        radio_btn_id_arr.add(buttonView.id.toString())
                    }
                }
        }
        val lyt: LinearLayout = dialogView.findViewById(R.id.lyt)
        lyt.addView(layout2) //you add the whole RadioGroup to the layout
        //rg.setOnCheckedChangeListener((group, checkedId) -> selectedId_radiobtn_topping = rg.getCheckedRadioButtonId());
        val btn_add: Button = dialogView.findViewById(R.id.btn_add)
        val btn_reserve: Button = dialogView.findViewById(R.id.btn_reserve)
        val plus_btn: TextView = dialogView.findViewById(R.id.plus_btn)
        val minus1: TextView = dialogView.findViewById(R.id.minus_btn)
        val txt_qty: TextView = dialogView.findViewById(R.id.txt_qty)
        plus_btn.setOnClickListener { v: View? ->
            var value = txt_qty.text.toString().toInt()
            ++value
            txt_qty.text = value.toString()
        }
        minus1.setOnClickListener { v: View? ->
            var value = txt_qty.text.toString().toInt()
            --value
            txt_qty.text = value.toString()
        }
        btn_add.setOnClickListener {
            // dialog.dismiss();
            val item_id = category_itemList.id
            val qty = txt_qty.text.toString()
            //String extra ="1,2";
            var extra = ""
            for (i in radio_btn_id_arr.indices) {
                extra = if (i == 0) {
                    radio_btn_id_arr[i]
                } else {
                    extra + "," + radio_btn_id_arr[i]
                }
            }

            //Log.d("booking_id", booking_id);
            radio_btn_id_arr.clear()
            if (extra.isEmpty()) {
                extra = "1,2" //hardcoded please coreect it
            }
            Log.d("extra", extra)
            Log.d("qty", qty)
            Log.d("item_id", item_id!!)
            Log.d("selectedId", "" + selectedId_radiobtn_topping)

            //api call
            if (isNetworkAvailable(requireContext())) {
                dialog.dismiss()
                callApi_addtocart(
                    item_id,
                    qty,
                    storePrefrence.getString(Constant.BOOKINGID),
                    extra,
                    "book_table"
                )
            } else {
                Toast.makeText(context, Constant.NETWORKEROORMSG, Toast.LENGTH_SHORT).show()
            }
        }
        btn_reserve.setOnClickListener { v: View? -> dialog.dismiss() }
        dialog.show()
    }

    fun cartListingView() {
        val dialog = Dialog(requireActivity(), R.style.FullHeightDialog)
        dialog.setContentView(R.layout.cartview_alertview_2)
        if (dialog.window != null) {
            dialog.window?.setLayout(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }
        val txt_restroname: TextView = dialog.findViewById(R.id.txt_restroname)
        val txt_custname: TextView = dialog.findViewById(R.id.txt_custname)
        val txt_datetime: TextView = dialog.findViewById(R.id.txt_datetime)
        val txt_phoneno: TextView = dialog.findViewById(R.id.txt_phoneno)
        val etv_noperson: EditText = dialog.findViewById(R.id.etv_noperson)
        val btn_pay_table_food: Button = dialog.findViewById(R.id.btn_pay_table_food)
        val btn_pay_table: Button = dialog.findViewById(R.id.btn_pay_table)
        val img_close: ImageView = dialog.findViewById(R.id.img_close)
        progressBar_alertview = dialog.findViewById(R.id.progressBar_alertview)
        txt_restroname.text = tableList_get?.str_hotel_name
        txt_custname.text = tableList_get?.str_customer_name
        etv_noperson.setText(tableList_get?.number_of_person)
        txt_phoneno.text = storePrefrence.getString(MOBILE)
        txt_datetime.text = tableList_get?.str_time
        img_close.setOnClickListener { dialog.dismiss() }
        binding.orderFoodRecycleview.layoutManager = LinearLayoutManager(activity)
        if (isNetworkAvailable(requireContext())) {
            callApi_cartListview()
        } else {
            Toast.makeText(context, Constant.NETWORKEROORMSG, Toast.LENGTH_SHORT).show()
        }
        btn_pay_table_food.setOnClickListener { v: View? ->
            dialog.dismiss()
            val mainIntent = Intent(context, Activity_PaymentSummary::class.java)
            //Bundle bundle = new Bundle();
            //bundle.putParcelableArrayList("cartbookingarraylist", cartBookingArrayList);
            mainIntent.putExtra("model", tableList_get)
            mainIntent.putExtra("restromodel", restoData)
            mainIntent.putExtra("comingfrom", "SelectFood")
            startActivity(mainIntent)
        }
        btn_pay_table.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    fun loadJSONFromAsset(): String? {
        var json: String? = null
        json = try {
            val `is` = requireActivity().assets.open("local2.json")
            val size = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            String(buffer)// "UTF-8"
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }

    fun loadJSONFromAsset_t(): String? {
        var json: String? = null
        json = try {
            val `is` = requireActivity().assets.open("local4.json")
            val size = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            String(buffer)//"UTF-8"
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }

    fun callApi_cartListview() {
        //showProgress();
        progressBar_alertview?.visibility = View.VISIBLE
        info.getcart_detail(
            "Bearer " + storePrefrence.getString(TOKEN_LOGIN), storePrefrence.getString(
                Constant.IDENTFIER
            )
        )
            ?.enqueue(object : Callback<JsonObject?> {
                override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                    try {
                        //Log.d("Result", jsonObject.toString());
                        if (response.code() == Constant.SUCCESS_CODE_n) {
                            val obj = JSONObject(Gson().toJson(response.body()))
                            if (obj.getString("status").equals("200", ignoreCase = true)) {
                                val data_obj = obj.getJSONObject("data")
                                val cart_array_item = data_obj.getJSONArray("cart_item")
                                cartBookingArrayList = ArrayList()
                                for (i in 0 until cart_array_item.length()) {
                                    val cartBooking = CartBooking()
                                    val cart_detail_obj = cart_array_item.getJSONObject(i)


                                    //data obj
                                    cartBooking.data_userid = data_obj.getString("user_id")
                                    if (data_obj.has("booking_table_id")) {
                                        cartBooking.data_booking_table_id =
                                            data_obj.getString("booking_table_id")
                                    } else {
                                        cartBooking.data_booking_table_id = ""
                                    }
                                    cartBooking.data_total = data_obj.getString("total")

                                    //cart_item obj
                                    cartBooking.cart_item_qty = cart_detail_obj.getString("qty")
                                    cartBooking.cart_item_cartid =
                                        cart_detail_obj.getString("cart_id")
                                    cartBooking.cart_item_id = cart_detail_obj.getString("id")
                                    if (cart_detail_obj.has("item_extra_id")) {
                                        cartBooking.cart_item_extra_id =
                                            cart_detail_obj.getString("item_extra_id")
                                    } else {
                                        cartBooking.cart_item_extra_id = "0"
                                    }
                                    //cart_item_details obj
                                    cartBooking.cart_item_details_category_id =
                                        cart_detail_obj.getJSONObject("cart_item_details")
                                            .getString("category_id")
                                    cartBooking.cart_item_details_name =
                                        cart_detail_obj.getJSONObject("cart_item_details")
                                            .getString("name")
                                    cartBooking.cart_item_details_price =
                                        cart_detail_obj.getJSONObject("cart_item_details")
                                            .getString("price")


                                    //extra_item_details obj
                                    val extra_namelist = ArrayList<String>()
                                    val extra_pricelist = ArrayList<String>()
                                    if (cart_detail_obj.getJSONArray("extra_item_details")
                                            .length() > 0
                                    ) {
                                        for (j in 0 until cart_detail_obj.getJSONArray("extra_item_details")
                                            .length()) {
                                            val extra_item_obj =
                                                cart_detail_obj.getJSONArray("extra_item_details")
                                                    .getJSONObject(j)
                                            if (extra_item_obj.has("name")) {
                                                extra_namelist.add(extra_item_obj.getString("name"))
                                            } else {
                                                extra_namelist.add("")
                                            }
                                            if (extra_item_obj.has("price")) {
                                                extra_pricelist.add(extra_item_obj.getString("price"))
                                            } else {
                                                extra_pricelist.add("")
                                            }
                                            //cartBooking.setExtra_item_details_item_id(extra_item_obj.getString("item_id"));
                                        }
                                    } else {
                                        extra_namelist.add("")
                                        extra_pricelist.add("")
                                    }

                                    //extra name
                                    var str_extraname = ""
                                    for (k in extra_namelist.indices) {
                                        str_extraname = if (k == 0) {
                                            extra_namelist[k]
                                        } else {
                                            str_extraname + "," + extra_namelist[k]
                                        }
                                    }
                                    cartBooking.extra_item_details_name = str_extraname

                                    //extra price
                                    var str_extraprice = ""
                                    for (k in extra_pricelist.indices) {
                                        str_extraprice = if (k == 0) {
                                            extra_pricelist[k]
                                        } else {
                                            str_extraprice + "," + extra_pricelist[k]
                                        }
                                    }
                                    cartBooking.extra_item_details_price = str_extraprice
                                    cartBookingArrayList?.add(cartBooking)
                                }
                                progressBar_alertview?.visibility = View.GONE
                                //call adapter
                                val cartBookingAdapter =
                                    CartListingAdapter(requireContext(), cartBookingArrayList)
                                binding.orderFoodRecycleview.adapter = cartBookingAdapter
                            }
                        } else if (response.code() == Constant.ERROR_CODE_n || response.code() == Constant.ERROR_CODE) {
                            val jsonObject = response.errorBody()?.string()?.let { JSONObject(it) }
                            Toast.makeText(
                                context,
                                jsonObject?.getString("message"),
                                Toast.LENGTH_LONG
                            ).show()
                            progressBar_alertview?.visibility = View.GONE
                            /*JSONObject obj = new JSONObject(loadJSONFromAsset_t());

                                if(obj.getString("status").equalsIgnoreCase("200"))
                                {
                                    JSONObject data_obj = obj.getJSONObject("data");
                                    JSONArray cart_array_item = data_obj.getJSONArray("cart_item");
                                    cartBookingArrayList = new ArrayList<>();
                                    for(int i = 0; i<cart_array_item.length(); i++)
                                    {
                                        CartBooking cartBooking = new CartBooking();
                                        JSONObject cart_detail_obj = cart_array_item.getJSONObject(i);


                                        //data obj
                                        cartBooking.setData_userid(data_obj.getString("user_id"));
                                        cartBooking.setData_booking_table_id(data_obj.getString("booking_table_id"));
                                        cartBooking.setData_total(data_obj.getString("total"));

                                        //cart_item obj
                                        cartBooking.setCart_item_qty(cart_detail_obj.getString("qty"));
                                        cartBooking.setCart_item_cartid(cart_detail_obj.getString("cart_id"));
                                        cartBooking.setCart_item_id(cart_detail_obj.getString("item_id"));
                                        cartBooking.setCart_item_extra_id(cart_detail_obj.getString("item_extra_id"));

                                        //cart_item_details obj
                                        cartBooking.setCart_item_details_category_id(cart_detail_obj.getJSONObject("cart_item_details").getString("category_id"));
                                        cartBooking.setCart_item_details_name(cart_detail_obj.getJSONObject("cart_item_details").getString("name"));
                                        cartBooking.setCart_item_details_price(cart_detail_obj.getJSONObject("cart_item_details").getString("price"));


                                        //extra_item_details obj
                                        ArrayList<String>extra_namelist = new ArrayList<>();
                                        ArrayList<String>extra_pricelist = new ArrayList<>();
                                        for(int j = 0; j<cart_detail_obj.getJSONArray("extra_item_details").length(); j++)
                                        {
                                            JSONObject extra_item_obj = cart_detail_obj.getJSONArray("extra_item_details").getJSONObject(j);

                                            extra_namelist.add(extra_item_obj.getString("name"));
                                            extra_pricelist.add(extra_item_obj.getString("price"));


                                            //cartBooking.setExtra_item_details_item_id(extra_item_obj.getString("item_id"));
                                        }

                                        //extra name
                                        String str_extraname ="";
                                        for(int k =0; k<extra_namelist.size(); k++)
                                        {
                                            if (k == 0)
                                            { str_extraname = extra_namelist.get(k); }
                                            else{  str_extraname = str_extraname+","+extra_namelist.get(k);}
                                        }
                                        cartBooking.setExtra_item_details_name(str_extraname);

                                        //extra price
                                        String str_extraprice ="";
                                        for(int k =0; k<extra_pricelist.size(); k++)
                                        {
                                            if (k == 0)
                                            { str_extraprice = extra_pricelist.get(k); }
                                            else{  str_extraprice = str_extraprice+","+extra_pricelist.get(k);}
                                        }
                                        cartBooking.setExtra_item_details_price(str_extraprice);
                                        cartBookingArrayList.add(cartBooking);

                                   }
                                    progressBar.setVisibility(View.GONE);
                                    //call adapter
                                    CartBookingAdapter cartBookingAdapter = new CartBookingAdapter(getActivity(),cartBookingArrayList);
                                    recycleView.setAdapter(cartBookingAdapter);

                                }*/
                        }
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                        progressBar_alertview?.visibility = View.GONE
                        Toast.makeText(context, "Error occur please try again", Toast.LENGTH_LONG)
                            .show()
                    }
                }

                override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                    progressBar_alertview?.visibility = View.GONE
                    Toast.makeText(context, "Error occur please try again", Toast.LENGTH_LONG)
                        .show()
                    //stopProgress();
                }
            })
    }

    fun callApi_addqty(cart_itemid: String?, qty: String?) {
        //showProgress();
        binding.progressBar.visibility = View.VISIBLE
        info.cart_updateqty(
            "Bearer " + storePrefrence.getString(TOKEN_LOGIN),
            cart_itemid,
            qty,
            storePrefrence.getString(
                Constant.IDENTFIER
            )
        )
            ?.enqueue(object : Callback<JsonObject?> {
                override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                    try {
                        //Log.d("Result", jsonObject.toString());
                        if (response.code() == Constant.SUCCESS_CODE_n) {
                            val obj = JSONObject(Gson().toJson(response.body()))
                            if (obj.getString("status").equals("200", ignoreCase = true)) {
                                Toast.makeText(
                                    context,
                                    obj.getString("message"),
                                    Toast.LENGTH_SHORT
                                ).show()
                                binding.progressBar.visibility = View.GONE
                                if (isNetworkAvailable(requireContext())) {
                                    callApi_cartListview()
                                } else {
                                    Toast.makeText(
                                        context,
                                        Constant.NETWORKEROORMSG,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        } else if (response.code() == Constant.ERROR_CODE_n || response.code() == Constant.ERROR_CODE) {
                            val jsonObject = response.errorBody()?.string()?.let { JSONObject(it) }
                            Toast.makeText(
                                context,
                                jsonObject?.getString("message"),
                                Toast.LENGTH_SHORT
                            ).show()
                            binding.progressBar.visibility = View.GONE
                        }
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(context, "Error occur please try again", Toast.LENGTH_LONG)
                            .show()
                    }
                }

                override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(context, "Error occur please try again", Toast.LENGTH_LONG)
                        .show()
                }
            })
    }

    fun callApi_removeitemcart(cart_itemid: String?) {
        //showProgress();
        binding.progressBar.visibility = View.VISIBLE
        info.cart_removeqty(
            "Bearer " + storePrefrence.getString(TOKEN_LOGIN),
            cart_itemid,
            storePrefrence.getString(
                Constant.IDENTFIER
            )
        )!!
            .enqueue(object : Callback<JsonObject?> {
                override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                    try {
                        //Log.d("Result", jsonObject.toString());
                        if (response.code() == Constant.SUCCESS_CODE_n) {
                            val obj = JSONObject(Gson().toJson(response.body()))
                            if (obj.getString("status").equals("200", ignoreCase = true)) {
                                Toast.makeText(
                                    context,
                                    obj.getString("message"),
                                    Toast.LENGTH_SHORT
                                ).show()
                                binding.progressBar.visibility = View.GONE
                                if (isNetworkAvailable(requireContext())) {
                                    callApi_cartListview()
                                } else {
                                    Toast.makeText(
                                        context,
                                        Constant.NETWORKEROORMSG,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        } else if (response.code() == Constant.ERROR_CODE_n || response.code() == Constant.ERROR_CODE) {
                            val jsonObject = JSONObject(response.errorBody()!!.string())
                            Toast.makeText(
                                context,
                                jsonObject.getString("message"),
                                Toast.LENGTH_SHORT
                            ).show()
                            binding.progressBar.visibility = View.GONE
                        }
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(context, "Error occur please try again", Toast.LENGTH_LONG)
                            .show()
                    }
                }

                override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(context, "Error occur please try again", Toast.LENGTH_LONG)
                        .show()
                }
            })
    }

    companion object {
        var category_id: String? = null
        var tableList_get: TableList? = null
        var restoData: RestoData? = null
        fun newInstance(tableList: TableList?, bookTable: RestoData?): Select_Food_Fragment {
            //category_id = category_id_val;
            //Log.d("idval",category_id);
            tableList_get = tableList
            restoData = bookTable
            return Select_Food_Fragment()
        }
    }
}