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
import com.forkmang.vm.PickUpSelectFoodViewModel
import com.forkmang.R
import com.forkmang.activity.ActivityPaymentSummary
import com.forkmang.adapter.ADD_QTY
import com.forkmang.adapter.PickupFoodListAdapter
import com.forkmang.adapter.PickupListingAdapter
import com.forkmang.adapter.REMOVE_CART_ITEM
import com.forkmang.data.CartBooking
import com.forkmang.data.Category_ItemList
import com.forkmang.data.Extra_Topping
import com.forkmang.data.RestoData
import com.forkmang.databinding.FragmentPickupLayoutBinding
import com.forkmang.helper.Constant
import com.forkmang.helper.Constant.COMMAND_CART_LIST_VIEW
import com.forkmang.helper.Constant.MOBILE
import com.forkmang.helper.Constant.TOKEN_LOGIN
import com.forkmang.helper.StorePrefrence
import com.forkmang.helper.Utils.isNetworkAvailable
import com.forkmang.helper.showToastMessage
import com.forkmang.network_call.Api.info
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class PickupSelectFoodFragment : Fragment() {

    var cartBookingArrayList: ArrayList<CartBooking>? = null
    var selectedId_radiobtn_topping = 0
    private val storePrefrence by lazy { StorePrefrence(requireContext()) }
    var all_orderFood_adapter: PickupFoodListAdapter? = null

    private var _binding: FragmentPickupLayoutBinding? = null
    private val binding get() = _binding!!

    private lateinit var rvCartDialog: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPickupLayoutBinding.inflate(inflater, container, false)
        binding.pickRecycleview.layoutManager = LinearLayoutManager(context)

        callApiFood1()
        observe()
        return binding.root
    }

    private fun observe() {

        viewModel.command.observe(viewLifecycleOwner) { command ->
            when (command) {
                COMMAND_CART_LIST_VIEW -> {
                    cartListingView()
                }
            }
        }

        viewModel.searchFoodItemData.observe(viewLifecycleOwner) { data ->
            all_orderFood_adapter = restoData?.let {
                PickupFoodListAdapter(
                    requireContext(),
                    requireActivity(),
                    data,
                    it
                ) { itemList ->
                    showAlertView(itemList)
                }
            }
            binding.pickRecycleview.adapter = all_orderFood_adapter
            binding.progressbar.visibility = View.GONE
        }

        viewModel.categoryItemData.observe(viewLifecycleOwner) { data ->
            context?.showToastMessage("reached in observe with $data")
            all_orderFood_adapter = restoData?.let { it1 ->
                PickupFoodListAdapter(
                    requireContext(),
                    requireActivity(),
                    data,
                    it1
                ) {
                    showAlertView(it)
                }
            }
            binding.pickRecycleview.adapter = all_orderFood_adapter
            binding.progressbar.visibility = View.GONE
        }

    }


    fun callApiAddToCart(
        item_id: String?,
        qty: String?,
        booking_table_id: String?,
        item_extra: String?,
        type: String?
    ) {
        //showProgress();
        binding.progressbar.visibility = View.VISIBLE
        info.addItemCart(
            "Bearer " + storePrefrence.getString(TOKEN_LOGIN),
            item_id,
            qty,
            booking_table_id,
            item_extra,
            storePrefrence.getString(
                Constant.IDENTFIER
            ),
            type,
            restoData?.id
        )
            ?.enqueue(object : Callback<JsonObject?> {
                override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                    try {
                        //Log.d("Result", jsonObject.toString());
                        if (response.code() == Constant.SUCCESS_CODE_n) {
                            val jsonObject = JSONObject(Gson().toJson(response.body()))
                            if (jsonObject.getString("status").equals("200", ignoreCase = true)) {
                                context?.showToastMessage(jsonObject.getString("message"))
                                storePrefrence.setString(
                                    Constant.CARTID,
                                    jsonObject.getJSONObject("data").getString("cart_id")
                                )
                                storePrefrence.setString(
                                    Constant.CART_ITEMID,
                                    jsonObject.getJSONObject("data").getString("item_id")
                                )
                                binding.progressbar.visibility = View.GONE
                                cartListingView()
                            }
                        } else if (response.code() == Constant.ERROR_CODE_n || response.code() == Constant.ERROR_CODE) {
                            val jsonObject = response.errorBody()?.string()?.let { JSONObject(it) }
                            binding.progressbar.visibility = View.GONE
                            context?.showToastMessage(jsonObject?.getString("message").toString())

                            /*JSONObject obj = new JSONObject(loadJSONFromAsset());
                                if(obj.getString("status").equalsIgnoreCase("200"))
                                {
                                    context?.showToastMessage(, obj.getString("message")+" offline "();
                                    storePrefrence.setString("cartid", obj.getJSONObject("data").getString("cart_id"));
                                    storePrefrence.setString("item_id", obj.getJSONObject("data").getString("item_id"));
                                    cartListingView();

                                }*/
                        }
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                        binding.progressbar.visibility = View.GONE
                        context?.showToastMessage("Error occur please try again")
                    }
                }

                override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                    context?.showToastMessage("Error occur please try again")
                    binding.progressbar.visibility = View.GONE
                    //stopProgress();
                }
            })
    }

    fun callApiFood1() {
        if (isNetworkAvailable(requireContext())) {
            viewModel.callApiFoodItem(category_id)
        } else {
            context?.showToastMessage(Constant.NETWORKEROORMSG)
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
        /*var radioButton4_extra: RadioButton
        var radioButton5_extra: RadioButton
        var radioButton6_extra: RadioButton*/
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
        val extra_toppingArrayList_get: ArrayList<Extra_Topping>? =
            category_itemList.extra_toppingArrayList
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
            layout2.addView(rb?.get(i))
            rb?.get(i)
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
        plus_btn.setOnClickListener {
            var value = txt_qty.text.toString().toInt()
            ++value
            txt_qty.text = value.toString()
        }
        minus1.setOnClickListener {
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
                extra = "1,2" //hardcoded please correct it
            }
            Log.d("extra", extra)
            Log.d("qty", qty)
            if (item_id != null) {
                Log.d("item_id", item_id)
            }
            Log.d("selectedId", "" + selectedId_radiobtn_topping)

            //api call
            if (isNetworkAvailable(requireContext())) {
                dialog.dismiss()
                callApiAddToCart(item_id, qty, "", extra, "pickup")
            } else {
                context?.showToastMessage(Constant.NETWORKEROORMSG)
            }
        }
        btn_reserve.setOnClickListener { dialog.dismiss() }
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
/*        val txt_datetime: TextView
        val etv_noperson: EditText
        var linear_view_layout_2: LinearLayout*/
        val linear_view1: LinearLayout = dialog.findViewById(R.id.linear_view1)
        linear_view1.visibility = View.GONE

        // linear_view_layout_2 =dialog.findViewById(R.id.linear_view_layout_2);
        // linear_view_layout_2.setVisibility(View.GONE);
        val txt_restroname: TextView = dialog.findViewById(R.id.txt_restroname)
        val txt_custname: TextView = dialog.findViewById(R.id.txt_custname)
        val txt_phoneno: TextView = dialog.findViewById(R.id.txt_phoneno)
        val btn_pay_table_food: Button = dialog.findViewById(R.id.btn_pay_table_food)
        val btn_pay_table: Button = dialog.findViewById(R.id.btn_pay_table)
        val img_close: ImageView = dialog.findViewById(R.id.img_close)
        rvCartDialog = dialog.findViewById(R.id.recycleview)
        txt_restroname.text = restoData?.rest_name ?: ""
        txt_custname.text = storePrefrence.getString(Constant.NAME)
        txt_phoneno.text = storePrefrence.getString(MOBILE)
        //txt_datetime.setText(tableList_get.getStr_time());
        img_close.setOnClickListener { dialog.dismiss() }
        rvCartDialog.layoutManager = LinearLayoutManager(activity)
        if (isNetworkAvailable(requireContext())) {
            callApiCartListView()
        } else {
            context?.showToastMessage(Constant.NETWORKEROORMSG)
        }
        btn_pay_table_food.setOnClickListener {
            dialog.dismiss()
            val mainIntent = Intent(context, ActivityPaymentSummary::class.java)
            //Bundle bundle = new Bundle();
            //bundle.putParcelableArrayList("cartbookingarraylist", cartBookingArrayList);
            mainIntent.putExtra("comingfrom", "PickupFood")
            mainIntent.putExtra("restromodel", restoData)
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
            String(buffer) // "UTF-8"
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

    fun callApiCartListView() {
        //showProgress();
        binding.progressbar.visibility = View.VISIBLE
        info.getCartDetail(
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
                                    if(data_obj.has("total")) {
                                        cartBooking.data_total = data_obj.getString("total")
                                    } else {
                                        cartBooking.data_total = ""
                                    }
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
                                    if (cart_detail_obj.has("cart_item_details")) {
                                        cartBooking.cart_item_details_category_id =
                                            cart_detail_obj.getJSONObject("cart_item_details")
                                                .getString("category_id")
                                        cartBooking.cart_item_details_name =
                                            cart_detail_obj.getJSONObject("cart_item_details")
                                                .getString("name")
                                        cartBooking.cart_item_details_price =
                                            cart_detail_obj.getJSONObject("cart_item_details")
                                                .getString("price")
                                    }

/*                                    //extra_item_details obj
                                    val extra_namelist = ArrayList<String>()
                                    val extra_pricelist = ArrayList<String>()
                                    if (cart_detail_obj.getJSONArray("extra_item_details").length() > 0) {
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
                                    cartBooking.extra_item_details_price = str_extraprice*/
                                    cartBookingArrayList?.add(cartBooking)
                                }
                                binding.progressbar.visibility = View.GONE
                                //call adapter
                                val pickupListingAdapter = PickupListingAdapter(
                                    requireContext(), cartBookingArrayList
                                ) { func_name, cart_item_id, qty_update ->
                                    when (func_name) {
                                        ADD_QTY -> {
                                            callApiAddQty(cart_item_id, qty_update)
                                        }
                                        REMOVE_CART_ITEM -> {
                                            callApiRemoveItemCart(cart_item_id)
                                        }
                                    }
                                }
                                rvCartDialog.adapter = pickupListingAdapter
                            }
                        } else if (response.code() == Constant.ERROR_CODE_n || response.code() == Constant.ERROR_CODE) {
                            val jsonObject = response.errorBody()?.string()?.let { JSONObject(it) }
                            context?.showToastMessage(jsonObject?.getString("message").toString())
                            binding.progressbar.visibility = View.GONE
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
                        binding.progressbar.visibility = View.GONE
                        context?.showToastMessage("Error occur please try again")
                    }
                }

                override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                    binding.progressbar.visibility = View.GONE
                    context?.showToastMessage("Error occur please try again")
                    //stopProgress();
                }
            })
    }

    fun callApiAddQty(cart_itemid: String?, qty: String?) {
        //showProgress();
        binding.progressbar.visibility = View.VISIBLE
        info.cartUpdateQty(
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
                                context?.showToastMessage(obj.getString("message"))
                                binding.progressbar.visibility = View.GONE
                                if (isNetworkAvailable(requireContext())) {
                                    callApiCartListView()
                                } else {
                                    context?.showToastMessage(Constant.NETWORKEROORMSG)
                                }
                            }
                        } else if (response.code() == Constant.ERROR_CODE_n || response.code() == Constant.ERROR_CODE) {
                            val jsonObject = response.errorBody()?.string()?.let { JSONObject(it) }
                            context?.showToastMessage(jsonObject?.getString("message").toString())
                            binding.progressbar.visibility = View.GONE
                        }
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                        binding.progressbar.visibility = View.GONE
                        context?.showToastMessage("Error occur please try again")
                    }
                }

                override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                    binding.progressbar.visibility = View.GONE
                    context?.showToastMessage("Error occur please try again")
                }
            })
    }

    fun callApiRemoveItemCart(cart_itemid: String?) {
        //showProgress();
        binding.progressbar.visibility = View.VISIBLE
        info.cartRemoveQty(
            "Bearer " + storePrefrence.getString(TOKEN_LOGIN),
            cart_itemid,
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
                                context?.showToastMessage(obj.getString("message"))
                                binding.progressbar.visibility = View.GONE
                                if (isNetworkAvailable(requireContext())) {
                                    callApiCartListView()
                                } else {
                                    context?.showToastMessage(Constant.NETWORKEROORMSG)
                                }
                            }
                        } else if (response.code() == Constant.ERROR_CODE_n || response.code() == Constant.ERROR_CODE) {
                            val jsonObject = response.errorBody()?.string()?.let { JSONObject(it) }
                            context?.showToastMessage(jsonObject?.getString("message").toString())
                            binding.progressbar.visibility = View.GONE
                        }
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                        binding.progressbar.visibility = View.GONE
                        context?.showToastMessage("Error occur please try again")
                    }
                }

                override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                    binding.progressbar.visibility = View.GONE
                    context?.showToastMessage("Error occur please try again")
                }
            })
    }

    companion object {
        var category_id: String? = null
        var restoData: RestoData? = null
        var booking_id: String? = "0"
        lateinit var viewModel: PickUpSelectFoodViewModel
        fun newInstance( /*TableList tableList,*/
            bookTable: RestoData,
            category_id_val: String?,
            booking_id_val: String?,
            vm: PickUpSelectFoodViewModel
        ): PickupSelectFoodFragment {
            category_id = category_id_val
            //Log.d("idval",category_id);
            //tableList_get = tableList;
            booking_id = booking_id_val
            restoData = bookTable
            viewModel = vm
            return PickupSelectFoodFragment()
        }
    }
}